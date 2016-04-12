package game_engine.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import game_engine.affectors.Affector;
import game_engine.game_elements.Projectile;
import game_engine.game_elements.Tower;
import game_engine.libraries.AffectorLibrary;
import game_engine.game_elements.Path;
import game_engine.game_elements.Projectile;
import game_engine.game_elements.Tower;
import game_engine.game_elements.Unit;
import game_engine.properties.Bounds;
import game_engine.properties.Health;
import game_engine.properties.Position;
import game_engine.properties.State;
import game_engine.properties.UnitProperties;
import game_engine.properties.Velocity;


public class TowerFactory {

	private AffectorLibrary myAffectorLibrary;

<<<<<<< HEAD
	public TowerFactory(AffectorLibrary affectorLibrary){
		this.myAffectorLibrary = affectorLibrary;
	}

	public Tower createFourWayTower(String name, List<Unit> myProjectiles2, Position startingPosition){
		List<Projectile> myProjectiles = new ArrayList<Projectile>();
		Affector move = myAffectorLibrary.getAffector("Homing", "Move");
		move.setTTL(Integer.MAX_VALUE);
		Projectile p = new Projectile("Projectile", Arrays.asList(move), 3);
		p.setDeathDelay(15);
		p.setTTL(1000000);
		p.setFireRate(90);
		Velocity velocity = new Velocity(2, 180);        
		List<Position> l1 = new ArrayList<>();
		l1.add(new Position(0,0));
		l1.add(new Position(30,0));
		l1.add(new Position(30,30));
		l1.add(new Position(0,30));
		Bounds b = new Bounds(l1);
		State st = new State("Moving");
		Path p2 = new Path("Something here");
		p2.addPosition(startingPosition.copyPosition());
		p2.addPosition(new Position(startingPosition.getX(), startingPosition.getY()-900));
		Health health = new Health(30);
		UnitProperties properties = new UnitProperties(health, null, null, velocity, b, startingPosition.copyPosition(), null, st, p2);
		Affector damage = myAffectorLibrary.getAffector("Constant", "HealthDamage");
		damage.setTTL(1);
		damage.setBaseNumbers(Arrays.asList(new Double(10)));
		Affector stateToDamaging = myAffectorLibrary.getAffector("State", "Change");
		stateToDamaging.setBaseNumbers(Arrays.asList(new Double(4)));
		stateToDamaging.setTTL(1);
		p.setAffectorsToApply(Arrays.asList(new Affector[]{damage, stateToDamaging}));
		p.setProperties(properties);
		myProjectiles.add(p);
		return createSpecifiedTower(name, myProjectiles2, myProjectiles);
	}

	public Tower createSpecifiedTower(String name, List<Unit> myProjectiles2, List<Projectile> myProjectiles) {
		List<Affector> affectors = new ArrayList<>();
		Tower t = new Tower(name, affectors, myProjectiles2, myProjectiles, 2);
		List<Position> l1 = new ArrayList<>();
		l1.add(new Position(0,0));
		l1.add(new Position(70,0));
		l1.add(new Position(70,55));
		l1.add(new Position(0,55));
		Bounds b = new Bounds(l1);
		Health health2 = new Health(50);
		Position position2 = new Position(200, 300);
		Velocity velocity2 = new Velocity(0, 90);
		State st = new State ("Stationary");
		Path p2 = new Path("Something here");
		UnitProperties properties2 = new UnitProperties(health2, null, null, velocity2, b, position2, null, st, p2);
		t.setProperties(properties2);
		t.setTTL(1000000);
		t.setDeathDelay(100);
		return t;
	}

=======
    public TowerFactory(AffectorLibrary affectorLibrary){
            this.myAffectorLibrary = affectorLibrary;
    }
    
    public Tower createFourWayTower(String name, List<Unit> myProjectiles2, Position startingPosition){
        List<Projectile> myProjectiles = new ArrayList<Projectile>();
        Affector move = myAffectorLibrary.getAffector("Homing", "Move");
        move.setTTL(Integer.MAX_VALUE);
        Projectile p = new Projectile("Projectile", Arrays.asList(move), 3);
        p.setDeathDelay(15);
        p.setTTL(1000000);
        p.setFireRate(90);
        Velocity velocity = new Velocity(2, 180);        
        List<Position> l1 = new ArrayList<>();
        l1.add(new Position(0,0));
        l1.add(new Position(30,0));
        l1.add(new Position(30,30));
        l1.add(new Position(0,30));
        Bounds b = new Bounds(l1);
        State st = new State("Moving");
        Path p2 = new Path("Something here");
        p2.addPosition(startingPosition.copyPosition());
	p2.addPosition(new Position(startingPosition.getX(), startingPosition.getY()-900));
        UnitProperties properties = new UnitProperties(null, null, null, velocity, b, startingPosition.copyPosition(), null, st, p2);
        Affector damage = myAffectorLibrary.getAffector("Constant", "HealthDamage");
        damage.setTTL(1);
        damage.setBaseNumbers(Arrays.asList(new Double(10)));
        Affector stateToDamaging = myAffectorLibrary.getAffector("State", "Change");
        stateToDamaging.setBaseNumbers(Arrays.asList(new Double(4)));
        stateToDamaging.setTTL(1);
        p.setAffectorsToApply(Arrays.asList(new Affector[]{damage, stateToDamaging}));
        p.setProperties(properties);
        myProjectiles.add(p);
        return createSpecifiedTower(name, myProjectiles2, myProjectiles);
    }
    
    public Tower createSpecifiedTower(String name, List<Unit> myProjectiles2, List<Projectile> myProjectiles) {
        List<Affector> affectors = new ArrayList<>();
        Tower t = new Tower(name, affectors, myProjectiles2, myProjectiles, 2);
        List<Position> l1 = new ArrayList<>();
        l1.add(new Position(0,0));
        l1.add(new Position(70,0));
        l1.add(new Position(70,55));
        l1.add(new Position(0,55));
        Bounds b = new Bounds(l1);
        Health health2 = new Health(50);
        Position position2 = new Position(200, 300);
        Velocity velocity2 = new Velocity(0, 90);
        State st = new State ("Stationary");
        Path p2 = new Path("Something here");
        UnitProperties properties2 = new UnitProperties(health2, null, null, velocity2, b, position2, null, st, p2);
        t.setProperties(properties2);
        t.setTTL(1000000);
        t.setDeathDelay(100);
        return t;
    }
    
>>>>>>> 464265eb94e4881348dbb63df4eb74de3e8748b5
}