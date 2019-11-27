package com.valkclashofclans.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.valkclashofclans.ValkClashOfClans;

/**
 * Creates a hologram of visible by default.
 * @author valkyrienyanko
 *
 */
public class Hologram {
	ArmorStand as;
	Location location;
	ValkClashOfClans plugin = null;
	public Hologram(Location loc, String name) {
		plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
		this.location = loc;
		as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		as.setArms(false);
		as.setGravity(true);
		as.setVisible(false);
		as.setCustomName(name);
		as.setCustomNameVisible(true);
	}
	
	public Hologram setVisible(boolean visible) {
		as.setCustomNameVisible(visible);
		return this;
	}
	
	public Hologram move() {
		final Vector to = new Vector(0, 1, 0).multiply(0.01);
		new BukkitRunnable() {
			@Override
			public void run() {
				as.setVelocity(to);
			}
		}.runTaskTimer(plugin, 0, 1);
		return this;
	}
	
	public void destroy() {
		as.remove();
	}
}