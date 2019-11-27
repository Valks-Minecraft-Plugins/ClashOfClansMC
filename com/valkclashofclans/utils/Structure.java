package com.valkclashofclans.utils;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import com.valkclashofclans.ValkClashOfClans;

public class Structure {
	ValkClashOfClans plugin = null;
	Configuration config = null;

	private String structure;
	private String path;
	private int costFactor;
	
	public Structure(Configuration config, String path, String structure, int costFactor) {
		this.plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
		this.config = config;
		this.structure = structure;
		this.path = path;
		this.costFactor = costFactor;
	}
	
	public int getCost() {
		int cost = (int) Math.pow(getTier() * costFactor, 1.1);
		return cost;
	}
	
	public int getTier() {
		checkConfig(path);
		return config.getInt(path + ".structures." + structure) + 1;
	}
	
	private void checkConfig(String path) {
		if (!config.isSet(path + ".structures." + structure)) {
			config.set(path + ".structures." + structure, 0);
		}
	}
}
