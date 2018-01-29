import statics.*;
import nonStatics.*;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Grid;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

public class AntFarmRunner {
	public static void main(String[] args) {
		Grid<Actor> grid = new BoundedGrid<Actor>(19, 19);
        ActorWorld world = new ActorWorld(grid);

        world.add(new Location(2, 2), new Cookie());
        world.add(new Location(17, 2), new Cake());
        world.add(new Location(9, 15), new Queen());

        for (int i = 0; i < 25; i++)
        	world.add(new WorkerAnt());

        world.show();
	}
}