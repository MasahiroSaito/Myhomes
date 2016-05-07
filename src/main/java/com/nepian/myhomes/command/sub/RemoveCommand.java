package com.nepian.myhomes.command.sub;

import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.nepian.myhomes.HomedataController;
import com.nepian.myhomes.Myhomes;
import com.nepian.myhomes.Properties;
import com.nepian.npcore.util.Messenger;
import com.nepian.npcore.util.PlayerUtil;
import com.nepian.npcore.util.command.CommandSenderType;
import com.nepian.npcore.util.command.SubCommand;
import com.nepian.npcore.util.command.SubCommandType;

public class RemoveCommand extends SubCommand {
	private Myhomes plugin;
	
	public RemoveCommand(Myhomes plugin) {
		super("remove");
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
			String name = (args.length == 0) ? Properties.DEFAULT_HOME_NAME : args[0];

			if (!hc.has(player, name)) {
				mes.sendFailed(sender, "ホーム(&6" + name + "&r)は登録されていません");
				return;
			}

			try {
				hc.removeHome(player, name);
				mes.sendSuccess(sender, "ホーム(&6" + name + "&r)を削除しました");
			} catch (SQLException e) {
				e.printStackTrace();
				mes.sendFailed(sender, "ホーム(&6" + name + "&r)の削除に失敗しました");
			}
		} else if (sender instanceof ConsoleCommandSender) {
			
			if (args.length != 2) {
				mes.sendFailed(sender, "引数が不正です(" + label + " remove <player_name> <home_name>)");
				return;
			}
			
			String playerName = args[0];
			String homeName = args[1];
			OfflinePlayer player = PlayerUtil.getOfflinePlayer(playerName);
			
			if (player == null) {
				mes.sendFailed(sender, "プレイヤー(&6" + playerName + "&r)は存在しません");
				return;
			}
			
			if (!hc.has(player, homeName)) {
				mes.sendFailed(sender, "ホーム(&6" + homeName + "&r)は存在しません");
				return;
			}
			
			try {
				hc.removeHome(player, homeName);
				mes.sendSuccess(sender, "ホーム(&6" + homeName + "&r)を削除しました");
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
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
		return "ホームを削除する";
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
