package com.nepian.myhomes.command.sub;

import java.sql.SQLException;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nepian.myhomes.HomedataController;
import com.nepian.myhomes.Main;
import com.nepian.myhomes.Properties;
import com.nepian.npcore.util.Messenger;
import com.nepian.npcore.util.command.CommandSenderType;
import com.nepian.npcore.util.command.SubCommand;
import com.nepian.npcore.util.command.SubCommandType;

public class SetCommand extends SubCommand {
	private Main plugin;
	
	public SetCommand(Main plugin) {
		super("set");
		super.addCommandSenderType(CommandSenderType.PLAYER);
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args)
			throws CommandException {

		Player player = (Player) sender;
		Messenger mes = plugin.getMessenger();
		HomedataController hc = plugin.getHomedataController();
		String name = (args.length == 0) ? Properties.DEFAULT_HOME_NAME : args[0];
		
		if (hc.has(player, name)) {
			try {
				hc.updateHome(player, name);
				mes.sendSuccess(sender, "ホーム(&6" + name + "&r)を更新しました");
			} catch (SQLException e) {
				e.printStackTrace();
				mes.sendFailed(sender, "ホーム(&6" + name + "&r)の更新に失敗しました");
			}
			return;
		} else {
			try {
				hc.addHome(player, name);
				mes.sendSuccess(sender, "ホーム(&6" + name + "&r)を追加しました");
			} catch (SQLException e) {
				e.printStackTrace();
				mes.sendFailed(sender, "ホーム(&6" + name + "&r)の追加に失敗しました");
			}
			return;
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
		return "ホームを設定する";
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
