package com.nepian.myhomes.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.nepian.myhomes.HomedataController;
import com.nepian.myhomes.Myhomes;
import com.nepian.myhomes.Properties;

public class PlayerRespawnListener implements Listener {
	private static Myhomes plugin;
	
	public PlayerRespawnListener(Myhomes plugin) {
		PlayerRespawnListener.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		HomedataController hc = plugin.getHomedataController();
		
		if (hc.has(player, Properties.DEFAULT_HOME_NAME)) {
			event.setRespawnLocation(hc.getHome(player, Properties.DEFAULT_HOME_NAME));
		}
	}
}
