package com.nepian.myhomes.command.sub;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import com.nepian.myhomes.Myhomes;
import com.nepian.npcore.util.command.MainCommand;
import com.nepian.npcore.util.command.SubCommand;
import com.nepian.npcore.util.command.SubCommandType;

public class HelpCommand extends SubCommand {
	private MainCommand main;
	private Myhomes plugin;
	
	public HelpCommand(Myhomes plugin, MainCommand main) {
		super("help");
		this.main = main;
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
			throws CommandException {

		plugin.getMessenger().send(sender, "[ホームコマンドリスト]");
		
		this.sendUsage(sender, label, main);
		
		for (SubCommand sub : main.getSubCommands()) {
			this.sendUsage(sender, label, sub);
		}
	}

	@Override
	public String getPossibleArguments() {
		return null;
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.HIDDEN;
	}

	private void sendUsage(CommandSender sender, String label, SubCommand cmd) {
		if (!(cmd.getType() == SubCommandType.GENERIC)) {
			return;
		}
		
		StringBuilder msg = new StringBuilder("");
		msg.append("&6/" + label);
		
		if (!(cmd instanceof MainCommand)) {
			msg.append(" " + cmd.getName());
		}
		
		if (cmd.getPossibleArguments() != null) {
			msg.append(" ").append(cmd.getPossibleArguments());
		}
		
		if (cmd.getDescription() != null) {
			msg.append("&r : ").append(cmd.getDescription());
		}
		
		plugin.getMessenger().sendNoPre(sender, msg.toString());
	}
}
