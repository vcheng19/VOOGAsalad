package game_engine.affectors;

import java.util.List;

import game_engine.functions.Function;
import game_engine.game_elements.Unit;

public class DeathActivationAffector extends Affector{

	public DeathActivationAffector(AffectorData data){
		super(data);
	}
	
	public void apply(Unit unit){
		unit.setElapsedTimeToDeath();
	}
	
}