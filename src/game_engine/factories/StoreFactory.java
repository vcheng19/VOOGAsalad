package game_engine.factories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game_engine.affectors.Affector;
import game_engine.game_elements.Unit;
import game_engine.libraries.AffectorLibrary;
import game_engine.libraries.UnitLibrary;
import game_engine.store_elements.Store;
import game_engine.store_elements.Pair;

public class StoreFactory {
	private Store myStore;
	private Map<String, Unit> myUnits;
	private Map<String, Affector> myUpgrades;
	
	public StoreFactory(UnitLibrary ul, AffectorLibrary al){
		myUnits = new HashMap<String, Unit>();
		myUpgrades = new HashMap<String, Affector>();
		for(String name : ul.getUnitNames()){
			myUnits.put(name, ul.getUnitByName(name));
		}
		for(String name : al.getAffectorNames()){
			myUpgrades.put(name,  al.getAffector(name));
		}
	}
	public StoreFactory(Map<String, Unit> units, Map<String, Affector> upgrades){
		myStore = null;
		myUnits = units;
		myUpgrades = upgrades;
	}
	
	public void createNewStore(int money){
		myStore = new Store(money);
	}	
	
	public void addBuyableUnit(String name, int cost){
		myStore.addBuyableUnit(myUnits.get(name), cost);
	}
	
	public void addBuyableUnits(List<Pair<String, Integer>> unitsWithPrices){
		for(Pair p : unitsWithPrices){
			myStore.addBuyableUnit(myUnits.get(p.getLeft()),(Integer) p.getRight());
		}
	}
	
	public void addBuyableUnits(List<String> names, List<Integer> prices){
		for(int i = 0;i < names.size();i++){
			this.addBuyableUnit(names.get(i), prices.get(i));
		}
	}
	
	public void addUpgrade(String unitName,String upgradeName, int cost){
		myStore.addUpgrade(myUnits.get(unitName), myUpgrades.get(upgradeName), cost);
	}
	
	public void addUpgrades(List<String> unitNames, List<String> upgradeNames, List<Integer> costs){
		for(int i = 0;i < unitNames.size();i++){
			this.addUpgrade(unitNames.get(i), upgradeNames.get(i), costs.get(i));
		}
	}
	
	
}