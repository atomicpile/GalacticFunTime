/**
 * This class contains the main method to start the game. Currently is used to
 * create the game, and add entities to the game in the form of enemy ships.
 * 
 * @author Andrew
 * 
 */
public class GalacticConquestMain {

	/**
	 * The main method for the game, creates the game engine and adds entities
	 * to it. Also sets the size of the game window...currently the background
	 * image is not scalable to this setting, so the window size isn't changed
	 * much.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates the game engine for the game. The whole game is constructed
		// here. The size of the game window in pixels is the only parameter.
		GameEngine engine = new GameEngine(1600, 1024);
		// Adds entities to the engine in the form of different class types (ie.
		// destroyer). Only really use this space to add enemies for testing.
		engine.addEntity(new Destroyer());
		engine.addEntity(new Destroyer());
		engine.addEntity(new Destroyer());
		engine.addEntity(new Destroyer());
		engine.addEntity(new Destroyer());
		engine.addEntity(new Destroyer());
		engine.addEntity(new Destroyer());
	}
}
