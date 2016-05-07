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

public class RemoveCommand extends SubCommand {
	private Main plugin;
	
	public RemoveCommand(Main plugin) {
		super("remove");
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
