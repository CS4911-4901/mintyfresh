package edu.gatech.cs4911.mintyfresh.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.List;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;
import edu.gatech.cs4911.mintyfresh.io.CacheLoader;
import edu.gatech.cs4911.mintyfresh.io.ImageCache;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class ImageCacheTest extends AndroidTestCase {
    Context context;
    DBHandler handler;
    ImageCache cache;
    FloorplanMeta testMeta;

    public void setUp() throws Exception {
        super.setUp();

        context = getContext();
        handler = new DBHandler(STEAKSCORP_READ_ONLY);
        testMeta = new FloorplanMeta("CUL", 1);
    }

    public void tearDown() throws Exception {

    }

    public void testCacheLoaderLoadImages() throws Exception {
        assertNotNull(CacheLoader.loadImages(context));
    }

    public void testCacheLoaderLoadMeta() throws Exception {
        assertNotNull(CacheLoader.getLocalNodes(context));
    }

    public void testCacheLoaderLoadHash() throws Exception {
        CacheLoader.loadHashes(context);
    }

    public void testInstantiation() throws Exception {
        cache = new ImageCache(handler, context);
        assertNotNull(cache);
    }

    public void testUpdate() throws Exception {
        cache = new ImageCache(handler, context);
        cache.update();

        assertTrue(cache.size() > 0);
        assertTrue(cache.contains(testMeta));
    }
}