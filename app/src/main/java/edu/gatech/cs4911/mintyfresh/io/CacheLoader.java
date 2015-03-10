package edu.gatech.cs4911.mintyfresh.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Returns a list of image file names in the given path.
     * Returns null if there are no files at the path provided.
     *
     * @param path A given path to look for images.
     * @return A list of image file names in the given path.
     */
    public static List<String> loadImages(String path) {
        List<String> output = new ArrayList<>();
        File[] folder = new File(path).listFiles();
        for (File image : folder) {
            if (image.isFile()) {
                output.add(image.getName());
            }
        }

        // Return null if there's nothing there!
        return (output.size() > 0) ? output : null;
    }

    /**
     * Returns a list of image file names in the default path.
     * Returns null if there are no files at the path provided.
     *
     * @return A list of image file names in CacheLoader.LOCAL_IMAGE_PATH.
     */
    public static List<String> loadImages() {
        return loadImages(LOCAL_IMAGE_PATH);
    }

    /**
     * Returns a map of image file names to hashes, loaded
     * from a local hash file. Returns null if the hash file
     * does not exist or is empty.
     *
     * @param hashFile The path to a local hash file.
     * @return A map of image file names to hashes.
     */
    public static Map<String, String> loadHashes(String hashFile) {
        /**
         * Format of file: filename:hash
         */
        Map<String, String> fileHashes = new HashMap<>();
        try {
            // Read file line-by-line and populate map
            BufferedReader reader = new BufferedReader(new FileReader(hashFile));
            for (String line; (line = reader.readLine()) != null;) {
                // "image.svg:hash123" -> <K="image.svg": V="hash123">
                fileHashes.put(line.split(":")[0], line.split(":")[1]);
            }

            // We're done with the file!
            reader.close();
        } catch (IOException e) {
            return null;
        }

        // Return null if there was nothing useful in the file!
        return (fileHashes.size() > 0) ? fileHashes : null;
    }

    /**
     * Returns a map of image file names to hashes, loaded
     * from the default hash file path.Returns null if the
     * hash file does not exist or is empty.
     *
     * @return The map contained in CacheLoader.LOCAL_HASHFILE_PATH.
     */
    public static Map<String, String> loadHashes() {
        return loadHashes(LOCAL_HASHFILE_PATH);
    }
}
