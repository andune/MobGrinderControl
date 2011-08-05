/**
 * 
 */
package org.morganm.mobgrindercontrol.commands;

import org.bukkit.command.CommandSender;
import org.morganm.mobgrindercontrol.Grinder;
import org.morganm.mobgrindercontrol.GrinderManager;
import org.morganm.mobgrindercontrol.MobGrinderControlPlugin;
import org.morganm.mobgrindercontrol.command.BaseCommand;
import org.morganm.mobgrindercontrol.command.Command;

/**
 * @author morganm
 *
 */
public class Mgc extends BaseCommand implements Command {

	@Override
	public boolean execute(CommandSender sender, org.bukkit.command.Command command, String[] args) {
		if( !isEnabled() )
			return false;
		
		if( args.length < 1 ) {
			printUsage(sender);
		}
		else {
			if( args[0].equals("reload") ) {
				// TODO: code reload command
			}
			else if( args[0].equals("showgrinders") ) {
				sender.sendMessage("Active grinders:");
				MobGrinderControlPlugin mgcPlugin = (MobGrinderControlPlugin) plugin;
				GrinderManager gm = mgcPlugin.getGrinderManager();
				for(Grinder grinder : gm.getGrinders()) {
					sender.sendMessage("  grinder at "+grinder.getDeathLocation()+", killCount = "+grinder.getDeathCount());
				}
			}
		}
		
		return true;
	}
	
	private void printUsage(CommandSender sender) {
		sender.sendMessage("Usage:");
		sender.sendMessage("  mgc reload - reload config");
		sender.sendMessage("  mgc showgrinders - show location of running grinders");
	}

}
