package nonStatics;

import statics.*;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.awt.Color;
import java.util.*;

public class WorkerAnt extends Actor {
	// used for random turning when finding items
	int turns = 0;
	Random rand = new Random(System.currentTimeMillis() * System.currentTimeMillis());
	
	// used for storing queen location
	Location food;
	Location queen;
	boolean queenFound = false;
	boolean foodFound = false;
	
	// used for moving between the food and queen
	boolean adjacentQueen = false;
	boolean adjacentFood = false;
	boolean moveToFood = false;

	public WorkerAnt() {
		setDirection(rand.nextInt(8) * 45);
		setColor(Color.BLACK);
	}

	/*
	 * Getters
	 * for in app debugging
	 */

	public void getQueen() {
		System.out.println(queen);
	}

	public void getFood() {
		System.out.println(food);
	}

	/*
	 * Getters
	 */
	public void findingMovement() {
		turns = rand.nextInt(9);				// get random number
		for (int x = 0; x < turns; x++) {		// turn the random amounts of times
			turn();
		}
		
		if (canMove()) {		// if the bug can move, move, otherwise turn
			move();
		} else {
			turn();
		}
	}

	public void act() {

		Grid<Actor> gr = getGrid();
		Location loc = getLocation();
		ArrayList<Location> scan = gr.getOccupiedAdjacentLocations(loc);

		/*
		 * this block of code is used to scan the surrounding of the ant to check for
		 * food and queen
		 */
		for (Location x : scan) {
			// if x is food store x's location
			if (!foodFound && (gr.get(x) instanceof Cake || gr.get(x) instanceof Cookie)) {
				foodFound = true;
				food = new Location(x.getRow(), x.getCol());
			}
			// if x is the queen store x's location
			if (!queenFound && gr.get(x) instanceof Queen) {
				queenFound = true;
				queen = new Location(x.getRow(), x.getCol());
			}
			// if x is an ant communicate with x
			if (gr.get(x) instanceof WorkerAnt) {
				communicate((WorkerAnt) gr.get(x));
			}
		}

		// if the queen and food are found then call function doMove()
		// if not then do random finding movements using the findingMovement() function
		if (foodFound && queenFound) {
			doMove(); // function for going back and forth between food and queen
		} else {
			findingMovement(); // function for randomly moving trying to find items
		}
	}

	// communication function that takes in an ant to communicate with
	public void communicate(WorkerAnt x) {
		// if the ant to communicate with doesn't know where food is and this ant knows
		// tell the ant in question the location
		if (x.food == null && food != null) {
			x.foodFound = true;
			x.food = food;
		}
		// if the ant to communicate with doesn't know where the queen is and this ant knows
		// tell the ant in question the location
		if (x.queen == null && queen != null) {
			x.queenFound = true;
			x.queen = queen;
		}
	}

	public void doMove() {
		
		Grid<Actor> gr = getGrid();
		Location loc = getLocation();
		ArrayList<Location> scan = gr.getOccupiedAdjacentLocations(loc);
		
		/*
		 * scan around the bug for queens and food
		 */
		for (Location x : scan) {
			// if x is the queen then store the boolean value
			if (gr.get(x) instanceof Queen) {
				adjacentQueen = true;
				break;
			} else {
				adjacentQueen = false;
			}
			// if x is food then store the boolean value
			if (gr.get(x) instanceof Cake || gr.get(x) instanceof Cookie) {
				adjacentFood = true;
				break;
			} else {
				adjacentFood = false;
			}
		}
	
		/*
		 * These blocks of code are used to switch modes between moving towards
		 * the queen or moving towards the food
		 */
		
		// this if statement is checking for what direction the bug needs to go
		if (adjacentFood) {
			setColor(Color.RED);
			moveToFood = false;
		}
		if (adjacentQueen) {
			setColor(Color.BLACK);
			moveToFood = true;
		}
		
		
		if (moveToFood) {
			// move toward the food
			setDirection(getLocation().getDirectionToward(food));
			if (canMove()) {
				move();
			} else {
				while (!canMove())
					turn();
				move();
			}

		} else {
			// move toward the queen
			setDirection(getLocation().getDirectionToward(queen));
			if (canMove()) {
				move();
			} else {
				while (!canMove())
					turn();
				move();
			}
		}
		
	}

	/*
	 * 
	 * Helpers
	 * 
	 */
	public void turn() {
		setDirection(getDirection() + 45);
	}

	public void move() {
		Grid<Actor> gr = getGrid();
		if (gr == null) {
			return;
		}
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		if (gr.isValid(next)) {
			moveTo(next);
		} else {
			removeSelfFromGrid();
		}
	}

	public boolean canMove() {
		Grid<Actor> gr = getGrid();
		if (gr == null) {
			return false;
		}
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		if (!gr.isValid(next)) {
			return false;
		}
		Actor neighbor = (Actor) gr.get(next);
		return neighbor == null;
	}
}
