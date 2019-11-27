package com.valkclashofclans.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Items {
	public static Item dropItem(Block block, double chance, ItemStack item) {
		double random = Math.random();
		if (random < chance) {
			return block.getWorld().dropItemNaturally(block.getLocation(), item);
		}
		return null;
	}
	
	public static Item dropItem(Location loc, double chance, ItemStack item) {
		double random = Math.random();
		if (random < chance) {
			return loc.getWorld().dropItemNaturally(loc, item);
		}
		return null;
	}
	
	public static ItemStack item(Material material, int amount, String name, String lore) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta item_meta = item.getItemMeta();
		item_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5" + name));
		List<String> item_lore = new ArrayList<String>();
		item_lore.add(ChatColor.translateAlternateColorCodes('&', "&d" + lore));
		item_meta.setLore(item_lore);
		item.setItemMeta(item_meta);
		return item;
	}

	public static ItemStack item(Material material, int amount, byte data, String name, String lore) {
		ItemStack item = new ItemStack(material, amount, data);
		ItemMeta item_meta = item.getItemMeta();
		item_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5" + name));
		List<String> item_lore = new ArrayList<String>();
		item_lore.add(ChatColor.translateAlternateColorCodes('&', "&d" + lore));
		item_meta.setLore(item_lore);
		item.setItemMeta(item_meta);
		return item;
	}

	public static ItemStack item(Material material) {
		ItemStack item = new ItemStack(material);
		return item;
	}
	
	public static ItemStack invInfo(Material item, String name, String[] lore) {
		ItemStack stack = new ItemStack(item);
		ItemMeta stack_meta = stack.getItemMeta();
		stack_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + name));
		List<String> item_lore = new ArrayList<String>();
		for (int i = 0; i < lore.length; i++) {
			item_lore.add(ChatColor.translateAlternateColorCodes('&', "&2" + lore[i]));
		}
		stack_meta.setLore(item_lore);
		stack.setItemMeta(stack_meta);
		return stack;
	}
	
	public static ItemStack invClassInfo(Material item, String name, String[] lore, int[] level) {
		ItemStack stack = new ItemStack(item);
		ItemMeta stack_meta = stack.getItemMeta();
		stack_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + name));
		List<String> item_lore = new ArrayList<String>();
		for (int i = 0; i < lore.length; i++) {
			item_lore.add(
					ChatColor.translateAlternateColorCodes('&', "&2" + lore[i] + " &8(&7LvL &a" + level[i] + "&8)"));
		}
		stack_meta.setLore(item_lore);
		stack.setItemMeta(stack_meta);
		return stack;
	}
	
	@SuppressWarnings("deprecation")
	public static void moreBlocks(Location loc, int width, int length, Material check, Material set) {
		loc.setX(loc.getBlockX() - (width / 2));
		loc.setZ(loc.getBlockZ() - (length / 2));
		for (int x = 0; x < (width + 1); x++) {
			for (int z = 0; z < (length + 1); z++) {
				loc.setY(loc.getBlockY() - 1);
				if (loc.getBlock().getType().equals(check)) {
					loc.setY(loc.getBlockY() + 1);
					if (loc.getBlock().getType().equals(Material.AIR)) {
						loc.getBlock().setType(set);
						loc.getBlock().setData((byte) 1);
					}
				} else {
					loc.setY(loc.getBlockY() + 1);
				}
				loc.setZ(loc.getBlockZ() + 1);
			}
			loc.setZ(loc.getBlockZ() - (length + 1));
			loc.setX(loc.getBlockX() + 1);
		}
	}
}
