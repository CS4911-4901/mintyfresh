package edu.gatech.cs4911.mintyfresh.io;

import android.content.Context;

import com.caverock.androidsvg.SVG;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;
import edu.gatech.cs4911.mintyfresh.exception.DisplayFloorplanException;
import edu.gatech.cs4911.mintyfresh.exception.NoDbResultException;

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
     * The extension images are stored as.
     */
    public static final String IMAGE_EXTENSION = ".svg";
    /**
     * The filename of the local hash file.
     */
    public static final String HASH_FILENAME = "hashfile.txt";

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
     * Gets a floorplan image, as an SVG object, given floorplan metadata.
     *
     * @param cacheNode The node to access an image.
     * @return A floorplan image, as an SVG.
     * @throws DisplayFloorplanException if there was a problem accessing/decoding the image.
     */
    public SVG get(FloorplanMeta cacheNode) throws DisplayFloorplanException {
        SVG result;

        try {
            if (!contains(cacheNode) || needsUpdate(cacheNode)) {
                update(cacheNode);
            }

            String filename = cacheNode.getId() + "_" + cacheNode.getLevel() + IMAGE_EXTENSION;
            // Explicitly declared so we can close this cleanly after we're done
            FileInputStream inputStream = context.openFileInput(filename);
            result = SVG.getFromInputStream(inputStream);

            // We're done!
            inputStream.close();
        } catch (Exception e) {
            throw new DisplayFloorplanException();
        }

        return result;
    }

    /**
     * Gets a floorplan image, as an SVG object, given a building ID and floor number.
     *
     * @param buildingId A provided building, identified by a String ID.
     * @param level The floor of the building to return an image for.
     * @return A floorplan image, as an SVG.
     * @throws DisplayFloorplanException if there was a problem accessing/decoding the image.
     */
    public SVG get(String buildingId, int level) throws DisplayFloorplanException {
        return get(new FloorplanMeta(buildingId, level));
    }

    /**
     * Gets a floorplan image, as an SVG object, given a Building object and floor number.
     *
     * @param building A provided building.
     * @param level The floor of the building to return an image for.
     * @return A floorplan image, as an SVG.
     * @throws DisplayFloorplanException if there was a problem accessing/decoding the image.
     */
    public SVG get(Building building, int level) throws DisplayFloorplanException {
        return get(new FloorplanMeta(building.getId(), level));
    }

    /**
     * Returns a FloorplanMeta object from this cache, given a building and level.
     * Performs a database query and downloads the image and metadata from the database
     * if necessary.
     *
     * @param buildingId A provided building, identified by a String ID.
     * @param level The floor of the building to return an image for.
     * @return Returns a FloorplanMeta object corresponding to the given building and leve.
     * @throws NoDbResultException if a database query was required, but no result was returned.
     */
    public FloorplanMeta getMeta(String buildingId, int level) throws NoDbResultException {
        FloorplanMeta tempNode = new FloorplanMeta(buildingId, level);
        try {
            if (!contains(tempNode)) {
                get(tempNode);
            }

            for (FloorplanMeta cacheNode : cache) {
                if (cacheNode.equals(tempNode)) {
                    return cacheNode;
                }
            }
        } catch (Exception e) {
            throw new NoDbResultException();
        }

        // This should not happen
        return null;
    }

    /**
     * Returns a FloorplanMeta object from this cache, given a building and level.
     * Performs a database query and downloads the image and metadata from the database
     * if necessary.
     *
     * @param building A provided building.
     * @param level The floor of the building to return an image for.
     * @return Returns a FloorplanMeta object corresponding to the given building and leve.
     * @throws NoDbResultException if a database query was required, but no result was returned.
     */
    public FloorplanMeta getMeta(Building building, int level) throws NoDbResultException{
        return getMeta(building.getId(), level);
    }

    /**
     * Updates the ImageCache by contacting the server for the image
     * corresponding to the metadata object requested.
     *
     * @throws SQLException if there was a problem connecting to the database.
     * @throws IOException if there was a problem writing to a file.
     */
    private void update(FloorplanMeta meta) throws SQLException, IOException {
        ImageDownloader.downloadToImageFile(handler, meta, context);
        try {
            meta = getMeta(meta.getId(), meta.getLevel());
        } catch (NoDbResultException e) {
            throw new SQLException();
        }

        insert(meta);
        updateHashFile();
    }

    /**
     * Checks a local metadata object against the upstream database to
     * see if there is a mismatch between local and upstream metadata.
     *
     * @param meta The node to access the images.
     * @return true if there is a cache mismatch; false otherwise.
     * @throws SQLException if there are no results from the server (e.g. no images)
     */
    private boolean needsUpdate(FloorplanMeta meta) throws SQLException {
        FloorplanMeta upstreamMeta = DBQuery.getFloorplanMetadata(
                handler, meta.getId(), meta.getLevel());

        if (meta.equals(upstreamMeta) && meta.hashEquals(upstreamMeta)) {
            return false;
        } return true; // If there is a hash mismatch, we need to redownload!
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
        }

        cache.add(node);
    }

    /**
     * Writes a new hash file at the data directory of this ImageCache's context,
     * overwriting any previous hash file version. The hash file will be written based
     * on the FloorplanMeta items currently in this ImageCache. <br><br>
     *
     * The format of each hash file line is as follows: <br>
     * <b>buildingID:floor:hash:native_width:native_height</b> <br><br>
     *
     * For example, this FloorplanMeta: <br>
     * <b>("TST", 1) with hash 1234f</b> <br>
     *
     * ...will be written as this hash file line: <br>
     * <b>"TST:1:1234f:x:y"</b>
     *
     * @throws IOException if we were prevented from writing to the context's data directory.
     */
    private void updateHashFile() throws IOException {
        FileOutputStream outputHash = context
                .openFileOutput(ImageCache.HASH_FILENAME, Context.MODE_PRIVATE);

        for (FloorplanMeta meta : cache) {
            outputHash.write(
                    (meta.getId() + ":" + meta.getLevel() + ":" + meta.getHash() + ":" +
                            meta.getNativeWidth() + ":" + meta.getNativeHeight() + "\n")
                            .getBytes()
            );
        }

        outputHash.close();
    }

    /**
     * Returns true if this ImageCache contains the given cache node.
     *
     * @param meta The given cache node.
     * @return true if this ImageCache contains the cache node; false otherwise.
     */
    public boolean contains(FloorplanMeta meta) {
        return cache.contains(meta);
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
        return contains(new FloorplanMeta(buildingId, level));
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
