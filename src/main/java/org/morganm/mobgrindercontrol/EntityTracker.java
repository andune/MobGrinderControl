/**
 * 
 */
package org.morganm.mobgrindercontrol;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * @author morganm
 *
 */
public class EntityTracker {
	final int ENTITY_DEATH_SIZE = 50;
	final int ENTITY_SPAWN_SIZE = 50;
	
	EntityDeath[] recentEntityDeaths = new EntityDeath[ENTITY_DEATH_SIZE];
	int entityDeathHead = 0;
	
	EntitySpawn[] recentEntitySpawns = new EntitySpawn[ENTITY_SPAWN_SIZE];
	int entitySpawnHead = 0;
	
	public EntityTracker() {
	}
	
	public void addEntityDeath(Entity entity, Location location, long time) {
		System.out.println("added entity death at location "+location);
		if( recentEntityDeaths[entityDeathHead] == null )
			recentEntityDeaths[entityDeathHead] = new EntityDeath();
		
		recentEntityDeaths[entityDeathHead].entity = entity;
		recentEntityDeaths[entityDeathHead].location = location;
		recentEntityDeaths[entityDeathHead].time = time;
		
		entityDeathHead++;
		
		if( entityDeathHead >= ENTITY_DEATH_SIZE )
			entityDeathHead = 0;
	}
	
	public void addEntitySpawn(Entity entity, Location location, long time) {
		if( recentEntitySpawns[entitySpawnHead] == null )
			recentEntitySpawns[entitySpawnHead] = new EntitySpawn();
		
		recentEntitySpawns[entitySpawnHead].entity = entity;
		recentEntitySpawns[entitySpawnHead].location = location;
		recentEntitySpawns[entitySpawnHead].time = time;
		
		entitySpawnHead++;
		
		if( entitySpawnHead >= ENTITY_SPAWN_SIZE )
			entitySpawnHead = 0;
	}
	
	class EntityDeath {
		public Entity entity;
		public Location location;
		public long time;
	}
	
	class EntitySpawn {
		public Entity entity;
		public Location location;
		public long time;
	}
}
