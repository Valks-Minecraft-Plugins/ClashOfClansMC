package com.valkclashofclans;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.valkclashofclans.blocks.RegisterStructureClicks;
import com.valkclashofclans.blocks.RespawningBlocks;
import com.valkclashofclans.commands.Island;
import com.valkclashofclans.commands.Test;
import com.valkclashofclans.configs.LoadPlayerFiles;
import com.valkclashofclans.mobs.BlockSpawns;
import com.valkclashofclans.npctraits.MyTrait;
import com.valkclashofclans.npctraits.Pirate;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitFactory;
import net.citizensnpcs.api.trait.TraitInfo;

public class ValkClashOfClans extends JavaPlugin {
	public static ValkClashOfClans main;
	
	File islandConfigFile = new File(getDataFolder(), "islands.yml");
	FileConfiguration islandConfig = YamlConfiguration.loadConfiguration(islandConfigFile);
	
	File npcConfigFile = new File(getDataFolder(), "npcs.yml");
	FileConfiguration npcConfig = YamlConfiguration.loadConfiguration(npcConfigFile);
	
	@Override
	public void onEnable() {
		if (!checkCitizensValid()) {
			return;
		}
		
		registerTraits();
		registerCommands();
		registerEvents();
	}
	
	@Override
	public void onDisable() {
		saveNpcConfig();
		saveIslandConfig();
	}
	
	private boolean checkCitizensValid() {
		if (getServer().getPluginManager().getPlugin("Citizens") == null
				|| getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().log(java.util.logging.Level.SEVERE, "Citizens 2.0 not found or not enabled");
			getServer().getPluginManager().disablePlugin(this);
			return false;
		}
		return true;
	}
	
	private void registerTraits() {
		TraitFactory tf = CitizensAPI.getTraitFactory();
		tf.registerTrait(TraitInfo.create(MyTrait.class));
		tf.registerTrait(TraitInfo.create(Pirate.class));
	}
	
	private void registerCommands() {
		getCommand("test").setExecutor(new Test());
		getCommand("island").setExecutor(new Island());
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new LoadPlayerFiles(), this);
		pm.registerEvents(new Island(), this);
		pm.registerEvents(new RespawningBlocks(), this);
		pm.registerEvents(new BlockSpawns(), this);
		pm.registerEvents(new RegisterStructureClicks(), this);
	}
	
	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', "&8{&f" + "Clash of Clans" + "&8} &7");
	}
	
	public Configuration getNpcConfig() {
		return npcConfig;
	}
	
	public void saveNpcConfig() {
		try {
			npcConfig.save(npcConfigFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Configuration getIslandConfig() {
		return islandConfig;
	}
	
	public void saveIslandConfig() {
		try {
			islandConfig.save(islandConfigFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
