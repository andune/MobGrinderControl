/**
 * 
 */
package org.morganm.mobgrindercontrol.command;

import org.bukkit.command.CommandSender;
import org.morganm.mobgrindercontrol.PluginInterface;
import org.morganm.mobgrindercontrol.config.ConfigOptions;


/** Abstract class that takes care of some routine tasks for commands, to keep those
 * objects as light as possible and so as to not violate the DRY principle.
 * 
 * @author morganm
 *
 */
public abstract class BaseCommand implements Command {

	protected PluginInterface plugin;
	private boolean enabled;
	private String permissionNode;
	private String commandName;

	/** Returns this object for easy initialization in a command hash.
	 * 
	 * @param plugin
	 * @return
	 */
	public Command setPlugin(PluginInterface plugin) {
		this.plugin = plugin;
		enabled = !plugin.getConfig().getBoolean(getDisabledConfigFlag(), Boolean.FALSE);
		return this;
	}
	
	/** Return true if the command is enabled, false if it is not.
	 * 
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	protected String getDisabledConfigFlag() {
		return ConfigOptions.COMMAND_TOGGLE_BASE + getCommandName();
	}
	
	/** Convenience method to run common command checks:
	 * 
	 *   1 - is the command enabled in the config?
	 *   2 - does the player have access to run the command?
	 *   
	 * @param p the player object that is running the command
	 * 
	 * @return returns false if the checks fail and Command processing should stop, true if the command is allowed to continue
	 */
	protected boolean defaultCommandChecks(CommandSender sender) {
		if( !enabled )
			return false;
		
		if(!hasPermission(sender))
			return false;

		return true;
	}
	
	/** Can be overridden, but default implementation just applies the command name
	 * as the lower case version of the class name of the implemented command.
	 */
	@Override
	public String getCommandName() {
		if( commandName == null ) {
			String className = this.getClass().getName();
			int index = className.lastIndexOf('.');
			commandName = className.substring(index+1).toLowerCase();
		}
		
		return commandName;
	}
	
	/** Here for convenience if the command has no aliases.  Otherwise, this should be overridden.
	 * 
	 */
	@Override
	public String[] getCommandAliases() {
		return null;
	}
	
	/** Return true if the player has permission to run this command.  If they
	 * don't have permission, print them a message saying so and return false.
	 * 
	 * @param p
	 * @return
	 */
	protected boolean hasPermission(CommandSender sender) {
		if( permissionNode == null )
			permissionNode = plugin.getBasePermissionString() + "command." + getCommandName() + ".use";
		
		if( !plugin.hasPermission(sender, permissionNode) ) {
			sender.sendMessage("You don't have permission to do that.");
			return false;
		}
		else
			return true;
	}
}
