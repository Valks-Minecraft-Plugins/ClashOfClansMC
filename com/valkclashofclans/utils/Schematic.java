package com.valkclashofclans.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.valkclashofclans.ValkClashOfClans;

public class Schematic {

	String path;
	Configuration config;
	final Player p;
	String fileName;
	boolean pasteSlow;

	ValkClashOfClans plugin = null;
	
	NBTStorageFile schematic = null;
	
	public Schematic(String path, Configuration config, final Player p, String fileName,
			boolean pasteSlow) {
		this.plugin = JavaPlugin.getPlugin(ValkClashOfClans.class);
		this.path = path;
		this.config = config;
		this.p = p;
		this.fileName = fileName;
		this.pasteSlow = pasteSlow;
		
		schematic = new NBTStorageFile(plugin.getDataFolder().toString() + "\\schematics\\", fileName);
	}

	public void pasteSchematic() {
		final ArrayList<Location> list = new ArrayList<Location>();
		
		final byte[] blocks = schematic.getByteArray("Blocks");
		final byte[] data = schematic.getByteArray("Data");
		
		short height = schematic.getShort("Height");
		short width = schematic.getShort("Width");
		short length = schematic.getShort("Length");

		final World w = Bukkit.getWorld(config.getString(path + ".world"));
		Location configLoc = new Location(w, config.getInt(path + ".x"), config.getInt(path + ".y"),
				config.getInt(path + ".z"));
		Location loc = configLoc.add(getOffsetX(), getOffsetY(), getOffsetZ());
		for (int y = 0; y < height; y++) {
			for (int z = 0; z < length; z++) {
				for (int x = 0; x < width; x++) {
					Location newLoc = loc.clone();
					newLoc.add(x, y, z);
					list.add(newLoc);
				}
			}
		}

		if (pasteSlow) {
			pasteSlow(list, blocks, data, w);
		} else {
			pasteFast(list, blocks, data, w, false);
		}
	}

	@SuppressWarnings("deprecation")
	private void pasteFast(final ArrayList<Location> list, final byte[] blocks, final byte[] data, final World w,
			boolean ignoreAir) {
		for (int i = 0; i < list.size(); i++) {
			Block b = w.getBlockAt(list.get(i));
			if (ignoreAir) {
				if (blocks[i] != 0) {
					if (blocks[i] < 0) {
						b.setTypeId(blocks[i] + 256);
						b.setData(data[i]);
					} else {
						b.setTypeId(blocks[i]);
						b.setData(data[i]);
					}
				}
			} else {
				if (blocks[i] < 0) {
					b.setTypeId(blocks[i] + 256);
					b.setData(data[i]);
				} else {
					b.setTypeId(blocks[i]);
					b.setData(data[i]);
				}
			}

		}
	}

	private void pasteSlow(final ArrayList<Location> list, final byte[] blocks, final byte[] data, final World w) {
		new BukkitRunnable() {
			int counter = 0;
	
			@SuppressWarnings("deprecation")
			public void run() {
				if (counter >= list.size()) {
					cancel();
				} else {
					Block b = w.getBlockAt(list.get(counter));
					if (blocks[counter] < 0) {
						b.setTypeId(blocks[counter] + 256);
						b.setData(data[counter]);
					} else {
						b.setTypeId(blocks[counter]);
						b.setData(data[counter]);
					}
					counter++;
				}
			}
		}.runTaskTimer(plugin, 6, 1);
	}
	
	public short getWidth() {
		return schematic.getShort("Width");
	}
	
	public short getHeight() {
		return schematic.getShort("Height");
	}
	
	public short getLength() {
		return schematic.getShort("Length");
	}
	
	public short getOffsetX() {
		return schematic.getShort("WEOffsetX");
	}
	
	public short getOffsetY() {
		return schematic.getShort("WEOffsetY");
	}
	
	public short getOffsetZ() {
		return schematic.getShort("WEOffsetZ");
	}
}
