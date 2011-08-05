/**
 * 
 */
package org.morganm.mobgrindercontrol;

import java.util.ArrayList;

import org.bukkit.Location;

/**
 * @author morganm
 *
 */
public class GrinderFinder implements Runnable {
	private final int DEATH_RANGE = 25;
	
//	private PluginInterface plugin;
	private EntityTracker tracker;
	private GrinderManager grinderManager;
	
	public GrinderFinder(PluginInterface plugin, EntityTracker tracker, GrinderManager grinderManager) {
//		this.plugin = plugin;
		this.tracker = tracker;
		this.grinderManager = grinderManager;
	}

	public void run() {
		System.out.println("GrinderFinder running");
		int checkStart= 0;
		int checkFull = 0;
		
		ArrayList<InterestingDeath> interestingDeaths = new ArrayList<InterestingDeath>();
		
		/* This runs asynchronously, so while this is running, it's possible the circular buffer
		 * is being added to at the same time.  This is fine, we just need to take care to be
		 * aware of that and deal with it appropriately.  Here we do that by tracking the head
		 * when we start looping so it's a fixed target for us to stop looping at.
		 */
		int index = tracker.entityDeathHead;
		int lockedHead = tracker.entityDeathHead;
		
		LOOP:
		do {
			checkStart++;
			index--;
			if( index < 0 )
				index = tracker.ENTITY_DEATH_SIZE-1;

			if( tracker.recentEntityDeaths[index] == null )
				continue;
			
			Location thisLoc = tracker.recentEntityDeaths[index].location;
			if( thisLoc == null )
				continue;
			
			String world = tracker.recentEntityDeaths[index].location.getWorld().getName();
			int x = tracker.recentEntityDeaths[index].location.getBlockX();
			int y = tracker.recentEntityDeaths[index].location.getBlockY();
			int z = tracker.recentEntityDeaths[index].location.getBlockZ();

			for(Grinder grinder : grinderManager.getGrinders()) {
//				System.out.println("checking possible grinder "+grinder);
				if( grinder.isGrinderDeath(thisLoc) ) {
					grinder.incrementDeathCount(tracker.recentEntityDeaths[index].time);
					
					// once a death has been assigned to a grinder, we remove it from the list
					// so we don't accidentally track it again.
					tracker.recentEntityDeaths[index].location = null;
					tracker.recentEntityDeaths[index].entity = null;
					tracker.recentEntityDeaths[index].time = 0;
					
					continue LOOP;
				}
			}
			
			// existing InterestingDeaths mean we found two deaths close together. Check to see if
			// this is a 3rd death near any existing InterestingDeath's we already found.  If so,
			// this location is promoted to possible grinder status.
			for(InterestingDeath id : interestingDeaths) {
//				System.out.println("checking InterestingDeath "+id);
				if( id.isClose(world, x, y, z) ) {
					Grinder grinder = new Grinder(thisLoc);
					grinder.incrementDeathCount(tracker.recentEntityDeaths[index].time);
					grinderManager.addGrinder(grinder);
					
					// once a death has been assigned to a grinder, we remove it from the list
					// so we don't accidentally track it again.
					tracker.recentEntityDeaths[index].location = null;
					tracker.recentEntityDeaths[index].entity = null;
					tracker.recentEntityDeaths[index].time = 0;
					
					continue LOOP;
				}
			}

			int searchIndex = index;
			do {
				searchIndex--;
				if( searchIndex < 0 )
					searchIndex = tracker.ENTITY_DEATH_SIZE-1;
				
				if( tracker.recentEntityDeaths[searchIndex] == null )
					continue;
				
				Location l = tracker.recentEntityDeaths[searchIndex].location;
				
				// is the death we're comparing to on same world and within the specified range?
				if( l != null && l.getWorld().getName().equals(thisLoc.getWorld().getName())) {
					checkFull++;
					
					/*
					System.out.println("x= "+x+", lowX="+(l.getBlockX()-DEATH_RANGE)+", highX="+(l.getBlockX()+DEATH_RANGE));
					System.out.println("y= "+y+", lowY="+(l.getBlockY()-DEATH_RANGE)+", highY="+(l.getBlockY()+DEATH_RANGE));
					System.out.println("z= "+z+", lowZ="+(l.getBlockZ()-DEATH_RANGE)+", highZ="+(l.getBlockZ()+DEATH_RANGE));
					*/
					
					if( (x > l.getBlockX()-DEATH_RANGE && x < l.getBlockX()+DEATH_RANGE) &&
						(y > l.getBlockY()-DEATH_RANGE && y < l.getBlockY()+DEATH_RANGE) &&
						(z > l.getBlockZ()-DEATH_RANGE && z < l.getBlockZ()+DEATH_RANGE)
					  )
					{
//						System.out.println("Found InterestingDeath");
						
						// if we're here, we now have two deaths that happened very close to each other
						InterestingDeath id = new InterestingDeath();
						id.world = l.getWorld().getName();
						id.minX = x-DEATH_RANGE;
						id.minY = y-DEATH_RANGE;
						id.minZ = z-DEATH_RANGE;
						id.maxX = x+DEATH_RANGE;
						id.maxY = y+DEATH_RANGE;
						id.maxZ = z+DEATH_RANGE;
						
						interestingDeaths.add(id);
					}
				}
			} while( searchIndex != index );
		} while( index != lockedHead );
		
		System.out.println("GrinderFinder finished running, checkStart = "+checkStart+", checkFull = "+checkFull);
	}
	
	class InterestingDeath {
		String world;
		
		int minX;
		int minY;
		int minZ;
		
		int maxX;
		int maxY;
		int maxZ;
		
		public boolean isClose(String world, int x, int y, int z) {
			if( this.world == null || !this.world.equals(world) )
				return false;
			
			if( (x > minX-DEATH_RANGE && x < maxX+DEATH_RANGE) &&
				(y > minY-DEATH_RANGE && y < maxY+DEATH_RANGE) &&
				(z > minZ-DEATH_RANGE && z < maxZ+DEATH_RANGE)
			  )
				return true;
			else
				return false;
		}
	}
}
