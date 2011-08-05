/**
 * 
 */
package org.morganm.mobgrindercontrol;

import org.bukkit.Location;

/**
 * @author morganm
 *
 */
public class Grinder {
	private final static int GRINDER_DEATH_RANGE = 50;
	private final int KNOWN_GRINDER_DEATH_COUNT = 10;
	/* maximum time that can pass between deaths in order for deaths to count as part of the grinder.
	 * This only has any effect if the grinder is not yet active.
	 */
	private final long SECONDS_TO_COUNT_DEATH = 10;
	
	private Location deathLocation;
	private int deathX;
	private int deathY;
	private int deathZ;
	private int mobDeathCount = 0;
	private long lastDeathTime;			// in seconds
	
	public Grinder(Location deathLocation) {
		this.deathLocation = deathLocation;
		deathX = deathLocation.getBlockX();
		deathY = deathLocation.getBlockY();
		deathZ = deathLocation.getBlockZ();
	}
	
	public Location getDeathLocation() {
		return deathLocation;
	}

	/** This object may represent only a "possible" grinder.  This method can be used to distinguish
	 * between a possible and an active grinder. 
	 * 
	 * @return
	 */
	public boolean isActiveGrinder() {
		return mobDeathCount > KNOWN_GRINDER_DEATH_COUNT;
	}
	
	/** Increment the death counter.  A time for the death being incremented must be provided.
	 * 
	 * @param time
	 */
	public void incrementDeathCount(long timeInSeconds) {
//		System.out.println("incrementDeathCount, time="+timeInSeconds+", lastDeathTime = "+lastDeathTime+", this="+this);
		if( isActiveGrinder() || (timeInSeconds - lastDeathTime) < SECONDS_TO_COUNT_DEATH ) { 
//			System.out.println("incrementDeathCount, incremented. this="+this);
			mobDeathCount++;
		}
		
		if( timeInSeconds > lastDeathTime )
			lastDeathTime = timeInSeconds;
	}
	
	public int getDeathCount() {
		return mobDeathCount;
	}
	
	/** Return true if a location of a death is considered to be part of this grinder.
	 * 
	 * @param l
	 * @return
	 */
	public boolean isGrinderDeath(Location l) {
		if( l != null && l.getWorld().getName().equals(deathLocation.getWorld().getName()))
		{
			if( (deathX > l.getBlockX()-GRINDER_DEATH_RANGE && deathX < l.getBlockX()+GRINDER_DEATH_RANGE) &&
				(deathY > l.getBlockY()-GRINDER_DEATH_RANGE && deathY < l.getBlockY()+GRINDER_DEATH_RANGE) &&
				(deathZ > l.getBlockZ()-GRINDER_DEATH_RANGE && deathZ < l.getBlockZ()+GRINDER_DEATH_RANGE)
			  )
				return true;
		}
		return false;
	}
}
