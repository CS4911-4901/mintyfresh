package edu.gatech.cs4911.mintyfresh.io;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;

/**
 * A CacheLoader handles the loading of images and hashes
 * from local storage into an ImageCache.
 */
public class CacheLoader {
    /**
     * Returns a list of image file names in the data directory of the
     * provided application context. Returns an empty list if there
     * are no files in the context provided.
     *
     * @param context A given application context.
     * @return A list of image file names in the given path.
     */
    public static List<String> loadImages(Context context) {
        List<String> output = new ArrayList<>();
        File[] folder = context.getFilesDir().listFiles();
        for (File image : folder) {
            if (image.isFile() && image.getName().contains(ImageCache.IMAGE_EXTENSION)) {
                output.add(image.getName());
            }
        }

        return output;
    }

    /**
     * Returns a list of FloorplanMetas constructed from the files
     * found at the data directory of the provided application context.
     * Returns an empty list if there are no files in the context provided.
     *
     * @param context A given application context.
     * @return A list of FloorplanMetas in CacheLoader.LOCAL_IMAGE_PATH.
     */
    public static List<FloorplanMeta> getLocalNodes(Context context) {
        List<FloorplanMeta> localNodes = new ArrayList<>();
        for (String filename : loadImages(context)) {
            // File name format: <building>_<floor>.<ext>
            // Below splits into FloorplanMeta(<building>, int(<floor>))
            localNodes.add(new FloorplanMeta(filename.split("\\.")[0].split("_")[0],
                    Integer.parseInt(filename.split("\\.")[0].split("_")[1])));
        }

        return localNodes;
    }

    /**
     * Returns a list of image metadata, loaded from a local
     * hash file found in the data directory of the provided
     * application context. Returns an empty list if the hash
     * file is empty or the hash file does not exist.
     *
     * @param context A given application context.
     * @return A list of image metadata.
     */
    public static List<FloorplanMeta> loadHashes(Context context) {
        /**
         * Format of file: buildingID:floor:hash
         */
        List<FloorplanMeta> hashNodes = new ArrayList<>();
        try {
            File path = new File(context.getFilesDir(), ImageCache.HASH_FILENAME);

            // Read file line-by-line and populate map
            BufferedReader reader = new BufferedReader(new FileReader(path));
            for (String line; (line = reader.readLine()) != null;) {
                // "stu:1:hash123" -> "(STU, 1)" with hash: hash123
                hashNodes.add(new FloorplanMeta(line.split(":")[0],
                        Integer.parseInt(line.split(":")[1]), line.split(":")[2]));
            }

            // We're done with the file!
            reader.close();
        } catch (IOException e) {
            // If the file doesn't exist, abort!
            return hashNodes;
        }

        return hashNodes;
    }
}
