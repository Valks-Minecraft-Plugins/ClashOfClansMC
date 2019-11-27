package com.valkclashofclans.npctraits;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.valkclashofclans.ValkClashOfClans;
import com.valkclashofclans.commands.Test;
import com.valkclashofclans.structures.UpgradeStructure;
import com.valkclashofclans.utils.ValkUtils;

import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class Pirate extends Trait {
	
	int structStartX;
	int structStartY;
	int structStartZ;
	
	int structEndX;
	int structEndY;
	int structEndZ;
	
	int animationAttacking = 0;
	
	Location target = null;
	
	ValkClashOfClans plugin = null;
	public Pirate() {
		super("pirate");
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
		npc.getNavigator().getLocalParameters().range(100);
		npc.setProtected(false);
	}
	
	final int SEARCH_INTERVAL = 20;
	
	@Override
	public void run() {
		final Configuration npcConfig = plugin.getNpcConfig();
		final String id = npc.getUniqueId().toString();
		
		Configuration islandConfig = plugin.getIslandConfig();
		
		if (!npc.isSpawned()) {
			return;
		}
		
		if (!npcConfig.isSet(id + ".state")) {
			return;
		}
		
		switch(npcConfig.getString(id + ".state")) {
		case "FIND_TARGET":
			Equipment equipTrait = npc.getTrait(Equipment.class);
			equipTrait.set(EquipmentSlot.HAND, new ItemStack(Material.IRON_AXE));
			equipTrait.set(EquipmentSlot.OFF_HAND, new ItemStack(Material.SHIELD));
			
			String playerStringUUID = npcConfig.getString(id + ".island");
			
			String thePath = playerStringUUID + ".island";
			
			Player p = Bukkit.getPlayer(UUID.fromString(playerStringUUID));
			
			UpgradeStructure us = new UpgradeStructure(islandConfig, thePath, p, "townhall", true, -1);
			
			int islandX = islandConfig.getInt(thePath + ".x");
			int islandY = islandConfig.getInt(thePath + ".y");
			int islandZ = islandConfig.getInt(thePath + ".z");
			
			structStartX = islandX + us.getOffsetX();
			structStartY = islandY + us.getOffsetY();
			structStartZ = islandZ + us.getOffsetZ();
			
			structEndX = structStartX + us.getWidth();
			structEndY = structStartY + us.getHeight();
			structEndZ = structStartZ + us.getLength();
			
			target = new Location(p.getWorld(), (structStartX + structEndX) / 2, (structStartY + structEndY) / 2, (structStartZ + structEndZ) / 2);
			
			npcConfig.set(id + ".state", "ATTACK");
			break;
		case "ATTACK":
			if (target != null) {
				ValkUtils vu = new ValkUtils();
				Entity player = vu.findNearestPlayer(npc);
				
				if (player != null) {
					npc.getNavigator().setTarget(player , true);
				} else {
					npc.getNavigator().setTarget(target);
					

					if (npc.getEntity().getLocation().distance(target) < 15) {
						animationAttacking++;
						if (Test.bb.getProgress() >= 0.01) {
							if (animationAttacking % 10 == 0) {
								Test.bb.setProgress(Test.bb.getProgress() - 0.01);
							}
						} else {
							Test.bb.removeAll();
							npc.destroy();
						}
					}
				}
			}
			break;
		}
	}
	
	@Override
	public void onSpawn() {
		npc.getEntity().setCustomNameVisible(false);
	}
	
	@Override
	public void onRemove() {
		Configuration npcConfig = plugin.getNpcConfig();
		String id = npc.getUniqueId().toString();
		npcConfig.set(id, null);
	}
}
