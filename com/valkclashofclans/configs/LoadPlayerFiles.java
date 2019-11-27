package com.valkclashofclans.configs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoadPlayerFiles implements Listener {
	@EventHandler
	public void loadFiles(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		PlayerFiles cm = PlayerFiles.getConfig(p.getUniqueId());
		if (!p.hasPlayedBefore()) {
			FileConfiguration f = cm.getConfig();
			f.set("name", p.getName());
			f.set("uuid", p.getUniqueId().toString());
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
			f.set("join_date", format.format(now));
			f.set("last_join", format.format(now));
			f.set("trail", "NONE");
			f.set("nick", "NONE");
			f.set("color", "&7");
			f.set("rank", "Default");
			f.set("muted", false);
			f.set("gold", 0);
			f.set("class", "NONE");
			cm.saveConfig();
		} else {
			FileConfiguration f = cm.getConfig();
			f.set("name", p.getName());
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
			f.set("last_join", format.format(now));
			if (!f.isSet("trail")) {
				f.set("trail", "NONE");
			}
			if (!f.isSet("nick")) {
				f.set("nick", "NONE");
			}
			if (!f.isSet("rank")) {
				f.set("rank", "Default");
			}
			if (!f.isSet("color")) {
				f.set("color", "&7");
			}
			if (!f.isSet("muted")) {
				f.set("muted", false);
			}
			if (!f.isSet("gold")) {
				f.set("gold", 0);
			}
			if (!f.isSet("class")) {
				f.set("class", "NONE");
			}
			cm.saveConfig();
			
			if (!f.get("nick").equals("NONE")) {
				p.setDisplayName(f.getString("nick"));
			}
			
			p.setWhitelisted(true);
		}
	}
}