package edu.gatech.cs4911.mintyfresh.io;

import java.util.List;

/**
 * A CacheLoader handles the loading of images and hashes
 * from local storage into an ImageCache.
 */
public class CacheLoader {
    /**
     * The default path to look for local images to load into the cache.
     */
    public static final String LOCAL_IMAGE_PATH = "";
    /**
     * The default path to look for a file with image hashes corresponding
     * to images located at LOCAL_IMAGE_PATH.
     */
    public static final String LOCAL_HASHFILE_PATH = "";

    // TODO: implement
    public static List<String> loadImages(String path) { return null; }
    public static List<String> loadImages() {
        return loadImages(LOCAL_IMAGE_PATH);
    }

    // TODO: implement
    public static List<String> loadHashes(String hashFile) { return null; }
    public static List<String> loadHashes() {
        return loadHashes(LOCAL_HASHFILE_PATH);
    }
}
