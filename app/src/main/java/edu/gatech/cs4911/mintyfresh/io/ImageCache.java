package edu.gatech.cs4911.mintyfresh.io;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.exception.NoDbResultException;

/**
 * An ImageCache maintains the mappings of image files to hashes.
 */
public class ImageCache {
    /**
     * A map of image file names to file hashes.
     */
    private Map<String, String> cache;
    /**
     * The upstream database server connection to compare hashes against
     * and download images from if necessary.
     */
    private DBHandler handler;

    /**
     * Creates a new ImageCache. The ImageCache will be populated
     * with images and image hashes corresponding to the values of
     * CacheLoader.LOCAL_IMAGE_PATH and CacheLoader.LOCAL_HASHFILE_PATH.
     *
     * @param handler The upstream database server connection.
     */
    public ImageCache(DBHandler handler) {
        cache = new HashMap<>();

        this.handler = handler;
        populate();
    }

    /**
     * Updates the ImageCache by contacting the server for file hashes,
     * downloading files to replace old or missing images if necessary.
     */
    public void update() {
        List<String> upstreamHashes;
        List<String> localHashes = null;

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

        }

    }

    /**
     * Adds or replaces a new item in the image cache.
     *
     * @param filename The file name of the image.
     * @param hash The file hash of the image.
     */
    public void add(String filename, String hash) {
        cache.put(filename, hash);
    }

    // TODO: implement stuff

    public int size() {
        return cache.size();
    }

    /**
     * Populates the image cache with images and image hashes
     * corresponding to the values of CacheLoader.LOCAL_IMAGE_PATH
     * and CacheLoader.LOCAL_HASHFILE_PATH.
     */
    private void populate() {
        List<String> filenames = CacheLoader.loadImages();
        Map<String, String> hashes = CacheLoader.loadHashes();
        for (String filename : filenames) {
            // Below might result in null values - handled next!
            cache.put(filename, hashes.get(filename));
        }
    }
}
