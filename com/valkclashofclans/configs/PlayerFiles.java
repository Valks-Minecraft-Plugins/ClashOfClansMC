package com.valkclashofclans.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerFiles {

	private final UUID u;
	private FileConfiguration fc;
	private File file;
	private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(this.getClass());
	private static List<PlayerFiles> configs = new ArrayList<PlayerFiles>();

	private PlayerFiles(Player p) {
		this.u = p.getUniqueId();

		configs.add(this);
	}

	private PlayerFiles(UUID u) {
		this.u = u;

		configs.add(this);
	}

	private PlayerFiles(OfflinePlayer p) {
		this.u = p.getUniqueId();
	}

	public Player getOwner() {
		if (u == null)
			try {
				throw new Exception();
			} catch (Exception e) {
				getInstance().getLogger().warning("ERR... Player is Null!");
				e.printStackTrace();
			}
		return Bukkit.getPlayer(u);
	}

	public UUID getOwnerUUID() {
		if (u == null)
			try {
				throw new Exception();
			} catch (Exception e) {
				getInstance().getLogger().warning("ERR... Player is Null!");
				e.printStackTrace();
			}
		return u;
	}

	public JavaPlugin getInstance() {
		if (plugin == null)
			try {
				throw new Exception();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return plugin;
	}

	public static PlayerFiles getConfig(Player p) {
		for (PlayerFiles c : configs) {
			if (c.getOwnerUUID().equals(p.getUniqueId())) {
				return c;
			}
		}
		return new PlayerFiles(p);
	}

	public static PlayerFiles getConfig(OfflinePlayer p) {
		for (PlayerFiles c : configs) {
			if (c.getOwnerUUID().equals(p.getUniqueId())) {
				return c;
			}
		}
		return new PlayerFiles(p);
	}

	public static PlayerFiles getConfig(UUID u) {
		for (PlayerFiles c : configs) {
			if (c.getOwnerUUID().equals(u)) {
				return c;
			}
		}
		return new PlayerFiles(u);
	}

	public boolean delete() {
		return getFile().delete();
	}

	public boolean exists() {
		if (fc == null || file == null) {
			File temp = new File(getDataFolder(), "players/" + getOwner() + ".yml");
			if (!temp.exists()) {
				return false;
			} else {
				file = temp;
			}
		}
		return true;
	}

	public File getDataFolder() {
		File dir = new File(
				PlayerFiles.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
		File d = new File(dir.getParentFile().getPath(), getInstance().getName());
		if (!d.exists()) {
			d.mkdirs();
		}
		return d;
	}

	public File getFile() {
		if (file == null) {
			file = new File(getDataFolder(), "players/" + getOwnerUUID() + ".yml");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public FileConfiguration getConfig() {
		if (fc == null) {
			fc = YamlConfiguration.loadConfiguration(getFile());
		}
		return fc;
	}

	public void reload() {
		if (file == null) {
			file = new File(getDataFolder(), "players/" + getOwner().getUniqueId().toString() + ".yml");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fc = YamlConfiguration.loadConfiguration(file);
		}
	}

	public void resetConfig() {
		delete();
		getConfig();
	}

	public void saveConfig() {
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}