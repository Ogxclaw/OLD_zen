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
import com.kirik.zen.main.PermissionDeniedException;
import com.kirik.zen.main.ZenCommandException;

@Names({"summon", "tphere"})
@Help("Teleports a player to your current location")
@Usage("/summon <name>")
@Permission("zen.teleport.summon")
public class SummonCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		//TODO Add silent summon
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		final Player target = playerHelper.matchPlayerSingle(args[0]);
		final Player player = (Player)commandSender;
		PlayerConfiguration playerConfig = new PlayerConfiguration(target.getUniqueId());
		Location prevLoc = target.getLocation();
		prevLoc.setYaw(target.getLocation().getYaw());
		prevLoc.setPitch(target.getLocation().getPitch());
		
		if(playerHelper.getPlayerLevel(player) <= playerHelper.getPlayerLevel(target))
			throw new PermissionDeniedException();
		
		if(commandSender.hasPermission("zen.teleport.tp.override")){
			playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
			playerConfig.savePlayerConfig();
			target.teleport(player.getLocation());
			playerHelper.sendDirectedMessage(commandSender, "Summoned " + target.getName());
			return;
		}
		playerHelper.sendDirectedMessage(commandSender, "Please wait 5 seconds for teleportation.");
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run(){
				playerConfig.getPlayerConfig().set("previousLocation", prevLoc);
				playerConfig.savePlayerConfig();
				target.teleport(player.getLocation());
				playerHelper.sendDirectedMessage(player, "Summoned " + target.getName());
				playerHelper.sendDirectedMessage(target, player.getName() + " has summoned you");
			}
		}, 100L);
		/*Player target = playerHelper.matchPlayerSingle(args[0]);
		
		Player player = (Player)commandSender;
		target.teleport(player.getLocation());
		playerHelper.sendDirectedMessage(commandSender, "Summoned " + target.getName());
		playerHelper.sendDirectedMessage(target, commandSender.getName() + " has summoned you");*/
	}

}
