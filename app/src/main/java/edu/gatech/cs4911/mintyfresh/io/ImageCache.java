package edu.gatech.cs4911.mintyfresh.io;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;

/**
 * An ImageCache maintains the mappings of image files to hashes.
 */
public class ImageCache {
    /**
     * A list of floorplan image metadata.
     */
    private List<FloorplanMeta> cache;

    /**
     * A two-level map. The traversal is as follows: <br>
     * <b> Building ID -> Floor -> Cache node </b>
     * <br><br>
     * For example, to retrieve a cache node corresponding
     * to <b>building TST, floor 1</b>: <br>
     * <b>K1 = "TST", K2 = 1, V = ("TST", 1) with hash [hash]</b>
     */
    private Map<String, Map<Integer, FloorplanMeta>> cache;
    /**
     * The upstream database server connection to compare hashes against
     * and download images from if necessary.
     */
    private DBHandler handler;

    /**
     * Creates a new ImageCache. The ImageCache will be populated
     * with image hashes and images corresponding to the values of
     * CacheLoader.LOCAL_IMAGE_PATH and CacheLoader.LOCAL_HASHFILE_PATH.
     *
     * @param handler The upstream database server connection.
     */
    public ImageCache(DBHandler handler) {
        cache = new ArrayList<>();

        this.handler = handler;
        populate();
    }

    /**
     * Updates the ImageCache by contacting the server for file hashes,
     * downloading files to replace old or missing images if necessary.
     */
    public void update() {
        // Generates a list of invalid or missing images...
        Queue<FloorplanMeta> downloadQueue;
        try {
            downloadQueue = generateDownloadQueue();
        } catch (SQLException e) {
            // We can't do anything without upstream results!
            return;
        }

        // And download them!
        for (FloorplanMeta image : downloadQueue) {
            // And update the cache
            if (cache.containsKey(image))
        }
    }

    /**
     * Generates a queue of image metadata corresponding to
     * images that need to be downloaded from the upstream server,
     * based on the local contents of this ImageCache.
     *
     * @return A queue of image metadata to be downloaded.
     * @throws SQLException if there are no results from the server (e.g. no images)
     */
    private Queue<FloorplanMeta> generateDownloadQueue() throws SQLException {
        /**
         * Stores a list of metadata retrieved from the database.
         */
        List<FloorplanMeta> upstreamMetadata = DBQuery.getFloorplanMetadata(handler);
        /**
         * Stores a list of metadata corresponding to images that
         * must be downloaded from the server.
         */
        Queue<FloorplanMeta> refreshQueue = new LinkedList<>();

        for (FloorplanMeta meta : upstreamMetadata) {
            if (!cache.contains(meta)) {
                // If doesn't exist, or hash but nonexistant file,
                if ()
                // or exists but hash mismatch...

                // We need to download from the server!
                refreshQueue.add(meta);
            }
        }

        return refreshQueue;
    }

    /**
     * Inserts a new node of metadata into this ImageCache.
     *
     * If there is a node that already exists with the same
     * building ID and floor but with an outdated hash,
     * it will be replaced.
     *
     * @param node The node to insert into this ImageCache.
     */
    private void insert(FloorplanMeta node) {
        if (cache.get(node.getId()) == null) {
            // Building doesn't exist in cache, add it!
            cache.put(node.getId(), new HashMap<Integer, FloorplanMeta>());
        }

        cache.get(node.getId()).put(node.getLevel(), node);
    }

    /**
     * Returns true if this ImageCache contains a cache node
     * for a provided building ID and floor.
     *
     * @param buildingId A String ID correponding to a building.
     * @param level The
     * @return
     */
    public boolean contains(String buildingId, int level) {

    }


    // TODO: implement stuff

    public int size() {
        return cache.size();
    }

    /**
     * Populates the image cache with image hashes and file names
     * corresponding to the values of CacheLoader.LOCAL_IMAGE_PATH
     * and CacheLoader.LOCAL_HASHFILE_PATH.
     */
    private void populate() {
        // Get a list of local files that we already have
        List<FloorplanMeta> localNodes = CacheLoader.getLocalNodes();
        // Get a map of local hashes we already have
        for (FloorplanMeta node : CacheLoader.loadHashes()) {
            // Ignore hashes for files that don't exist locally
            // e.g. If image was deleted somehow but hash wasn't removed
            if (localNodes.contains(node)) {
                insert(node);
            }
        }
    }
}
