package com.nepian.myhomes.command;

import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nepian.myhomes.HomedataController;
import com.nepian.myhomes.Myhomes;
import com.nepian.myhomes.Properties;
import com.nepian.myhomes.command.sub.HelpCommand;
import com.nepian.myhomes.command.sub.ListCommand;
import com.nepian.myhomes.command.sub.RemoveCommand;
import com.nepian.myhomes.command.sub.SetCommand;
import com.nepian.npcore.util.command.CommandSenderType;
import com.nepian.npcore.util.command.MainCommand;
import com.nepian.npcore.util.command.SubCommandType;

public class HomeCommand extends MainCommand {
	private Myhomes plugin;
	
	public HomeCommand(Myhomes plugin) {
		super(plugin.getMessenger(), "home");
		super.addCommandSenderType(CommandSenderType.PLAYER);
		this.plugin = plugin;
		this.registerSubCommand(new SetCommand(plugin));
		this.registerSubCommand(new RemoveCommand(plugin));
		this.registerSubCommand(new ListCommand(plugin));
		this.registerSubCommand(new HelpCommand(plugin, this));
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
			throws CommandException {

		Player player = (Player) sender;
		HomedataController hc = plugin.getHomedataController();
		String name = (args.length == 0) ? Properties.DEFAULT_HOME_NAME : args[0];
		Location location = hc.getHome(player, name);
		
		if (location == null) {
			plugin.getMessenger().sendFailed(sender, "ホーム(&6" + name + "&r)は設定されていません");
			return;
		}
		
		player.teleport(location);
	}

	@Override
	public String getPossibleArguments() {
		return "(<ホーム名>)";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public String getDescription() {
		return "設定したホームへ移動する";
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
