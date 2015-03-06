package edu.gatech.cs4911.mintyfresh.test;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.util.List;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.*;

import static edu.gatech.cs4911.mintyfresh.db.DBQuery.*;
import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

/**
 * This class repeats the same large query on DBQuery.
 *
 * If the cache works correctly, subsequent tests after
 * testGetAmenitiesTry1() should be faster.
 *
 * Alternatively, if DBQueryTest's testGetAmenities1()
 * executes first, testGetAmenitiesTry1() should be faster.
 */
public class DBQueryCacheTest extends InstrumentationTestCase {
    DBHandler handler;

    public void setUp() throws Exception {
        super.setUp();
        handler = new DBHandler(STEAKSCORP_READ_ONLY);
    }
    public void tearDown() throws Exception { }

    public void testGetAmenitiesTry1() throws Exception {
        List<Amenity> result = getAmenities(handler);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesTry2() throws Exception {
        List<Amenity> result = getAmenities(handler);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesTry3() throws Exception {
        List<Amenity> result = getAmenities(handler);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesTry4() throws Exception {
        List<Amenity> result = getAmenities(handler);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesTry5() throws Exception {
        List<Amenity> result = getAmenities(handler);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }
}