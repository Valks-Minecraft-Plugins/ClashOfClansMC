package com.valkclashofclans.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import com.valkclashofclans.ValkClashOfClans;
import com.valkclashofclans.inventories.Inventories;
import com.valkclashofclans.structures.UpgradeStructure;

public class RegisterStructureClicks implements Listener {
	ValkClashOfClans plugin = null;
	UpgradeStructure us;

	int structStartX;
	int structStartY;
	int structStartZ;

	int structEndX;
	int structEndY;
	int structEndZ;

	int blocX;
	int blocY;
	int blocZ;

	public RegisterStructureClicks() {
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
	}

	@EventHandler
	private void registerClicks(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		String id = p.getUniqueId().toString();
		String path = id + ".island";

		Configuration config = plugin.getIslandConfig();

		Location loc = null;
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getHand().equals(EquipmentSlot.HAND)) {
				if (!(e.getClickedBlock().getType() == Material.GRASS)) {
					loc = e.getClickedBlock().getLocation();
					String[] structures = new String[] {"townhall", "storage", "mine", "farm", "temple", "market", "workshop"};
					for (String structure: structures) {
						if (isStructureSet(config, path, structure)) {
							registerClicksForStructure(config, path, p, loc, structure);
							if (clickInRegion()) {
								switch (structure) {
								case "townhall":
									p.openInventory(Inventories.townhall());
									break;
								case "storage":
									p.openInventory(Inventories.storageclick());
									break;
								case "mine":
									p.openInventory(Inventories.mine());
									break;
								case "farm":
									p.openInventory(Inventories.farm());
									break;
								case "temple":
									p.openInventory(Inventories.temple());
									break;
								case "market":
									p.openInventory(Inventories.market());
									break;
								case "workshop":
									p.openInventory(Inventories.workshop());
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean clickInRegion() {
		if (blocX >= structStartX && blocX <= structEndX) {
			if (blocY >= structStartY && blocY <= structEndY) {
				if (blocZ >= structStartZ && blocZ <= structEndZ) {
					return true;
				}
			}
		}
		return false;
	}

	private void registerClicksForStructure(Configuration config, String path, Player p, Location loc, String structure) {
		blocX = loc.getBlockX();
		blocY = loc.getBlockY();
		blocZ = loc.getBlockZ();
		
		UpgradeStructure us = new UpgradeStructure(config, path, p, structure, true, -1);

		int islandX = config.getInt(path + ".x");
		int islandY = config.getInt(path + ".y");
		int islandZ = config.getInt(path + ".z");

		structStartX = islandX + us.getOffsetX();
		structStartY = islandY + us.getOffsetY();
		structStartZ = islandZ + us.getOffsetZ();

		structEndX = structStartX + us.getWidth();
		structEndY = structStartY + us.getHeight();
		structEndZ = structStartZ + us.getLength();
	}

	private boolean isStructureSet(Configuration config, String path, String structure) {
		if (config.isSet(path + ".structures." + structure)) {
			return true;
		}
		return false;
	}
}
