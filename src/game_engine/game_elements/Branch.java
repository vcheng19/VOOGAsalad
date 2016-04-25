package game_engine.game_elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import game_engine.properties.Position;

import java.util.List;

/*
 * Internal API that will be used in order to represent paths 
 * for enemy movements.
 */

public class Branch implements Serializable{
	private List<Position> myPositions;
	private Map<Position, Position> forwardPositions;
	private Map<Position, Position> backwardPositions;
	private boolean cycle;
	private List<Branch> myNeighbors;

	public Branch(List<Position> positions){
		myPositions = positions;
		myNeighbors = new ArrayList<>();
		initialize();
	}

	public Branch() {
		this.myPositions = new ArrayList<>();
		this.myNeighbors = new ArrayList<>();
		initialize();
	}

	public Branch(List<Position> positions, List<Branch> neighbors) {
		cycle = false;
		initialize(positions, neighbors);
	}

	public void initialize(){
		if(myPositions == null)
			myPositions = new ArrayList<>();
		if(forwardPositions == null)
			forwardPositions = new HashMap<Position, Position>();
		if(backwardPositions == null){
			backwardPositions = new HashMap<Position, Position>();
		}
		if(myNeighbors == null)
			myNeighbors = new ArrayList<>();
		setNextPositions();
	}

	public void initialize(List<Position> list, List<Branch> neighbors){
		myPositions = list;
		myNeighbors = neighbors;
		forwardPositions = new HashMap<Position, Position>();
		backwardPositions = new HashMap<Position, Position>();
		setNextPositions();
	}

	public void setCycle(boolean state){
		cycle = state;
		setNextPositions();
	}
	public void addPosition(Position append){
		myPositions.add(append);
		setNextPositions();
	}
	private void setNextPositions(){
		int size = cycle ? myPositions.size() : myPositions.size() - 1;
		if(myPositions.size() < 1){
			return;
		}
		double x = myPositions.get(0).getX();
		double y = myPositions.get(0).getY();
		for(int i = 0;i < size;i++){
			Position p1 = myPositions.get(i);
			Position p2 = myPositions.get((i+1)%myPositions.size());
			double vx = p2.getX() - p1.getX();
			double vy = p2.getY() - p1.getY();
			double mag = Math.sqrt(vx*vx + vy*vy);
			vx /= mag;
			vy /= mag;
			while((vx == 0 || (p2.getX() - x)/vx > 0 ) && (vy == 0 || (p2.getY() - y)/vy > 0)){
				Position newPosition = new Position(x + vx, y + vy);
				//myPositions.add(newPosition);
				forwardPositions.put(new Position(x, y), newPosition);
				backwardPositions.put(newPosition, new Position(x, y));
				x += vx;
				y += vy;
			}
			forwardPositions.put(new Position(x - vx, y - vy), p2);
			backwardPositions.put(p2,new Position(x - vx, y - vy));
			x = p2.getX();
			y = p2.getY();
		}
	}


	/*
	 * Gets the next position (point) in the path.
	 * This will be used in order to determine which direction 
	 * an Enemy needs to move in next.
	 *
	 * @return	The next Position in the list of Positions that represent the path being taken.
	 */
	public Position getNextPosition(Position currentPosition, Position moveTowards){
		if(currentPosition.equals(myPositions.get(myPositions.size()-1))){
			return null;
		}
		Map<Position, Position> use = moveTowards.equals(myPositions.get(0)) ? backwardPositions : forwardPositions;
		if(use.containsKey(currentPosition)){
			return use.get(currentPosition);
		}
		else{
			double minDist = Double.MAX_VALUE;
			Position closest = null;
			for(Position pos : use.keySet()){
				if(pos.distanceTo(currentPosition) < minDist){
					closest = pos;
					minDist = pos.distanceTo(currentPosition);
				}
			}
			return use.get(closest);
		}
	}

	//	public Branch copyBranch(){
	//		Branch newPath = new Branch();
	//		this.myPositions.forEach(t -> {
	//			newPath.addPosition(t.copyPosition());
	//		});
	//		newPath.addNeighbors(myNeighbors.stream().map(b -> b.copyBranch()).collect(Collectors.toList()));
	//		return newPath;
	//	}

	/*
	 * this should probably be deprecated because when units are moving along paths 
	 * they already need to know which positions they want to get to.
	 */
	public boolean isUnitAtLastPosition(Unit u) {
		Position lastPos = myPositions.get(myPositions.size()-1);
		return u.getProperties().getPosition().getX() == lastPos.getX() &&
				u.getProperties().getPosition().getY() == lastPos.getY();
	}

	public List<Position> getMyPositions() {
		return myPositions;
	}

	public Position getFirstPosition() {
		return myPositions.get(0);
	}

	public Double getNextDirection (Position currentPosition, Position moveTowards) {
		Position nextPosition = getNextPosition(currentPosition, moveTowards);
		if(nextPosition == null){
			nextPosition = currentPosition;
		}
		double dx = nextPosition.getX() - currentPosition.getX();
		double dy = nextPosition.getY() - currentPosition.getY();
		double newDir = Math.atan2((dy), (dx));
		double degreesDir = dx < 0 ? 270 - Math.toDegrees(newDir) : 90 - Math.toDegrees(newDir);
		return degreesDir;
	}

	public Position getLastPosition() {
		return myPositions.get(myPositions.size()-1);
	}

	//	public Position getSecondPosition(){
	//		if(getMyP().size() <= 1)
	//			return null;
	//		return getAllPositions().get(1);
	//	}

	public void addNeighbor(Branch neighbor){
		this.myNeighbors.add(neighbor);
	}

	public void addPositions(List<Position> positions){
		for(Position pos : positions){
			addPosition(pos);
		}
	}

	public List<Position> getPositions(){
		return myPositions;
	}

	public List<Branch> getNeighbors(){
		return myNeighbors;
	}

	public List<Position> cutoffByPosition(Position pos){
		List<Position> cutoff = new ArrayList<>();
		for(int x=myPositions.indexOf(pos); x<myPositions.size(); x++){
			cutoff.add(myPositions.get(x));
			if(x != myPositions.indexOf(pos)){
				myPositions.remove(x);
				x--;
			}
		}
		return cutoff;
	}

	public void addNeighbors(List<Branch> neighbors) {
		myNeighbors.addAll(neighbors);
	}

	public List<Branch> removeNeighbors(List<Branch> neighbors){
		List<Branch> removed = new ArrayList<>();
		for(Branch neighbor : neighbors){
			if(myNeighbors.contains(neighbor)){
				myNeighbors.remove(neighbor);
				removed.add(neighbor);
			}
		}
		return removed;
	}

	public String toString(){
		String first = "";
		if(myPositions.size() > 0){
			first = myPositions.get(0)+"";
		}
		String last = "";
		if(myPositions.size() > 0){
			last = myPositions.get(myPositions.size()-1)+"";
		}
		return "Branch positions: " + first+" "+last+"\n";
	}

	public int getLength(){
		return getPositions().size();
	}

	public List<Branch> getForwardNeighbors(){
		List<Branch> forwards = new ArrayList<>();
		for(Branch b : myNeighbors){
			if(b.getFirstPosition().equals(getLastPosition())){
				forwards.add(b);
			}
		}
		return forwards;
	}

	public boolean equals(Branch branch){
		for(int x=0; x<branch.getPositions().size(); x++){
			if(!branch.getPositions().get(x).equals(this.getPositions().get(x))){
				return false;
			}
		}
		return true;
	}

	public void removeNeighbor(Branch b) {
		this.myNeighbors.remove(b);
	}

	public Branch deepCopy(Map<Branch, Branch> isomorphism){
		Branch copy = isomorphism.get(this);
		if (copy == null) {
			copy = new Branch(this.getMyPositions(), this.getNeighbors());
			isomorphism.put(this, copy);
			for (Branch neighbor : this.myNeighbors) {
				copy.addNeighbor(neighbor.deepCopy(isomorphism));
			}
		}
		return copy;
	}
	
	public Branch copyBranch() {
        Branch obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            out.close();
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = (Branch) in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

}