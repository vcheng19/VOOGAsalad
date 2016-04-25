package game_engine.factories;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import exceptions.CorrectableException;
import exceptions.WompException;
import game_engine.affectors.Affector;
import game_engine.affectors.AffectorData;
import game_engine.game_elements.Branch;
import game_engine.game_elements.Unit;
import game_engine.libraries.UnitLibrary;
import game_engine.properties.Bounds;
import game_engine.properties.Health;
import game_engine.properties.Mass;
import game_engine.properties.Position;
import game_engine.properties.Price;
import game_engine.properties.State;
import game_engine.properties.UnitProperties;
import game_engine.properties.Velocity;
import javafx.scene.image.Image;

public class UnitFactory {

	private UnitLibrary myUnitLibrary;
	private BoundsFactory myBoundsFactory;

	public UnitFactory(){
		this.myUnitLibrary = new UnitLibrary();
		this.myBoundsFactory = new BoundsFactory();
	}
	
	public UnitFactory(UnitLibrary unitLibrary){
		this.myUnitLibrary = unitLibrary;
		this.myBoundsFactory = new BoundsFactory();
	}
	
	// Pass field inputs here
	public Unit createUnit(HashMap<String, String> inputs){
		String type = getType(inputs.get("Type"));
		String unitType = getUnitType(inputs.get("Unit Type"));
		UnitProperties newProperties = new UnitProperties();
		newProperties.setHealthProp(getUnitHealth(inputs.get("Health")));
		newProperties.setPriceProp(getUnitPrice(inputs.get("Price")));
		newProperties.setMassProp(getUnitMass(inputs.get("Mass")));
//		newProperties.setTeamProp(getUnitTeam(inputs.get("Team")));
		newProperties.setStateProp(getUnitState(inputs.get("State")));
		UnitProperties unitProperties = createPropertiesByType(type, newProperties);
		return createUnit(getName(type, unitType), unitProperties);
	}
	
	public UnitLibrary getUnitLibrary(){
		return myUnitLibrary;
	}
	
	private String getName(String type, String unitType){
		return type+unitType;
	}
	
	private Unit createUnit(String name, UnitProperties unitProperties){
		return new Unit(name, unitProperties);
	}
	
	public void setUnitBounds(Unit unit, Image image){
		unit.getProperties().setBounds(myBoundsFactory.createImageBounds(image));
	}
	
	private String getUnitType(String str){
		String type = str;
		if(type == null){
			CorrectableException we = new CorrectableException("Invalid name. Please enter a name.", String.class);
			type = we.getResult();
		}
		return type;
	}
	
	private String getType(String str){
		String type = str;
		if(type == null){
			CorrectableException we = new CorrectableException("Invalid name. Please enter a name.", String.class);
			type = we.getResult();
		}
		return type;
	}
	
	private Health getUnitHealth(String str){
		double health = 0;
		try{
			health = Double.parseDouble(str);
		}
		catch(Exception e){
			CorrectableException we = new CorrectableException("Invalid health value. Please enter a number", Double.class);
			health = Double.parseDouble(we.getResult());
		}
		return new Health(health);
	}
	
	private Price getUnitPrice(String str){
		double price = 0;
		try{
			price = Double.parseDouble(str);
		}
		catch(Exception e){
			CorrectableException we = new CorrectableException("Invalid price value. Please enter a number", Double.class);
			price = Double.parseDouble(we.getResult());
		}
		return new Price(price);
	}
	
	private Mass getUnitMass(String str){
		double mass = 0;
		try{
			mass = Double.parseDouble(str);
		}
		catch(Exception e){
			CorrectableException we = new CorrectableException("Invalid mass value. Please enter a number", Double.class);
			mass = Double.parseDouble(we.getResult());
		}
		return new Mass(mass);
	}
	
//	private Team getUnitTeam(String str){
//		double team = 0;
//		try{
//			team = Double.parseDouble(str);
//		}
//		catch(Exception e){
//			CorrectableException we = new CorrectableException("Invalid team value. Please enter a number", Double.class);
//			team = Double.parseDouble(we.getResult());
//		}
//		return new Team(team);
//	}
	
	private State getUnitState(String str){
		double state = 0;
		try{
			state = Double.parseDouble(str);
		}
		catch(Exception e){
			CorrectableException we = new CorrectableException("Invalid team value. Please enter a number", Double.class);
			state = Double.parseDouble(we.getResult());
		}
		return new State(state);
	}

	public void setUnitType(Unit unit, String type) throws WompException{
		Affector a = getPathFollowingAffector(type);
		a.setTTL(Integer.MAX_VALUE);
		setupPathFollowingType(unit, type);
	}

	private void setupPathFollowingType(Unit unit, String type){
		List<String> pathTypes = Arrays.asList("PathFollowPositionMoveAffector", "RandomPathFollowAffector", "AIPathFollowAffector");
		for(String p : pathTypes){
			if(!p.equals(type))
				unit.removeAffectorsByName(p);
		}
	}

	private Affector getPathFollowingAffector(String type) throws WompException{
		Class<?> clazz = null;
		Constructor<?> constructor = null;
		Object instance = null;
		try {
			clazz = Class.forName(type);
			constructor = clazz.getConstructor(String.class, Integer.class);
			instance = constructor.newInstance(new AffectorData());
		} catch (Exception e) {
			throw new WompException("Invalid path affector.");
		}
		return (Affector) instance;
	}

	public void addChildrenToUnit(String name, List<Unit> children){
		myUnitLibrary.getUnitByName(name).addChildren(children);
	}

	public void addAffectorsToUnit(String name, List<Affector> affectors){
		myUnitLibrary.getUnitByName(name).addAffectors(affectors);
	}

	public void addAffectorsToApplyToUnit(String name, List<Affector> affectorsToApply){
		myUnitLibrary.getUnitByName(name).addAffectorsToApply(affectorsToApply);
	}

	public void addBranchesToUnit(String name, List<Branch> branches, Position spawn, double currentDirection){
		myUnitLibrary.getUnitByName(name).getProperties().getMovement().setBranches(branches, spawn, currentDirection);
	}

	public void addStartingBranch(String name, Branch startingBranch, Position spawn, double currentDirection){
		myUnitLibrary.getUnitByName(name).getProperties().getMovement().setBranches(Arrays.asList(startingBranch), spawn, currentDirection);
	}

	public void setNewPropertiesForUnit(String name, UnitProperties newProperties){
		myUnitLibrary.getUnitByName(name).setProperties(newProperties);
	}

	public void setNewBoundPositionsForUnit(String name, List<Position> bounds){
		myUnitLibrary.getUnitByName(name).getProperties().getBounds().setPositions(bounds);
	}

	public void setNewBoundsForUnit(String name, Bounds bounds){
		myUnitLibrary.getUnitByName(name).getProperties().getBounds().setPositions(bounds.getPositions());
	}

	public void setNewHealthForUnit(String name, double health){
		myUnitLibrary.getUnitByName(name).getProperties().getHealth().setValue(health);
	}

	public void setNewPriceForUnit(String name, double price){
		myUnitLibrary.getUnitByName(name).getProperties().getPrice().setPrice(price);
	}

	public void setNewBranchesForUnit(String name, List<Branch> branches, Position spawn, double currentDirection){
		myUnitLibrary.getUnitByName(name).getProperties().getMovement().setBranches(branches, spawn, currentDirection);
	}

	public void setNewSpeedForUnit(String name, double speed){
		myUnitLibrary.getUnitByName(name).getProperties().getVelocity().setSpeed(speed);
	}

	public void setNewDirectionForUnit(String name, double direction){
		myUnitLibrary.getUnitByName(name).getProperties().getVelocity().setDirection(direction);
	}

	public void setNewVelocityForUnit(String name, Velocity velocity){
		setNewDirectionForUnit(name, velocity.getDirection());
		setNewSpeedForUnit(name, velocity.getSpeed());
	}

	public void addNewAffectorForUnit(String name, Affector affector){
		myUnitLibrary.getUnitByName(name).addAffectors(Arrays.asList(affector));
	}

	public void addNewAffectorToApplyForUnit(String name, Affector affectorToApply){
		myUnitLibrary.getUnitByName(name).addAffectorsToApply(Arrays.asList(affectorToApply));
	}
	
	public UnitProperties createPropertiesByType(String type, UnitProperties properties){
		return myUnitLibrary.addPropertiesByType(type, properties);
	}
	
	public void changePropertiesByType(String type, UnitProperties newProperties) throws WompException{
		this.myUnitLibrary.changePropertiesType(type, newProperties);
	}
	
	public UnitProperties createProperties(String type){
		UnitProperties libraryProp = myUnitLibrary.getPropertyByType(type);
		if(libraryProp == null){
			return new UnitProperties();
		}
		return libraryProp;
	}
	
	public List<String> getFields(){
		//return Arrays.asList("Unit Type", "Health", "Team", "Initial Speed", "Initial Direction", "Price", "State");
		return Arrays.asList("UnitType", "Type", "Health", "Initial Speed", "Initial Direction", "Price", "State", "Mass");
		//return Arrays.asList("Health", "Team", "Initial Speed", "Initial Direction", "Price", "State");
	}
	
	public List<String> getUnitTypes(){
		return Arrays.asList("Enemy", "Tower", "Projectile", "Terrain");
	}
	
}