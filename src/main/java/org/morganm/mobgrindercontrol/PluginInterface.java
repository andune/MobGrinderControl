/**
 * 
 */
package org.morganm.mobgrindercontrol;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.morganm.mobgrindercontrol.config.Config;

/** General plugin interface so I can re-use classes that depend on certain functionality without
 * tying them specifically to a single plugin class.
 * 
 * @author morganm
 *
 */
public interface PluginInterface {
	/** Used to get the Bukkit JavaPlugin object, so all objects which take a reference to
	 * this interface have the full power of the Bukkit JavaPlugin methods available to them. 
	 * 
	 * @return
	 */
	public JavaPlugin getPlugin();
	
	/** Return the Logger object the plugin is using, which allows it to be re-used through
	 * all the plugin classes.
	 * 
	 * @return
	 */
	public Logger getLogger();
	
	/** Return the logPrefix so all supporting classes can log with a consistent prefix.
	 * 
	 * @return
	 */
	public String getLogPrefix();
	
	/** Get the folder where the plugin keeps it's data, including any config files.
	 * 
	 * @return
	 */
	public File getDataFolder();
	
	/** Return the File the is the plugin Jar.  This is used to find the default config.yml.
	 * 
	 * @return
	 */
	public File getJarFile();
	
	/** Return the ClassLoader object in use for this plugin.
	 * 
	 * @return
	 */
	public ClassLoader getClassLoader();
	
	/** Return the plugin's Config object.
	 * 
	 * @return
	 */
	public Config getConfig();
	
	/** Return the base Permission string, for example "mgp." - all permissions checks will be
	 * based off of this string.
	 * 
	 * @return
	 */
	public String getBasePermissionString();
	
	/** Check to see if a given player has access to a given permission node, using whatever
	 * Permission system is active.
	 * 
	 * @return
	 */
	public boolean hasPermission(CommandSender p, String node);
	
	/** Turn debugging on or off.
	 * 
	 * @param debug
	 */
	public void setDebug(boolean debug);
	
	/**
	 * @return true if debugging is enabled, false if not
	 */
	public boolean isDebug();
}
