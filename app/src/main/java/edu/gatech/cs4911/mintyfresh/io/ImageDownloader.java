package edu.gatech.cs4911.mintyfresh.io;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;

/**
 * An ImageDownloader handles the download of files from the server
 * and population of the cache.
 */
public class ImageDownloader {
    /**
     * The size of the output file buffer, in bytes.
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * Downloads an image from the server into a file at the data directory of the
     * provided application context. The image to download is given by the building
     * ID and floor in the provided FloorplanMeta object. <br><br>
     *
     * Note that this will likely be the <b>slowest operation in the app</b>, and
     * methods that need images should consult an ImageCache first!
     *
     * @param handler The database connection to use.
     * @param meta The image to download from the server.
     * @param context A given application context.
     * @throws SQLException if the database query failed.
     * @throws IOException if there was a problem writing to the path.
     */
    public static void downloadToImageFile(DBHandler handler, FloorplanMeta meta, Context context)
            throws SQLException, IOException {
        int bytesOutput;
        byte[] buffer = new byte[BUFFER_SIZE];
        String filename =  meta.getId() + "_" + meta.getLevel() + ImageCache.IMAGE_EXTENSION;
        InputStream blob = DBQuery.getFloorplan(handler, meta.getId(), meta.getLevel());

        // The Android way of writing to a File!
        FileOutputStream output = context.openFileOutput(filename, Context.MODE_PRIVATE);

        // Write to output stream, BUFFER_SIZE bytes at a time
        while ((bytesOutput = blob.read(buffer)) != -1) {
            output.write(buffer, 0, bytesOutput);
        }

        // We're done!
        blob.close();
        output.close();
    }
}
