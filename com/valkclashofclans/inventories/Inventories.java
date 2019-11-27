package com.valkclashofclans.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import com.valkclashofclans.utils.Items;

public class Inventories {
	public static Inventory help() {
		Inventory inv = Bukkit.createInventory(null, 27, "Island");
		inv.setItem(0, Items.invInfo(Material.GRASS, "Create", new String[] { "Create an Island" }));
		inv.setItem(1, Items.invInfo(Material.BRICK, "Structures", new String[] { "View Island Structures" }));
		inv.setItem(2, Items.invInfo(Material.COMPASS, "Teleport", new String[] { "Teleport to Island" }));
		inv.setItem(3, Items.invInfo(Material.ANVIL, "DELETE", new String[] {"Do not use.. for debugging only."}));
		return inv;
	}

	public static Inventory structures() {
		Inventory inv = Bukkit.createInventory(null, 27, "Structures");
		inv.setItem(0, Items.invInfo(Material.LOG, "Town Hall", new String[] { "Main Island Structure" }));
		inv.setItem(1, Items.invInfo(Material.CHEST, "Storage", new String[] { "For storing gold." }));
		inv.setItem(2, Items.invInfo(Material.WOOD_PICKAXE, "Mine", new String[] { "For mining gold." }));
		inv.setItem(3, Items.invInfo(Material.WHEAT, "Farm", new String[] { "Food matters. Lives matter. lol" }));
		inv.setItem(4, Items.invInfo(Material.COBBLE_WALL, "Wall", new String[] { "Protect colony." }));
		inv.setItem(5, Items.invInfo(Material.WOOL, "Towers", new String[] { "Look out towers." }));
		inv.setItem(6, Items.invInfo(Material.BONE, "Temple", new String[] { "For choosing a class." }));
		inv.setItem(7, Items.invInfo(Material.FEATHER, "Market", new String[] { "For purchasing items." }));
		inv.setItem(8, Items.invInfo(Material.COAL, "Workshop", new String[] { "For researching stuff." }));
		return inv;
	}

	public static Inventory townhall() {
		Inventory inv = Bukkit.createInventory(null, 27, "TownHall");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}

	public static Inventory storage() {
		Inventory inv = Bukkit.createInventory(null, 27, "Storage");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}
	
	public static Inventory storageclick() {
		Inventory inv = Bukkit.createInventory(null, 27, "storageclick");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		inv.setItem(1, Items.invInfo(Material.GOLD_NUGGET, "Deposit", new String[] { "Deposit Gold into Storage" }));
		inv.setItem(2, Items.invInfo(Material.GOLD_INGOT, "View", new String[] { "View Gold in Storage" }));
		return inv;
	}

	public static Inventory mine() {
		Inventory inv = Bukkit.createInventory(null, 27, "Mine");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}

	public static Inventory farm() {
		Inventory inv = Bukkit.createInventory(null, 27, "Farm");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}

	public static Inventory wall() {
		Inventory inv = Bukkit.createInventory(null, 27, "Wall");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}

	public static Inventory towers() {
		Inventory inv = Bukkit.createInventory(null, 27, "Towers");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}

	public static Inventory temple() {
		Inventory inv = Bukkit.createInventory(null, 27, "Temple");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		inv.setItem(1, Items.invInfo(Material.FEATHER, "Class", new String[] { "Choose a Class" }));
		return inv;
	}

	public static Inventory classes() {
		Inventory inv = Bukkit.createInventory(null, 27, "Classes");
		inv.setItem(0, Items.invInfo(Material.WOOD_SWORD, "Tank", new String[] { "Specializes in Swords" }));
		inv.setItem(1, Items.invInfo(Material.BOW, "Archer", new String[] { "Specializes in Bows" }));
		inv.setItem(2, Items.invInfo(Material.STICK, "Wizard", new String[] { "Specializes in Magic" }));
		return inv;
	}
	
	public static Inventory market() {
		Inventory inv = Bukkit.createInventory(null, 27, "Market");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}
	
	public static Inventory workshop() {
		Inventory inv = Bukkit.createInventory(null, 27, "Workshop");
		inv.setItem(0, Items.invInfo(Material.NETHER_STAR, "Upgrade", new String[] { "Upgrade to next Tier" }));
		return inv;
	}
}
