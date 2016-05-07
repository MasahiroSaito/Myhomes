package com.nepian.myhomes.command.sub;

import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nepian.myhomes.HomedataController;
import com.nepian.myhomes.Main;
import com.nepian.npcore.util.Messenger;
import com.nepian.npcore.util.command.CommandSenderType;
import com.nepian.npcore.util.command.SubCommand;
import com.nepian.npcore.util.command.SubCommandType;

public class ListCommand extends SubCommand {
	private Main plugin;
	
	public ListCommand(Main plugin) {
		super("list");
		super.addCommandSenderType(CommandSenderType.PLAYER);
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
			throws CommandException {

		Player player = (Player) sender;
		Messenger mes = plugin.getMessenger();
		HomedataController hc = plugin.getHomedataController();
		StringBuilder msg = new StringBuilder("Homes: ");
		List<String> names = hc.getHomeNames(player);
		
		if (names.isEmpty()) {
			msg.append("ホームはまだ登録されていません");
		} else {
			for (String name : hc.getHomeNames(player)) {
				msg.append(name + ", ");
			}
			msg.delete(msg.lastIndexOf(", "), msg.length());
		}
		
		mes.send(sender, msg.toString());
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
		return "ホームの一覧を表示する";
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
