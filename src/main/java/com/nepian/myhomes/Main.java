package com.nepian.myhomes;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.nepian.myhomes.command.HomeCommand;
import com.nepian.myhomes.listener.PlayerRespawnListener;
import com.nepian.npcore.NPCore;
import com.nepian.npcore.util.CommandUtil;
import com.nepian.npcore.util.ListenerUtil;
import com.nepian.npcore.util.Messenger;
import com.nepian.npcore.util.sqlite.SQLite;

public class Main extends JavaPlugin {
	private static Main plugin;
	private NPCore npcore;
	private Messenger messenger;
	private SQLite sqlite;
	private HomedataController homedataController;
	
	@Override
	public void onEnable() {
		checkNPCore();
		plugin = this;
		messenger = new Messenger(plugin, false);
		sqlite = npcore.getSQLite();
		homedataController = new HomedataController(plugin, sqlite);
		
		CommandUtil.registerCommand(plugin, "home", new HomeCommand(plugin));
		ListenerUtil.register(plugin, new PlayerRespawnListener(plugin));
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	public NPCore getNPCore() {
		return npcore;
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
	public HomedataController getHomedataController() {
		return homedataController;
	}
	
	public void checkNPCore() {
		if (getServer().getPluginManager().getPlugin("NPCore") == null) {
			this.getLogger().log(Level.WARNING, "前提プラグイン(NPCore)が接続されていません");
			this.getLogger().log(Level.WARNING, "プラグインを切断します");
			this.getServer().getPluginManager().disablePlugin(this);
		} else {
			npcore = NPCore.getNPCore();
		}
	}
}
