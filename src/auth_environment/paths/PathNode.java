package auth_environment.paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import game_engine.affectors.Affector;
import game_engine.game_elements.Enemy;
import game_engine.game_elements.Path;
import game_engine.game_elements.Terrain;
import game_engine.game_elements.Unit;
import game_engine.properties.Position;

public class PathNode {

	private Branch myRoot;
	private int myID;
	
	public PathNode(int graphID){
		this.myID = graphID;
	}
	
	public PathNode(Branch root, int ID){
		this.myID = ID;
		this.myRoot = root;
	}
	
	public void setRoot(Branch root){
		this.myRoot = root;
	}
	
	public Branch getRoot(){
		return myRoot;
	}
	
	public List<Unit> getEnemies(){
		return getUnits(myRoot, new Enemy("0", new ArrayList<Affector>(), 0));
	}
	
	public List<Unit> getPaths(){
		return getUnits(myRoot, new Path("0"));
	}
	
	public List<Unit> getTerrains(){
		return getUnits(myRoot, new Terrain("0", 0));
	}
	
	public List<Branch> getPathNodes(Branch root){
		List<Branch> nodes = root.getNeighbors().stream().filter(n -> getPathNodes(n) != null).collect(Collectors.toList());
		nodes.add(root);
		return nodes;
	}
	
	// Uses post-order traversal to extract List of appropriate Unit
	private List<Unit> getUnits(Branch node, Unit target){
		List<Unit> myUnits = new ArrayList<>();
		for(Branch n : node.getNeighbors()){
			//myUnits.addAll(n.getUnitsByType(target));
			myUnits.addAll(getUnits(n, target));
		}
		return myUnits;
	}
	
	public int getID(){
		return myID;
	}
	
	public List<Branch> getPathByEdgePosition(Position pos){
		return myRoot.getNeighbors().stream().filter(
				n -> n.getPositions().get(0).equals(pos) || n.getPositions().get(n.getPositions().size()-1).equals(pos))
				.collect(Collectors.toList());
	}
	
	public Branch getPathByMidPosition(Position pos){
		Optional<Branch> graph = myRoot.getNeighbors().stream().filter(
				n-> n.getPositions().contains(pos) && !n.getPositions().get(0).equals(pos) && !n.getPositions().get(n.getPositions().size()-1).equals(pos))
				.findFirst();
		return graph.isPresent() ? graph.get() : null;
	}
	
}