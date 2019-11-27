package com.valkclashofclans.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkclashofclans.ValkClashOfClans;
import com.valkclashofclans.configs.PlayerFiles;
import com.valkclashofclans.inventories.Inventories;
import com.valkclashofclans.structures.UpgradeStructure;
import com.valkclashofclans.utils.GenIslands;
import com.valkclashofclans.utils.Schematic;
import com.valkclashofclans.utils.Structure;

public class Island implements CommandExecutor, Listener {
	ValkClashOfClans plugin = null;

	public Island() {
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
	}

	@EventHandler
	private void registerInventoryClicks(InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();

		Configuration islandConfig = plugin.getIslandConfig();

		String id = p.getUniqueId().toString();
		String path = id + ".island";

		int islandGold = islandConfig.getInt(path + ".gold");
		int slot = e.getSlot();

		String invName = e.getInventory().getName().toLowerCase();

		if (!islandConfig.isSet(path + ".established")) {
			islandConfig.set(path + ".established", false);
		}

		if (invName.equals("storageclick")) {
			switch (slot) {
			case 0: //upgrade
				if (canBuyStructure(p, path, "storage", islandGold, islandConfig)) {
					int storagetier = islandConfig.getInt(path + ".structures.storage");
					islandConfig.set(path + ".maxgold", (storagetier + 1) * 100);
					UpgradeStructure us = new UpgradeStructure(islandConfig, path, p, "storage", true);
					us.pasteStructure();
				}
				p.closeInventory();
				break;
			case 1: // deposit
				PlayerFiles cm = PlayerFiles.getConfig(p);
				FileConfiguration playerConfig = cm.getConfig();

				int maxStorageGold = islandConfig.getInt(path + ".maxgold");
				int storageGold = islandConfig.getInt(path + ".gold");

				ItemStack[] contents = p.getInventory().getContents();
				int playerGold = 0;
				for (int i = 0; i < contents.length; i++) {
					if (contents[i] != null) {
						if (contents[i].getType().equals(Material.GOLD_INGOT)) {
							int stack = contents[i].getAmount();
							playerGold += stack;
							contents[i].setType(Material.AIR);
						}
					}
				}
				p.getInventory().setContents(contents);

				int canBePutInto = Math.min(playerGold, maxStorageGold - storageGold);
				if (playerGold > maxStorageGold - storageGold) {
					int excess = playerGold - (maxStorageGold - storageGold);
					p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, excess));
				}
				playerConfig.set("gold", playerConfig.getInt("gold") - canBePutInto);
				islandConfig.set(path + ".gold", islandConfig.getInt(path + ".gold") + canBePutInto);
				if (canBePutInto == 0) {
					p.sendMessage(ValkClashOfClans.getPrefix()
							+ "You either have no gold to deposit or cannot deposit anymore because your storage is maxed out.");
				} else {
					p.sendMessage(ValkClashOfClans.getPrefix() + "Deposited " + canBePutInto + " gold into storage.");
				}
				p.closeInventory();

				cm.saveConfig();
				p.closeInventory();
				break;
			case 2:
				if (islandConfig.getInt(path + ".maxgold") == 0) {
					p.sendMessage(ValkClashOfClans.getPrefix() + "Your island cannot hold any gold yet. Upgrade your storage!");
				} else {
					p.sendMessage(ValkClashOfClans.getPrefix() + "Island gold: " + islandConfig.getInt(path + ".gold") + "/"
							+ islandConfig.getInt(path + ".maxgold"));
				}
				p.closeInventory();
				break;
			}
		}

		if (invName.equals("classes")) {
			PlayerFiles cm = PlayerFiles.getConfig((Player) e.getWhoClicked());
			FileConfiguration config = cm.getConfig();
			switch (slot) {
			case 0:
				p.sendTitle("Tank", "The Way of the Sword", 100, 200, 100);
				config.set("class", "tank");
				cm.saveConfig();
				p.closeInventory();
				break;
			case 1:
				p.sendTitle("Archer", "The Way of the Bow", 100, 200, 100);
				config.set("class", "archer");
				cm.saveConfig();
				p.closeInventory();
				break;
			case 2:
				p.sendTitle("Wizard", "The Way of the Stick", 100, 200, 100);
				config.set("class", "wizard");
				cm.saveConfig();
				p.closeInventory();
				break;
			}
		}

		if (invName.equals("structures")) {
			String structure;
			switch (slot) {
			case 0: // townhall
				structure = "townhall";
				break;
			case 1: // storage
				structure = "storage";
				break;
			case 2: // mine
				structure = "mine";
				break;
			case 3: // farm
				structure = "farm";
				break;
			case 4: // wall
				structure = "wall";
				break;
			case 5: // towers
				structure = "towers";
				break;
			case 6:
				structure = "temple";
				break;
			case 7:
				structure = "market";
				break;
			case 8:
				structure = "workshop";
				break;
			default:
				structure = null;
			}

			// "storage" is an anomoly
			if (structure == "storage") {
				if (canBuyStructure(p, path, "storage", islandGold, islandConfig)) {
					int storagetier = islandConfig.getInt(path + ".structures.storage");
					islandConfig.set(path + ".maxgold", (storagetier + 1) * 100);
					UpgradeStructure us = new UpgradeStructure(islandConfig, path, p, "storage", true);
					us.pasteStructure();
				}
				p.closeInventory();
			} else {
				if (structure != null) {
					if (canBuyStructure(p, path, structure, islandGold, islandConfig)) {
						UpgradeStructure us = new UpgradeStructure(islandConfig, path, p, structure, false);
						us.pasteStructure();
					}
					p.closeInventory();
				}
			}
		}
		
		String[] structures = new String[] {"townhall", "mine", "storage", "wall", "temple", "workshop", "market", "farm"};
		for (String structure: structures) {
			if (invName.equals(structure)) {
				if (slot == 0) {
					if (canBuyStructure(p, path, structure, islandGold, islandConfig)) {
						UpgradeStructure us = new UpgradeStructure(islandConfig, path, p, structure, false);
						us.pasteStructure();
					}
					p.closeInventory();
				}
			}
		}

		if (invName.equals("island")) {
			switch (slot) {
			case 0: // CREATE
				int spacing = 256;

				GenIslands gi = new GenIslands(islandConfig.getInt("islands") + 1);
				final int x = gi.x * spacing;
				final int y = 35; // do not change. its based on world gen value.
				final int z = gi.z * spacing;

				if (islandConfig.getBoolean(path + ".established") == false) {
					islandConfig.set(path + ".established", true);
					islandConfig.set(path + ".x", x);
					islandConfig.set(path + ".y", y);
					islandConfig.set(path + ".z", z);
					islandConfig.set(path + ".world", p.getWorld().getName());

					islandConfig.set("islands", islandConfig.getInt("islands") + 1);

					islandConfig.set(path + ".gold", 0);
					islandConfig.set(path + ".maxgold", 0);

					plugin.saveIslandConfig();

					Schematic struct = new Schematic(path, islandConfig, p, "island", false);
					struct.pasteSchematic();

					p.sendMessage(ValkClashOfClans.getPrefix() + "Your island was created. Teleporting you to it in 5 seconds..");
					final World w = Bukkit.getWorld(islandConfig.getString(path + ".world"));
					new BukkitRunnable() {
						public void run() {
							p.teleport(new Location(w, x, y, z));
							p.sendTitle("Withered", "", 50, 100, 50);
						}
					}.runTaskLater(plugin, 100);
				} else {
					p.sendMessage(ValkClashOfClans.getPrefix() + "You've already created your island.");
				}
				p.closeInventory();
				break;
			case 1: // upgrade
				if (islandConfig.getBoolean(path + ".established") == false) {
					p.sendMessage(
							ValkClashOfClans.getPrefix() + "You must create your island before you may view its structures.");
					p.closeInventory();
					return;
				}

				p.openInventory(Inventories.structures());
				break;
			case 2: // tp
				if (islandConfig.getBoolean(path + ".established") == false) {
					p.sendMessage(ValkClashOfClans.getPrefix() + "You must create your island before you may teleport to it.");
					p.closeInventory();
				} else {
					int xx = islandConfig.getInt(path + ".x");
					int yy = islandConfig.getInt(path + ".y");
					int zz = islandConfig.getInt(path + ".z");
					final World w = Bukkit.getWorld(islandConfig.getString(path + ".world"));
					p.teleport(new Location(w, xx, yy, zz));
					p.sendTitle("Withered", "", 50, 100, 50);
				}
				p.closeInventory();
				break;
			case 3: // delete
				if (islandConfig.getBoolean(path + ".established") == false) {
					p.sendMessage(ValkClashOfClans.getPrefix() + "You must create your island before you may delete it.");
					p.closeInventory();
				} else {
					islandConfig.set(path, null);
					plugin.saveIslandConfig();
				}
				p.closeInventory();
				break;
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("island")) {
			final Player p = Bukkit.getPlayer(sender.getName());
			p.openInventory(Inventories.help());
			return true;
		}
		return true;
	}

	private boolean canBuyStructure(CommandSender sender, String path, String structure, int islandGold,
			Configuration islandConfig) {
		Configuration config = plugin.getIslandConfig();
		Structure struct = new Structure(config, path, structure, 10);
		int structCost = struct.getCost();
		int structTier = struct.getTier();

		int townhalltier = islandConfig.getInt(path + ".structures.townhall");

		if (!structure.equals("townhall")) {
			if (townhalltier < structTier) {
				sender.sendMessage(ValkClashOfClans.getPrefix() + "You need to upgrade your level " + townhalltier
						+ " Town Hall if you want to upgrade " + structure + " to level " + structTier);
				return false;
			}
		}

		if (structTier != 1) {
			if (islandGold < structCost) {
				sender.sendMessage(ValkClashOfClans.getPrefix() + "You need " + (structCost - islandGold)
						+ " more gold to upgrade " + structure + "!");
				return false;
			} else {
				islandConfig.set(path + ".gold", islandConfig.getInt(path + ".gold") - structCost);
				return true;
			}
		}
		return true;
	}
}
