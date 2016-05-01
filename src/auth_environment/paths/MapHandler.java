package auth_environment.paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game_engine.game_elements.Branch;
import game_engine.handlers.PositionHandler;
import game_engine.properties.Position;

public class MapHandler {
	
	/*
	 * In order to make a path using this class.... Patrick Grady 2:45PM 4/29/2016
	 * 
	 * 	
	 	MapHandler mh = new MapHandler(new ArrayList<Branch>(), new ArrayList<Branch>(), new ArrayList<Branch>());
		List<Position> path = new ArrayList<>();

		path.add(new Position(500, 200));
		path.add(new Position(200, 200));
		path.add(new Position(150, 400));
		path.add(new Position(50, 400));
		path.add(new Position(500, 401));

		mh.processPositions(path);
		mh.addSpawn(path.get(0));
		mh.addGoal(path.get(path.size() - 1));

		myBranches = mh.getEngineBranches();
		l.setGoals(mh.getGoals());
		l.setSpawns(mh.getSpawns());
	 */
	
	private PathGraphFactory myPGF;
	private PositionHandler myPositionHandler;
	private List<Branch> myEngineBranches;
	private List<Position> myGoals;
	private List<Position> mySpawns;

	public MapHandler(){
		myPGF = new PathGraphFactory();
		myPositionHandler = new PositionHandler();
		myEngineBranches = new ArrayList<>();
		myGoals = new ArrayList<>();
		mySpawns = new ArrayList<>();
		createGrid();
//		insertTestBranches();
	}

	public MapHandler(List<Branch> engineBranches, List<Position> spawns, List<Position> goals ){
		myEngineBranches = engineBranches;
		this.mySpawns = spawns; 
		this.myGoals = goals; 
		myPGF = new PathGraphFactory(engineBranches);
		myPositionHandler = new PositionHandler();
		mySpawns = new ArrayList<>();
		myGoals = new ArrayList<>();
		//		insertTestBranches();
	}

	public void processPositions(List<Position> positions){
		List<Position> interpolatedPositions = myPositionHandler.getInterpolatedPositions(positions, false);
		myPGF.insertBranch(interpolatedPositions);
	}

	public void splinePositions(List<Position> positions){
		List<Position> splinedPoints = getSplinedPoints(positions);
		myPGF.insertBranch(splinedPoints);
	}
	
	public void addSpawn(Position spawn){
		this.mySpawns.add(spawn);
	}
	
	public void addGoal(Position goal){
		this.myGoals.add(goal);
		processPositions(myPositionHandler.getInterpolatedPositions(Arrays.asList(goal), false));
	}

	public void createGrid(){
		double screenWidth = 500;
		double screenHeight = 500;
		addGoal(new Position(500, 500));
		addSpawn(new Position(0, 0));
		myPGF.insertGrid(screenWidth, screenHeight, getGridSquareSize(screenWidth, screenHeight));
	}

	private double getGridSquareSize(double screenWidth, double screenHeight){
		return screenWidth*screenHeight/5000;
	}

	public void insertTestBranches(){
		Position p1 = new Position(0, 30);
		Position p3 = new Position(200, 30);
		List<Position> b1 = Arrays.asList(p1, p3);
		processPositions(b1);
		addSpawn(p1);

		Position p4 = new Position(200, 30);
		Position p5 = new Position(400, 30);
		Position p6 = new Position(400, 200);
		List<Position> b2 = Arrays.asList(p4, p5, p6);
		processPositions(b2);

		Position p7 = new Position(200, 30);
		Position p8 = new Position(200, 200);
		Position p9 = new Position(400, 200);
		List<Position> b3 = Arrays.asList(p7, p8, p9);
		processPositions(b3);

		Position p10 = new Position(400, 200);
		Position p11 = new Position(400, 525);
		List<Position> b4 = Arrays.asList(p10, p11);
		processPositions(b4);

		Position p12 = new Position(100, 30);
		Position p13 = new Position(100, 200);
		List<Position> b5 = Arrays.asList(p12, p13);
		processPositions(b5);
		
		addGoal(p11);
	}

	private List<Position> getSplinedPoints(List<Position> positions){
		return null;
	}

	public PathGraphFactory getPGF(){
		return myPGF;
	}

	public List<Branch> getEngineBranches() {
		myEngineBranches.addAll(myPGF.getBranches());
		return myEngineBranches;
	}

	public List<Position> getGoals() {
		return myGoals;
	}
	
	public List<Position> getSpawns() {
		return mySpawns;
	}

}