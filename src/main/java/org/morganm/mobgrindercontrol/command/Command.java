/**
 * 
 */
package org.morganm.mobgrindercontrol.command;

import org.bukkit.command.CommandSender;
import org.morganm.mobgrindercontrol.PluginInterface;


/**
 * @author morganm
 *
 */
public interface Command {
	public boolean execute(CommandSender sender, org.bukkit.command.Command command, String[] args);

	/** Return the name of the command.  Used in cooldown and permission checks as well as
	 * for matching the command the player types in.
	 * 
	 * @return
	 */
	public String getCommandName();
	
	/** Return any aliases for this command.  Can be null.
	 * 
	 * @return
	 */
	public String[] getCommandAliases();

	/** Commands can be disabled by configuration, this method allows them to declare
	 * themselves enabled or disabled.
	 * 
	 * @return
	 */
	public boolean isEnabled();
	
	public Command setPlugin(PluginInterface plugin);
}
