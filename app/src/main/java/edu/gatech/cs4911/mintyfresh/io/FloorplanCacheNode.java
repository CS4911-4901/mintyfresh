package edu.gatech.cs4911.mintyfresh.io;

/**
 * A FloorplanCacheNode is a node in an ImageCache that holds
 * information about a building ID and level that corresponds
 * to a floorplan image.
 */
public class FloorplanCacheNode {
    /**
     * An ID corresponding to a Building object.
     */
    private String id;
    /**
     * The floor of the building this node points to.
     */
    private int level;

    /**
     * Constructs a new FloorplanCacheNode with a provided Building ID
     * and Building level.
     *
     * @param id An ID corresponding to a Building object.
     * @param level The floor of the building this node points to.
     */
    public FloorplanCacheNode(String id, int level) {
        this.id = id;
        this.level = level;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FloorplanCacheNode) {
            if (((FloorplanCacheNode) obj).getId().equals(id)
                && ((FloorplanCacheNode) obj).getLevel() == level) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the Building ID.
     *
     * @return The Building ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the Building level.
     *
     * @return The Building level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns a String representation of this FloorplanCacheNode. <br>
     * For example, a node for building TST, floor 2 will be represented
     * as "(TST, 2)".
     *
     * @return A String representation of this FloorplanCacheNode.
     */
    public String toString() {
        return "(" + id + ", " + level + ")";
    }
}
