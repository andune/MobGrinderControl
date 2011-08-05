/**
 * 
 */
package org.morganm.mobgrindercontrol;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.morganm.mobgrindercontrol.config.ConfigOptions;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/** Class to wrap various Permission systems so that we can use whatever permission system
 * is being used on the server and abstract the rest of the plugin from caring.
 * 
 * @author morganm
 *
 */
public class PermissionWrapper {
	private final Logger log;
	private final String logPrefix;
	private PluginInterface plugin;
	
    private PermissionHandler permissionHandler;
	
	public PermissionWrapper(PluginInterface plugin) {
		this.plugin = plugin;
		this.log = plugin.getLogger();
		this.logPrefix = plugin.getLogPrefix();
	}
	
	/** Initialize the permission system.  This must be called before any permissions checks are
	 * done.  Best place to call this is from the plugin's onEnable() method.
	 * 
	 */
	public void init() {
        Plugin permissionsPlugin = plugin.getPlugin().getServer().getPluginManager().getPlugin("Permissions");
        if( permissionsPlugin != null ) {
        	permissionHandler = ((Permissions) permissionsPlugin).getHandler();
        	log.info(logPrefix + " Found Permissions-compatible plugin, using Permissions security");

        	/*
        	if( permissionsPlugin.getDescription().getVersion().startsWith("3") )
        		usePerm3 = true;
        		*/
        }
        else
	    	log.warning(logPrefix+" Permissions system not enabled, using isOP instead.");
	}
	
	public boolean hasPermission(CommandSender sender, String permissionNode) {
		// console always has permission
		if( sender instanceof ConsoleCommandSender )
			return true;
		
		Player p = null;
		if( p instanceof Player )
			p = (Player) sender;
		
		// shouldn't ever happen, but if it does, something is wrong, log and don't allow.
		if( p == null ) {
			log.warning(logPrefix + " hasPermission() unexpected non-Player/non-Console object received: "+sender);
			return false;
		}
		
    	if( permissionHandler != null ) 
    		return permissionHandler.has(p, permissionNode);
    	else {
    		if( p.isOp() )
    			return true;
    		
    		List<String> defaultPerms = plugin.getConfig().getStringList(ConfigOptions.DEFAULT_PERMISSIONS, null);
    		if( defaultPerms.contains(permissionNode) )
    			return true;
    		else
    			return false;
    	}
	}
}
