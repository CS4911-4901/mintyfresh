package edu.gatech.cs4911.mintyfresh.db.queryresponse;

/**
 * A FloorplanMeta is a node of image metadata that holds
 * information about a building ID, level, and hash
 * that corresponds to a floorplan image.
 */
public class FloorplanMeta {
    /**
     * An ID corresponding to a Building object.
     */
    private String id;
    /**
     * The floor of the building this node points to.
     */
    private int level;
    /**
     * The hash of the image this node refers to.
     */
    private String hash;
    /**
     * The native width of the image file (SVG).
     */
    private int nativeWidth;
    /**
     * The native height of the image file (SVG).
     */
    private int nativeHeight;

    /**
     * Constructs a new FloorplanMeta with a provided Building ID,
     * Building level, file hash, native width, and native height.
     *
     * @param id An ID corresponding to a Building object.
     * @param level The floor of the building this node points to.
     * @param hash The hash of the image this node refers to.
     * @param nativeWidth The native width of the image file (SVG).
     * @param nativeHeight The native height of the image file (SVG).
     */
    public FloorplanMeta(String id, int level, String hash,
                         int nativeWidth, int nativeHeight) {
        this.id = id;
        this.level = level;
        this.hash = hash;
        this.nativeWidth = nativeWidth;
        this.nativeHeight = nativeHeight;
    }

    /**
     * Constructs a new FloorplanMeta with a provided Building ID,
     * Building level, and file hash.
     *
     * @param id An ID corresponding to a Building object.
     * @param level The floor of the building this node points to.
     * @param hash The hash of the image this node refers to.
     */
    public FloorplanMeta(String id, int level, String hash) {
        this(id, level, hash, 0, 0);
    }

    /**
     * Constructs a new FloorplanMeta with a provided Building ID
     * and Building level, with a default null hash. This constructor
     * is useful for loading metadata directly from a filename.
     *
     * @param id An ID corresponding to a Building object.
     * @param level The floor of the building this node points to.
     */
    public FloorplanMeta(String id, int level) {
        this(id, level, null);
    }

    /**
     * Returns true if a given FloorplanMeta object corresponds to the same
     * image as this one, identified by their image hashes.
     *
     * @param meta A given FloorplanMeta object.
     * @return true if the object contains the same image hash; else false.
     */
    public boolean hashEquals(FloorplanMeta meta) {
        return meta.getHash().equals(hash);
    }

    /**
     * Performs a <i>shallow equality check</i> on two FloorplanMeta nodes.
     * They will be equal if they have the same ID and level. The hash will
     * NOT be checked. To do a <i>full equality check</i>, combine with
     * FloorplanMeta.hashEquals().
     *
     * @param obj A given FloorplanMeta object.
     * @return true if the object corresponds to the same building and floor; else false.
     */
    public boolean equals(Object obj) {
        return (obj instanceof FloorplanMeta)
                && ((FloorplanMeta) obj).getId().equals(id)
                && ((FloorplanMeta) obj).getLevel() == level;
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
     * Returns this node's hash.
     *
     * @return This node's hash.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Returns the native width of the image file. If the returned
     * value is 0, there is no stored native width for this image.
     *
     * @return The native width of the image file.
     */
    public int getNativeWidth() {
        return nativeWidth;
    }

    /**
     * Returns the native height of the image file. If the returned
     * value is 0, there is no stored native width for this image.
     *
     * @return The native width of the image file.
     */
    public int getNativeHeight() {
        return nativeHeight;
    }

    /**
     * Returns a String representation of this FloorplanMeta. <br>
     * For example, a node for building TST, floor 2 will be represented
     * as "(TST, 2) with hash [hash]".
     *
     * @return A String representation of this FloorplanMeta.
     */
    public String toString() {
        return "(" + id + ", " + level + ") with hash " + hash;
    }
}
