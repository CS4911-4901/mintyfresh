package edu.gatech.cs4911.mintyfresh.io;

import android.content.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
     * The upstream database server connection to compare hashes against
     * and download images from if necessary.
     */
    private DBHandler handler;
    /**
     * The current application Context.
     */
    private Context context;

    /**
     * Creates a new ImageCache. The ImageCache will be populated
     * with image hashes and images corresponding to the values of
     * CacheLoader.LOCAL_IMAGE_PATH and CacheLoader.LOCAL_HASHFILE_PATH.
     *
     * @param handler The upstream database server connection.
     * @param context The current application Context.
     */
    public ImageCache(DBHandler handler, Context context) {
        cache = new ArrayList<>();
        this.handler = handler;
        this.context = context;

        populate();
    }

    /**
     * Updates the ImageCache by contacting the server for file hashes,
     * downloading files to replace old or missing images if necessary.
     *
     * @throws SQLException if there was a problem connecting to the database.
     * @throws IOException if there was a problem writing to a file.
     */
    public void update() throws SQLException, IOException {
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
            ImageDownloader.downloadToImageFile(handler, image, context);

            // And update the cache
            insert(image);
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
            if (!cache.contains(meta) || (cache.contains(meta)
                    && !cache.get(cache.indexOf(meta)).hashEquals(meta))) {
                // If cache node doesn't exist locally...
                // Or exists but hash mismatch...

                // We need to download from the server!
                refreshQueue.add(meta);
            } // Else, this node is up-to-date!
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
        if (cache.contains(node)) {
            // No no, this makes sense, promise!

            // The equals() method of FloorplanMeta does NOT compare hashes -
            // so this will replace a node that matches with a shallow equality
            // check and insert a node with an updated hash!
            cache.remove(node);
            cache.add(node);
        }
    }

    /**
     * Returns true if this ImageCache contains a cache node
     * for a provided building ID and floor.
     *
     * @param buildingId A String ID correponding to a building.
     * @param level The floor of the given building.
     * @return true if this ImageCache contains the cache node; false otherwise.
     */
    public boolean contains(String buildingId, int level) {
        return cache.contains(new FloorplanMeta(buildingId, level));
    }

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
        List<FloorplanMeta> localNodes = CacheLoader.getLocalNodes(context);
        // Get a map of local hashes we already have
        for (FloorplanMeta node : CacheLoader.loadHashes(context)) {
            // Ignore hashes for files that don't exist locally
            // e.g. If image was deleted somehow but hash wasn't removed
            if (localNodes.contains(node)) {
                insert(node);
            }
        }
    }
}
