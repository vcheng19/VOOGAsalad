package game_engine.game_elements;

import java.util.Collection;

import game_engine.properties.UnitProperties;

/*
* Internal API that will be used to reflect the inclusion of terrain effects in games. 
* Most implementations of the Terrain API will apply some sort of affector to game elements within
* a certain area on the map.
*/
public interface Terrain {
	/*
	* Grabs all of the units within the area specified by the Terrain 
	*
	* @return	
	*/
	public static Collection<Unit> getAffectedUnits(Collection<Unit> allUnits) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	* Sets the affector for the terrain, which will be applied to each unit within the the terrain
	*/
	public void setAffector(Affector effect);

	/*
	* Applies the currently set affector to each unit that is considered inside the terrain.
	*/
	public void applyEffect();
	
	public static void affectTower(Collection<Unit> allUnits){
		Collection<Unit> affectedTowers = getAffectedUnits(allUnits);
		
		for(Unit tower: affectedTowers){
			UnitProperties up = tower.getProperties(); 
			Affector affect = getEffect(); 
			affect.apply(up);
		}
	}
	
	public static Affector getEffect() {
		return null;
	}
}
