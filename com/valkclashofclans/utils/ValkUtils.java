package com.valkclashofclans.utils;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.npc.NPC;

public class ValkUtils {
	public ValkUtils() {

	}

	public Entity findNearestPlayer(NPC npc) {
		List<LivingEntity> list = npc.getEntity().getWorld().getLivingEntities();
		for (LivingEntity entity : list) {
			if (entity.getType().equals(EntityType.PLAYER)) {
				if (entity instanceof Player) {
					Player p = (Player) entity;
					if (p.getGameMode().equals(GameMode.SURVIVAL)) {
						return entity;
					}
				}
				
			}
		}
		return null;
	}

	public Entity findNearestAttacker(NPC npc) {
		List<LivingEntity> entities = npc.getEntity().getWorld().getLivingEntities();
		for (LivingEntity entity : entities) {
			if (entity instanceof Monster) {
				return entity;
			}
		}
		return null;
	}

	public Location findBlock(NPC npc, Location loc, Material mat, int radius) {
		Block middle = npc.getStoredLocation().getBlock();
		for (int x = radius; x >= -radius; x--) {
			for (int y = radius; y >= -radius; y--) {
				for (int z = radius; z >= -radius; z--) {
					if (middle.getRelative(x, y, z).getType() == mat) {
						loc = middle.getRelative(x, y, z).getLocation();
						return loc;
					}
				}
			}
		}
		return null;
	}

	public boolean hasEmptySlot(Inventory inv) {
		for (ItemStack stack : inv.getContents()) {
			if (stack == null) {
				return true;
			}
		}
		return false;
	}
}
