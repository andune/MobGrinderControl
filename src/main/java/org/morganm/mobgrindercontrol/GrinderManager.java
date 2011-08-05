/**
 * 
 */
package org.morganm.mobgrindercontrol;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

/**  Class to manage known Grinders.
 * 
 * @author morganm
 *
 */
public class GrinderManager {
	private ArrayList<Grinder> grinders = new ArrayList<Grinder>();
	
	public void addGrinder(Grinder grinder) {
		grinders.add(grinder);
	}
	
	/** Note note all Grinders returned by this call are active.  use Grinder.isActiveGrinder() when
	 * iterating to find active ones.
	 * 
	 * @return
	 */
	public List<Grinder> getGrinders() {
		return grinders;
	}
	
	/** Given a death location, return any known grinder responsible for that death.
	 * 
	 * @param deathLocation
	 */
	public Grinder getGrinder(Location deathLocation) {
		for(Grinder grinder: grinders) {
			if( grinder.isGrinderDeath(deathLocation) )
				return grinder;
		}
		
		return null;
	}
}
