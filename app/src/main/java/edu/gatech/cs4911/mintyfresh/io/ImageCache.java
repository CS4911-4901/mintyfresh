package edu.gatech.cs4911.mintyfresh.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An ImageCache maintains the mappings of image files to hashes.
 */
public class ImageCache {
    /**
     * A map of image file names to file hashes.
     */
    private Map<String, String> cache;

    /**
     * Creates a new ImageCache. The ImageCache will be populated
     * with images and image hashes corresponding to the values of
     * CacheLoader.LOCAL_IMAGE_PATH and CacheLoader.LOCAL_HASHFILE_PATH.
     */
    public ImageCache() {
        cache = new HashMap<>();
        populate();
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

    /**
     * Populates the image cache with images and image hashes
     * corresponding to the values of CacheLoader.LOCAL_IMAGE_PATH
     * and CacheLoader.LOCAL_HASHFILE_PATH.
     */
    private void populate() {
        List<String> filenames = CacheLoader.loadImages();
        List<String> hashes = CacheLoader.loadHashes();
        for (int i = 0; i < filenames.size(); i++) {
            cache.put(filenames.get(i), hashes.get(i));

            // TODO: handle if filenames and hashes have size mismatch
            // TODO: how to guarantee they will be in right order?
        }
    }
}
