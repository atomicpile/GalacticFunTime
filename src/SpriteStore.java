import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * The sprite store provides functionality for retrieving sprites from the
 * resources folder, and caches loaded sprites so they don't have to be loaded
 * each time they are needed.
 * 
 * @author Andrew
 * 
 */
public class SpriteStore {

	// The singleton for the SpriteStore, this is the only instance of this
	// class.
	private static SpriteStore single = new SpriteStore();

	public static SpriteStore get() {
		return single;
	}

	// The cached sprite map, from reference to sprite instance
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	/**
	 * This method gets a sprite by taking a filename without the extension
	 * (currently only takes .png files).
	 * 
	 * @param ref
	 * @return
	 */
	public Sprite getSprite(String ref) {

		// Generates the proper string to use to get the image file. This can be
		// changed if the source folder name changes. Also adds the proper
		// extension.
		ref = "resources/" + ref + ".png";
		if (sprites.get(ref) != null) {
			// Returns the sprite from the cache if it has already been loaded
			return (Sprite) sprites.get(ref);
		}
		// Declaration of a bufferedImage for this sprite
		BufferedImage sourceImage = null;
		try {
			// Gets the image file from the resources folder
			URL url = this.getClass().getClassLoader().getResource(ref);
			if (url == null) {
				fail("Can't find ref: " + ref);
			}
			// Puts the image into memory
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			fail("Failed to load: " + ref);
		}
		// Creates an accelerated image of the right size to store sprite in
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		// Creates an image compatible with the current display device.
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),
				sourceImage.getHeight(), Transparency.BITMASK);
		// Draws source image into accelerated image
		image.getGraphics().drawImage(sourceImage, 0, 0, null);
		// Creates a sprite and adds it to the cache
		Sprite sprite = new Sprite(image);
		System.out.println("Sprite " + ref + " loaded!");
		// Puts the sprite and reference to it in the hashmap
		sprites.put(ref, sprite);
		return sprite;
	}

	/**
	 * This method is called if the image cannot be found, and the program is
	 * exited
	 * 
	 * @param message
	 */
	private void fail(String message) {
		System.err.println(message);
		System.exit(0);
	}

}
