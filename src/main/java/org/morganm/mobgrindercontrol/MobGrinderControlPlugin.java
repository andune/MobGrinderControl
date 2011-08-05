/**
 * 
 */
package org.morganm.mobgrindercontrol;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;
import org.morganm.mobgrindercontrol.command.CommandProcessor;
import org.morganm.mobgrindercontrol.config.Config;
import org.morganm.mobgrindercontrol.config.ConfigException;
import org.morganm.mobgrindercontrol.config.ConfigFactory;

/**
 * @author morganm
 *
 */
public class MobGrinderControlPlugin extends JavaPlugin implements PluginInterface {
    public static final Logger log = Logger.getLogger("MobGrinderControl");;
	private static final Level oldLogLevel = log.getLevel();
    public static String logPrefix;
	
	private PermissionWrapper permWrapper;
	private Config config;
	private boolean debug = false;
	
	private GrinderManager grinderManager;
	private EntityListener entityListener;
	private EntityTracker tracker;
	private GrinderFinder finder;
    private CommandProcessor cmdProcessor;
	
	@Override
	public void onEnable() {
		boolean loadError = false;
		
		permWrapper = new PermissionWrapper(this);
		permWrapper.init();
		
    	// load our configuration
    	try {
    		loadConfig();
    	}
    	catch(Exception e) {
    		loadError = true;
    		log.severe("Error loading plugin: "+getDescription().getName());
    		e.printStackTrace();
    	}
    	
    	if( loadError ) {
    		log.severe("Error detected when loading plugin "+ getDescription().getName() +", plugin shutting down.");
    		getServer().getPluginManager().disablePlugin(this);
    		return;
    	}
		
		grinderManager = new GrinderManager();
		tracker = new EntityTracker();
		
		entityListener = new EntityListener(this, tracker);
		getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, entityListener, Priority.Highest, this);

    	cmdProcessor = new CommandProcessor(this);
    	
    	finder = new GrinderFinder(this, tracker, grinderManager);
    	getServer().getScheduler().scheduleAsyncRepeatingTask(this, finder, 200, 200);
    	
        log.info( logPrefix + " version [" + getDescription().getVersion() + "] loaded" );
	}

	@Override
	public void onDisable() {
    	try {
    		config.save();
    	}
    	catch(ConfigException e) {
    		log.warning(logPrefix + " error saving configuration during onDisable");
    		e.printStackTrace();
    	}
    	
    	log.info( logPrefix + " version [" + getDescription().getVersion() + "] unloaded" );
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    	return cmdProcessor.onCommand(sender, command, commandLabel, args);
    }
	
    public void loadConfig() throws ConfigException, IOException {
		config = ConfigFactory.getInstance(ConfigFactory.Type.YAML, this, getDataFolder()+"/config.yml");
		config.load();
    }
    
	public GrinderManager getGrinderManager() { return grinderManager; }

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
		
		if( debug )
			log.setLevel(Level.FINEST);
		else
			log.setLevel(oldLogLevel);
	}
	@Override
	public boolean isDebug() { return debug; }
	
	@Override
	public JavaPlugin getPlugin() {
		return this;
	}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public String getLogPrefix() {
		if( logPrefix == null )
	    	logPrefix = "[" + getDescription().getName() + "]";

		return logPrefix;
	}

	@Override
	public File getJarFile() {
		return getFile();
	}

	@Override
	public ClassLoader getClassLoader() {
		return super.getClassLoader();
	}
	
	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public String getBasePermissionString() {
		return "mgc.";
	}

	@Override
	public boolean hasPermission(CommandSender p, String node) {
		return permWrapper.hasPermission(p, node);
	}

}
