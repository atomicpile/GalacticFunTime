import java.awt.Rectangle;
import java.util.Vector;

public class Quadtree {

	// How many objects a node will hold before it splits
	private final int MAX_OBJECTS = 10;
	// Deepest level the node will go
	private final int MAX_LEVELS = 5;
	// The current level node, 0 being the top
	private int level;
	// The list of objects in this quadtree
	private Vector<Entity> objects;
	// The retrieved list
	private Vector<Entity> retrieveList;
	// The bounds of this quadtree
	private Rectangle bounds;
	// The quadrants or branches of this quadtree
	private Quadtree[] nodes;

	/**
	 * Constructor
	 * 
	 * @param pLevel
	 * @param pBounds
	 */
	public Quadtree(int pLevel, Rectangle pBounds) {
		// sets the layer of this quadtree to the new layer
		level = pLevel;
		// the vector list of objects in this quadtree
		objects = new Vector<Entity>();
		retrieveList = new Vector<Entity>();
		// the rectangle defining this quadtree
		bounds = pBounds;
		nodes = new Quadtree[4];
	}

	public void clear() {
		objects.clear();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	/**
	 * Splits the node into 4 subnodes
	 * 
	 */
	public void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();
		// Creates four new quadtrees in this quadtree
		nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y,
				subWidth, subHeight));
		nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth,
				subHeight));
		nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight,
				subWidth, subHeight));
		nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y
				+ subHeight, subWidth, subHeight));
	}

	/**
	 * Determines which node the object belongs to. -1 means object cannot
	 * completely fit within a child node and is part of the parent node.
	 * 
	 * @param ent
	 * @return
	 */
	private int getIndex(Entity ent) {
		int index = -1;
		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
		// Object can completely fit within the top quadrants
		boolean topQuadrant = (ent.getY() < horizontalMidpoint && ent
				.getY() + ent.getHeight() < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (ent.getY() > horizontalMidpoint);
		// Object can completely fit within the left quadrants
		if (ent.getX() < verticalMidpoint
				&& ent.getX() + ent.getWidth() < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (ent.getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}
		return index;
	}

	public void insert(Entity ent) {
		if (nodes[0] != null) {
			int index = getIndex(ent);

			if (index != -1) {
				nodes[index].insert(ent);
				return;
			}
		}
		objects.add(ent);

		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}
			for (int i = 0; i < objects.size(); i++) {
				int index = getIndex(objects.get(i));
				if (index != -1) {
					nodes[index].insert(objects.remove(i));
				}
			}
		}
	}

	public Vector<Entity> retrieve(Entity ent) {
		retrieveList.clear();
		int index = getIndex(ent);
		if (index != -1 && nodes[0] != null) {
			retrieveList = nodes[index].retrieve(ent);
		}
		retrieveList.addAll(objects);
		return retrieveList;
	}
}
