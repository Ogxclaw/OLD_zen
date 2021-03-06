package com.kirik.zen.teleport.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.PermissionDeniedException;
import com.kirik.zen.main.ZenCommandException;

@Names("banish")
@Help("Forces a player to teleport to spawn")
@Usage("/banish <name>")
@Permission("zen.teleport.banish")
public class BanishCommand extends ICommand {

	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		//TODO silent flag
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		Player player = (Player)commandSender;
		Player target = playerHelper.matchPlayerSingle(args[0]);
		
		if(playerHelper.getPlayerLevel(player) <= playerHelper.getPlayerLevel(target))
			throw new PermissionDeniedException();
		
		Location spawnLocation = plugin.getServer().getWorld("world").getSpawnLocation();
		if(target.getLocation().getWorld() != plugin.getServer().getWorld("world")){
			target.getLocation().setWorld(plugin.getServer().getWorld("world"));
		}
		target.teleport(spawnLocation);
		playerHelper.sendServerMessage(commandSender.getName() + " has banished " + target.getName() + "!");
	}
}
