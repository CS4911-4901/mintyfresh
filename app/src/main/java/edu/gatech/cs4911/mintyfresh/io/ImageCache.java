package edu.gatech.cs4911.mintyfresh.io;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        List<String> upstreamHashes;

        // TODO: finish implementing - if all we care about is hash
        // and hash identifies blob in database, do we even need to
        // store file name...or at least have to store as a key?
        try {
            upstreamHashes = DBQuery.getFloorplanHashes(handler);
        } catch (SQLException e) {
            // We can't do anything if there are no hashes on server
            return;
        }

        for (String hash : upstreamHashes) {
            // TODO: do something
        }

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
                cache.add(node);
            }
        }
    }
}
