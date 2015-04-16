package edu.gatech.cs4911.mintyfresh.test;

import android.content.Context;
import android.test.AndroidTestCase;

import com.caverock.androidsvg.SVG;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
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

    public void testGet1() throws Exception {
        cache = new ImageCache(handler, context);

        assertNotNull(cache.get(testMeta));
    }

    public void testGet2() throws Exception {
        cache = new ImageCache(handler, context);

        assertNotNull(cache.get(testMeta.getId(), testMeta.getLevel()));
    }

    public void testGet3() throws Exception {
        cache = new ImageCache(handler, context);

        assertNotNull(cache.get(
                new Building("CUL", "Clough Undergraduate Learning Commons", 0.0, 0.0),
                testMeta.getLevel()));
    }

    public void testGetMeta1() throws Exception {
        cache = new ImageCache(handler, context);

        assertNotNull(cache.getMeta("STU", 1));
    }

    public void testGetMeta2() throws Exception {
        cache = new ImageCache(handler, context);

        assertNotNull(cache.getMeta(
                new Building("CUL", "Clough Undergraduate Learning Commons", 0.0, 0.0), 1));
    }

    public void testAddMetaCorrectly() throws Exception {
        cache = new ImageCache(handler, context);
        FloorplanMeta testMeta = new FloorplanMeta("CUL", 1);
        FloorplanMeta actualMeta = DBQuery.getFloorplanMetadata(handler, "CUL", 1);
        cache.get(testMeta);

        assertTrue(testMeta.equals(actualMeta));
        assertTrue(cache.contains(testMeta));
        assertTrue(cache.contains(actualMeta));
    }

    public void testGetMetaFullLogic() throws Exception {
        cache = new ImageCache(handler, context);

        Building testBuilding = new Building("CUL", "Clough Undergraduate Learning Commons",
                0.0, 0.0);
        FloorplanMeta testMeta = DBQuery.getFloorplanMetadata(handler, testBuilding.getId(), 1);
        SVG resultSvg = cache.get(testMeta);
        FloorplanMeta cacheMeta = cache.getMeta(testBuilding, 1);

        assertNotNull(testBuilding);
        assertNotNull(testMeta);
        assertNotNull(resultSvg);
        assertNotNull(cacheMeta);
    }

    public void testGetMetaNonZeroNatives() throws Exception {
        FloorplanMeta meta = cache.getMeta("STU", 1);
        assertFalse(meta.getNativeWidth() == 0);
        assertFalse(meta.getNativeHeight() == 0);
    }
}