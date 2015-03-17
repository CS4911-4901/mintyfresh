package edu.gatech.cs4911.mintyfresh.test;

import android.app.Activity;
import android.test.InstrumentationTestCase;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;
import edu.gatech.cs4911.mintyfresh.io.ImageCache;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class ImageCacheTest extends InstrumentationTestCase {
    DBHandler handler;
    Activity activity;
    ImageCache cache;
    FloorplanMeta testMeta;

    public void setUp() throws Exception {
        super.setUp();

        handler = new DBHandler(STEAKSCORP_READ_ONLY);
        activity = new Activity();
        testMeta = new FloorplanMeta("CUL", 1);
    }

    public void tearDown() throws Exception { }

    public void testInstantiation() throws Exception {
        cache = new ImageCache(handler, activity.getApplicationContext());
        assertNotNull(cache);
    }

    public void testUpdate() throws Exception {
        cache.update();

        assertTrue(cache.size() > 0);
        assertTrue(cache.contains(testMeta));
    }
}