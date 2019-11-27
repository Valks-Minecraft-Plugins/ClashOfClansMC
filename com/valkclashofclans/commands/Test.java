package com.valkclashofclans.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkclashofclans.ValkClashOfClans;
import com.valkclashofclans.npctraits.MyTrait;
import com.valkclashofclans.npctraits.Pirate;
import com.valkclashofclans.utils.Schematic;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;

public class Test implements CommandExecutor, Listener {
	ValkClashOfClans plugin = null;
	
	public static BossBar bb = null;

	public Test() {
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test")) {
			final Configuration config = plugin.getNpcConfig();

			final Player p = Bukkit.getPlayer(sender.getName());

			final NPCRegistry registry = CitizensAPI.getNPCRegistry();

			if (args.length < 1) {
				sender.sendMessage("Specify at least 1 argument.");
				return true;
			}

			if (args[0].equalsIgnoreCase("friendly")) {
				NPC npc = registry.createNPC(EntityType.PLAYER, "");

				String id = npc.getUniqueId().toString();
				config.set(id + ".owner", p.getUniqueId().toString());
				config.set(id + ".state", "IDLE");

				npc.data().setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false);
				npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, p.getName());
				npc.spawn(p.getLocation());
				npc.getEntity().setCustomNameVisible(false);
				npc.addTrait(MyTrait.class);

				Equipment equipTrait = npc.getTrait(Equipment.class);
				equipTrait.set(0, new ItemStack(Material.IRON_PICKAXE));
			}

			/*
			 * PIRATE INVASION EVENT
			 */
			if (args[0].equalsIgnoreCase("pirate")) {
				bb = Bukkit.createBossBar("Town Center", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG);
				
				bb.setProgress(1.0);
				bb.addPlayer(p);
				
				final Configuration islandConfig = plugin.getIslandConfig();

				String id = p.getUniqueId().toString();
				final String path = id + ".island";

				Schematic struct = new Schematic(path, islandConfig, p, "pirateship", false);
				struct.pasteSchematic();

				p.sendMessage("Pirate ship spawned!");

				int x = islandConfig.getInt(path + ".x");
				int y = islandConfig.getInt(path + ".y");
				int z = islandConfig.getInt(path + ".z");
				
				final Location npcspawnloc = new Location(p.getWorld(), x + struct.getOffsetX(), y + struct.getOffsetY(),
						z + struct.getOffsetZ());

				new BukkitRunnable() {

					int counter = 0;
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						counter++;
						if (counter >= 3) {
							cancel();
						}
						NPC npc = registry.createNPC(EntityType.SKELETON, "pirate");
						
						npc.addTrait(Pirate.class);
						
						npc.spawn(npcspawnloc);
						
						String npcid = npc.getUniqueId().toString();
						config.set(npcid + ".state", "FIND_TARGET");
						config.set(npcid + ".island", p.getUniqueId().toString());

						p.sendMessage("A pirate spawned..");
						
						
					}

				}.runTaskTimer(plugin, 20 * 5, 20);

				new BukkitRunnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Schematic struct = new Schematic(path, islandConfig, p, "pirateshipremoved", false);
						struct.pasteSchematic();
						p.sendMessage("Pirate ship removed!");
					}

				}.runTaskLater(plugin, 20 * 15);
			}

			return true;
		}
		return true;
	}

}
