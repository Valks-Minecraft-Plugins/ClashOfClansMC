package com.valkclashofclans.mobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class BlockSpawns implements Listener {
	@EventHandler
	public void blockSpawns(CreatureSpawnEvent e) {
		switch (e.getSpawnReason()) {
		case NATURAL:
		case CHUNK_GEN:
		case JOCKEY:
		case NETHER_PORTAL:
		case LIGHTNING:
			e.setCancelled(true);
		break;
		default:
			break;
		}
	}
}