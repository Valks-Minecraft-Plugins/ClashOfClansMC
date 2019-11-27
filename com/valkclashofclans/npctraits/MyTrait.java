package com.valkclashofclans.npctraits;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkclashofclans.ValkClashOfClans;
import com.valkclashofclans.blocks.RespawningBlocks;
import com.valkclashofclans.utils.Items;
import com.valkclashofclans.utils.ValkUtils;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.util.PlayerAnimation;

//This is your trait that will be applied to a npc using the /trait mytraitname command. Each NPC gets its own instance of this class.
//the Trait class has a reference to the attached NPC class through the protected field 'npc' or getNPC().
//The Trait class also implements Listener so you can add EventHandlers directly to your trait.
@TraitName("mytraitname") // convenience annotation in recent CitizensAPI versions for specifying trait
										// name
public class MyTrait extends Trait {
	ValkClashOfClans plugin = null;
	public MyTrait() {
		super("mytraitname");
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
	}

	final double MIN_DIST = 1.5; //Minimum distance to set target.
	final int SEARCH_RADIUS = 50; //Search in a radius of 50 blocks.
	final long SEARCH_INTERVAL = 20 * 5; //Every 5 seconds.
	
	Location mineloc = null;
	Location storageloc = null;
	Location townhallloc = null;
	int animationMining = 0;
	Entity player = null;
	Entity attacker = null;

	@EventHandler
	private void rightClick(NPCRightClickEvent e) {
		e.getClicker().openInventory(npcGUI(e.getNPC().getUniqueId().toString()));
	}

	@EventHandler
	private void registerClicks(InventoryClickEvent e) {
		Configuration npcConfig = plugin.getNpcConfig();
		int slot = e.getSlot();
		String id = npc.getUniqueId().toString();
		
		if (e.getInventory().getName().equals(npc.getUniqueId().toString())) {
			switch (slot) {
			case 0:
				npcConfig.set(id + ".state", "FIND_ORE");
				break;
			case 1:
				npcConfig.set(id + ".state", "FIND_PLAYER");
				break;
			case 2:
				npcConfig.set(id + ".state", "PREPARE_IDLE");
				break;
			case 3:
				npcConfig.set(id + ".state", "PREPARE_DEFEND");
			}
			e.getWhoClicked().closeInventory();
		}
	}

	private Inventory npcGUI(String id) {
		Inventory inv = Bukkit.createInventory(null, 9, id);
		inv.setItem(0, Items.item(Material.WOOD_PICKAXE, 1, "&fMine", "&7Order NPC to Mine"));
		inv.setItem(1, Items.item(Material.FEATHER, 1, "&fFollow", "&7Order NPC to Follow Nearest Player"));
		inv.setItem(2, Items.item(Material.COAL, 1, "&fIdle", "&7Order NPC to go Idle"));
		inv.setItem(3, Items.item(Material.DIAMOND_SWORD, 1, "&fDefend", "&7Defends your Island from Pirates"));
		return inv;
	}

	// Called every tick
	@Override
	public void run() {
		final Configuration npcConfig = plugin.getNpcConfig();
		final String id = npc.getUniqueId().toString();
		
		if (!npc.isSpawned()) {
			return;
		}
		
		if (!npcConfig.isSet(id + ".state")) {
			return;
		}
		
		UUID pid = UUID.fromString(npcConfig.getString(id + ".owner"));
		Player p = Bukkit.getPlayer(pid);
		if (p == null) {
			npc.getNavigator().cancelNavigation();
			return;
		}
		
		switch (npcConfig.getString(id + ".state")) {
		case "FIND_ORE":
			new BukkitRunnable() {
				@Override
				public void run() {
					npc.getNavigator().cancelNavigation();
					ValkUtils utils = new ValkUtils();
					mineloc = utils.findBlock(npc, mineloc, Material.GOLD_ORE, SEARCH_RADIUS); // Find a ore to mine.
					if (mineloc != null) { // If the ore exists, walk to it.
						npcConfig.set(id + ".state", "MINING");
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, SEARCH_INTERVAL);
			
			break;
		case "MINING":
			if (mineloc != null) {
				npc.getNavigator().setTarget(mineloc);
				if (npc.getStoredLocation().distance(mineloc) < MIN_DIST) { // If we are there, start mining the ore.
					animationMining++;

					if (animationMining % 10 == 0) {
						PlayerAnimation.ARM_SWING.play((Player) npc.getEntity());
					}

					if (animationMining > 20 * 5) {
						npcConfig.set(id + ".state", "FIND_STORAGE");
						animationMining = 0;
						RespawningBlocks rb = new RespawningBlocks();
						rb.respawningBlock(mineloc.getBlock().getLocation(), null, Material.GOLD_ORE, (byte) 0, "&e+1 gold", Material.COBBLESTONE, null, 10);
					}
				}
			}
			break;
		case "FIND_STORAGE":
			new BukkitRunnable() {
				@Override
				public void run() {
					npc.getNavigator().cancelNavigation();
					ValkUtils utils = new ValkUtils();
					storageloc = utils.findBlock(npc, storageloc, Material.CHEST, SEARCH_RADIUS);
					if (storageloc != null) {
						npcConfig.set(id + ".state", "RETURN_TO_STORAGE");
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, SEARCH_INTERVAL);
			break;
		case "RETURN_TO_STORAGE":
			if (storageloc != null) {
				npc.getNavigator().setTarget(storageloc);
				if (npc.getStoredLocation().distance(storageloc) < MIN_DIST) {
					Chest chest = (Chest) storageloc.getBlock().getState();
					ValkUtils utils = new ValkUtils();
					if (utils.hasEmptySlot(chest.getInventory())) {
						chest.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
						npcConfig.set(id + ".state", "FIND_ORE");
					} else {
						npcConfig.set(id + ".state", "FIND_TOWNHALL");
					}
				}
			}
			break;
		case "FIND_TOWNHALL":
			new BukkitRunnable() {
				@Override
				public void run() {
					npc.getNavigator().cancelNavigation();
					ValkUtils utils = new ValkUtils();
					townhallloc = utils.findBlock(npc, townhallloc, Material.IRON_BLOCK, SEARCH_RADIUS);
					if (townhallloc != null) {
						npcConfig.set(id + ".state", "RETURN_TO_TOWNHALL");
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, SEARCH_INTERVAL);
			break;
		case "RETURN_TO_TOWNHALL":
			if (townhallloc != null) {
				npc.getNavigator().setTarget(townhallloc);
				if (npc.getStoredLocation().distance(townhallloc) < MIN_DIST) {
					npcConfig.set(id + ".state", "PREPARE_IDLE");
				}
			}
			break;
		case "PREPARE_IDLE":
			npc.getNavigator().cancelNavigation();
			npcConfig.set(id + ".state", "IDLE");
			break;
		case "IDLE":
		  break;
		case "FIND_PLAYER":
			new BukkitRunnable() {
				@Override
				public void run() {
					npc.getNavigator().cancelNavigation();
					ValkUtils utils = new ValkUtils();
					player = utils.findNearestPlayer(npc);
					if (player != null) {
						npcConfig.set(id + ".state", "FOLLOW_PLAYER");
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, SEARCH_INTERVAL);
			break;
		case "FOLLOW_PLAYER":
			npc.getNavigator().setTarget(player, false);
			break;
		case "PREPARE_DEFEND":
			new BukkitRunnable() {
				@Override
				public void run() {
					ValkUtils utils = new ValkUtils();
					attacker = utils.findNearestAttacker(npc);
					if (attacker != null) {
						npcConfig.set(id + ".state", "DEFEND");
					}
				}
			}.runTaskTimer(plugin, 0, SEARCH_INTERVAL);
			break;
		case "DEFEND":
			if (attacker != null) {
				npc.getNavigator().setTarget(attacker, true);
			}
			break;
		default:
			break;
		}
	}

	// Run code when your trait is attached to a NPC.
	// This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
	// This would be a good place to load configurable defaults for new NPCs.
	@Override
	public void onAttach() {
		//plugin.getServer().getLogger().info(npc.getName() + "has been assigned MyTrait!");
	}

	// Run code when the NPC is despawned. This is called before the entity actually
	// despawns so npc.getBukkitEntity() is still valid.
	@Override
	public void onDespawn() {
		
	}

	// Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be
	// null until this method is called.
	// This is called AFTER onAttach and AFTER Load when the server is started.
	@Override
	public void onSpawn() {
		Configuration npcConfig = plugin.getNpcConfig();
		String id = npc.getUniqueId().toString();
		if (npcConfig.isSet(id + ".state")) {
			if (npcConfig.getString(id + ".state").equals("MINING")) {
				npcConfig.set(id + ".state", "FIND_ORE");
			}
			if (npcConfig.getString(id + ".state").equals("FOLLOW_PLAYER")) {
				npcConfig.set(id + ".state", "FIND_PLAYER");
			}
			if (npcConfig.getString(id + ".state").equals("RETURN_TO_STORAGE")) {
				npcConfig.set(id + ".state", "FIND_STORAGE");
			}
			if (npcConfig.getString(id + ".state").equals("RETURN_TO_TOWNHALL")) {
				npcConfig.set(id + ".state", "FIND_TOWNHALL");
			}
		}
	}

	// run code when the NPC is removed. Use this to tear down any repeating tasks.
	@Override
	public void onRemove() {
		Configuration npcConfig = plugin.getNpcConfig();
		String id = npc.getUniqueId().toString();
		npcConfig.set(id, null);
	}
}
