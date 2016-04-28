package game_engine.physics;

import java.util.List;
import game_engine.properties.Bounds;
import game_engine.properties.Position;


public class EncapsulationChecker {

    public static boolean encapsulates (List<Position> inner, List<Position> outer) {
        for (Position pos : inner) {
            if (!insidePolygon(outer, pos)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean encapsulates (List<Position> pos, Bounds outer) {
        return EncapsulationChecker.encapsulates(pos, outer.getPositions());
    }

    private static boolean insidePolygon (List<Position> bounds, Position p) {
        int counter = 0;
        double xinters;
        Position p1 = bounds.get(0);
        int numPos = bounds.size();
        for (int i = 1; i <= numPos; i++) {
            Position p2 = bounds.get(i % numPos);
            if (p.getY() > Math.min(p1.getY(), p2.getY())) {
                if (p.getY() <= Math.max(p1.getY(), p2.getY())) {
                    if (p.getX() <= Math.max(p1.getX(), p2.getX())) {
                        if (p1.getY() != p2.getY()) {
                            xinters =
                                    (p.getY() - p1.getY()) * (p2.getX() - p1.getX()) /
                                      (p2.getY() - p1.getY()) + p1.getX();
                            if (p1.getX() == p2.getX() || p.getX() <= xinters)
                                counter++;
                        }
                    }
                }
            }
            p1 = new Position(p2.getX(), p2.getY());
        }
        if (counter % 2 == 0)
            return (false);
        else
            return (true);
    }

}
