package com.kirik.zen.teleport.commands;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.main.ZenCommandException;

@Names("wild")
@Help("Teleports user to random \nlocation in the wild")
@Usage("/wild")
@Permission("zen.teleport.wild")
public class WildCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		
		Player player = (Player)commandSender;
		//playerHelper.hasBlockAirAboveHead(player);
		//playerHelper.hasBlockBelow(player);
		Location newLoc = randomLocation(player);
		while(player.getWorld().getBlockAt(newLoc.getBlockX(), newLoc.getBlockY() - 1, newLoc.getBlockZ()).isLiquid() || 
				player.getWorld().getBlockAt(newLoc.getBlockX(), newLoc.getBlockY() - 2, newLoc.getBlockZ()).isLiquid() ||
				player.getWorld().getBlockAt(newLoc.getBlockX(), newLoc.getBlockY() - 3, newLoc.getBlockZ()).isLiquid() || 
				!player.getWorld().getBlockAt(newLoc.getBlockX(), newLoc.getBlockY(), newLoc.getBlockZ()).isEmpty() ||
				!player.getWorld().getBlockAt(newLoc.getBlockX(), newLoc.getBlockY() + 1, newLoc.getBlockZ()).isEmpty() ||
				!player.getWorld().getBlockAt(newLoc.getBlockX(), newLoc.getBlockY() + 2, newLoc.getBlockZ()).isEmpty() || 
				!player.getWorld().getBlockAt(newLoc.getBlockX(), newLoc.getBlockY() + 3, newLoc.getBlockZ()).isEmpty()){
			newLoc = randomLocation(player);
		}
		while(randomLocation(player).getZ() > player.getWorld().getWorldBorder().getSize()){
			newLoc = randomLocation(player);
		}
		player.teleport(newLoc);
		playerHelper.sendDirectedMessage(player, "Teleported to a random location!");
	}
	
	private Location randomLocation(Player player){
		double borderSize = plugin.getServer().getWorld("world").getWorldBorder().getSize();
		Random randXCoord = new Random();
		Random randZCoord = new Random();
		int xCoord = randXCoord.nextInt((int)borderSize) + 1;
		int zCoord = randZCoord.nextInt((int)borderSize) + 1;
		if(xCoord > (borderSize / 2)){
			xCoord -= borderSize;
		}
		if(zCoord > (borderSize / 2)){
			zCoord -= borderSize;
		}
		int yCoord = playerHelper.getSolidBlock(xCoord, zCoord, player);
		return new Location(player.getWorld(), xCoord, yCoord, zCoord);
	}

}
