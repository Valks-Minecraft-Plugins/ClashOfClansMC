package com.valkclashofclans.blocks;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkclashofclans.ValkClashOfClans;
import com.valkclashofclans.utils.Hologram;

import net.md_5.bungee.api.ChatColor;

public class RespawningBlocks implements Listener {
	ValkClashOfClans plugin = null;

	public RespawningBlocks() {
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
	}

	@EventHandler
	private void respawningBlocks(BlockBreakEvent e) {
		e.setCancelled(true);

		final Player p = e.getPlayer();
		final Block b = e.getBlock();

		GameMode gm = p.getGameMode();

		if (gm.equals(GameMode.CREATIVE)) {
			b.setType(Material.AIR);
		}

		respawningBlock(b.getLocation(), p, Material.GOLD_ORE, (byte) 0, "&e+1 gold", Material.COBBLESTONE, new ItemStack(Material.GOLD_INGOT), 10);
		respawningBlock(b.getLocation(), p, Material.CROPS, (byte) 7, "&a+2 wheat", Material.SEEDS, new ItemStack(Material.WHEAT, 2), 10);
	}
	
	@SuppressWarnings("deprecation")
	public void respawningBlock(Location bloc, Player p, final Material respawningBlock, byte respawningBlockData, String name, Material setBlock, ItemStack itemDropped, int respawnTime) {
		boolean valid = false;
		if (p == null) {
			valid = true;
		} else {
			if (p.getGameMode().equals(GameMode.SURVIVAL)) {
				valid = true;
			} else {
				valid = false;
			}
		}
		
		final Block b = bloc.getBlock();
		Material material = b.getType();
		World w = b.getWorld();
	
		long blockRespawnTime = respawnTime * 20;
		
		if (material.equals(respawningBlock) && valid) {
			if (b.getData() == respawningBlockData) {
				final Hologram hg = new Hologram(bloc.add(0.5d, 0, 0.5d), ChatColor.translateAlternateColorCodes('&', name));
				hg.move();
				
				b.setType(setBlock);
				if (itemDropped != null) {
					w.dropItem(bloc, itemDropped);
				}
				
				new BukkitRunnable() {
					@Override
					public void run() {
						hg.destroy();
					}
				}.runTaskLater(plugin, 40); // 2 seconds later
	
				new BukkitRunnable() {
					@Override
					public void run() {
						b.setType(respawningBlock);
					}
				}.runTaskLater(plugin, blockRespawnTime); // 10 seconds later
			}
		}
	}
}
