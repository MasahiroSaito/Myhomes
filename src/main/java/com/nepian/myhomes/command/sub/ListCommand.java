package com.nepian.myhomes.command.sub;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.nepian.myhomes.HomedataController;
import com.nepian.myhomes.Myhomes;
import com.nepian.npcore.util.Messenger;
import com.nepian.npcore.util.PlayerUtil;
import com.nepian.npcore.util.command.CommandSenderType;
import com.nepian.npcore.util.command.SubCommand;
import com.nepian.npcore.util.command.SubCommandType;

public class ListCommand extends SubCommand {
	private Myhomes plugin;
	
	public ListCommand(Myhomes plugin) {
		super("list");
		super.addCommandSenderType(CommandSenderType.PLAYER);
		super.addCommandSenderType(CommandSenderType.CONSOLE);
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
			throws CommandException {
		
		Messenger mes = plugin.getMessenger();
		HomedataController hc = plugin.getHomedataController();

		if (sender instanceof Player) {
			Player player = (Player) sender;
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
			
		} else if (sender instanceof ConsoleCommandSender) {
			
			if (hc.getHomedatas().isEmpty()) {
				return;
			}
			
			for (UUID uuid : hc.getHomedatas().keySet()) {
				String playerName = PlayerUtil.getName(uuid);
				List<String> homes = hc.getHomeNames(uuid);
				
				mes.sendNoPre(sender, "&6" + playerName + "&r: " + homes);
			}
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
		return "ホームの一覧を表示する";
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
