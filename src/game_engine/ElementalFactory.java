package game_engine;

public class ElementalFactory {

	public EngineWorkspace myWorkspace;
	
	public ElementalFactory(EngineWorkspace workspace){
		this.myWorkspace = workspace;
	}
	
//	public Enemy createEnemy(String name){
//		Enemy enemy = new Enemy(name);
//		enemy.setWorkspace(myWorkspace);
//		return enemy;
//	}
	
}