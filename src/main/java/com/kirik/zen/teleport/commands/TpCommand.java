package com.kirik.zen.teleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.main.ZenCommandException;

@Names({"tp", "teleport", "tele"})
@Help("Teleports you to another player")
@Usage("/tp <name>")
@Permission("zen.teleport.tp")
public class TpCommand extends ICommand {

	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		//TODO Add silent tp, add coord tp
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		final Player target = playerHelper.matchPlayerSingle(args[0]);
		final Location targetLocation = target.getLocation();
		final Player player = (Player)commandSender;
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		Location prevLoc = player.getLocation();
		prevLoc.setYaw(player.getLocation().getYaw());
		prevLoc.setPitch(player.getLocation().getPitch());
		
		/*if(playerHelper.getPlayerLevel(player) <= playerHelper.getPlayerLevel(target))
			throw new PermissionDeniedException();*/
	
		if(commandSender.hasPermission("zen.teleport.tp.override")){
			playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
			playerConfig.savePlayerConfig();
			player.teleport(targetLocation);
			playerHelper.sendDirectedMessage(commandSender, "Teleported to " + target.getName());
			return;
		}
		playerHelper.sendDirectedMessage(commandSender, "Please wait 5 seconds for teleportation.");
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run(){
				playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
				playerConfig.savePlayerConfig();
				player.teleport(targetLocation);
				playerHelper.sendDirectedMessage(commandSender, "Teleported to " + target.getName());
			}
		}, 100L);
	}
}
