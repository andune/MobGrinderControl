/**
 * 
 */
package org.morganm.mobgrindercontrol;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author morganm
 *
 */
public class EntityListener extends org.bukkit.event.entity.EntityListener {
	
	@SuppressWarnings("unused")
	private PluginInterface plugin;
	private EntityTracker tracker;
	
	public EntityListener(PluginInterface plugin, EntityTracker tracker) {
		this.plugin = plugin;
		this.tracker = tracker;
	}
	
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();

		// we ignore non-living entities and Players
		if( !(entity instanceof LivingEntity) || (entity instanceof Player) )
			return;
		
		Location deathLocation = entity.getLocation();
		tracker.addEntityDeath(entity, deathLocation, System.currentTimeMillis() / 1000);
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
//		SpawnReason reason = event.getSpawnReason();
		// TODO Auto-generated method stub
		super.onCreatureSpawn(event);
	}
}
