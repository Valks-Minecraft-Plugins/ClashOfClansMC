package com.valkclashofclans.structures;

import java.io.File;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.valkclashofclans.ValkClashOfClans;
import com.valkclashofclans.utils.Schematic;

public class UpgradeStructure {
	ValkClashOfClans plugin = null;
	
	Configuration config;
	String path;
	String structure;
	Schematic struct;
	Player p;
	
	boolean pasteSlow;
	
	File schematic;
	
	int newLvl;
	
	/*
	 * For regsitering the structure clicks.
	 */
	public UpgradeStructure(Configuration config, String path, Player p, String structure, boolean pasteSlow, int lvlOffset) {
		newLvl = config.getInt(path + ".structures." + structure) + 1 + lvlOffset;
		struct = new Schematic(path, config, p, structure + newLvl, pasteSlow);
	}
	
	/*
	 * Main constructor.
	 */
	public UpgradeStructure(Configuration config, String path, Player p, String structure, boolean pasteSlow) {
		this.config = config;
		this.path = path;
		this.structure = structure;
		this.p = p;
		this.pasteSlow = pasteSlow;
		
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
		
		newLvl = config.getInt(path + ".structures." + structure) + 1;
		
		schematic = new File(plugin.getDataFolder().toString() + "\\schematics\\", 
				structure + newLvl + ".schematic");
		
		
	}
	
	public void pasteStructure() {
		if (!schematic.exists()) {
			p.sendMessage(ValkClashOfClans.getPrefix() + "Level " + (newLvl - 1) + " is the max level for " + structure + "!");
			return;
		}
		
		p.sendMessage(ValkClashOfClans.getPrefix() + "Upgrading " + structure + " to level " + newLvl + ".");
		struct = new Schematic(path, config, p, structure + newLvl, pasteSlow);
		
		struct.pasteSchematic();
		config.set(path + ".structures." + structure, newLvl);
	}
	
	public short getOffsetX() {
		return struct.getOffsetX();
	}
	
	public short getOffsetY() {
		return struct.getOffsetY();
	}
	
	public short getOffsetZ() {
		return struct.getOffsetZ();
	}
	
	public short getWidth() {
		return struct.getWidth();
	}
	
	public short getHeight() {
		return struct.getHeight();
	}
	
	public short getLength() {
		return struct.getLength();
	}
}
