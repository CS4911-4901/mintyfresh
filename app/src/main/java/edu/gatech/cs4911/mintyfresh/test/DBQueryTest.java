package edu.gatech.cs4911.mintyfresh.test;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.*;

import static edu.gatech.cs4911.mintyfresh.db.DBQuery.*;
import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class DBQueryTest extends InstrumentationTestCase {
    DBHandler handler;

    public void setUp() throws Exception {
        super.setUp();
        handler = new DBHandler(STEAKSCORP_READ_ONLY);
    }
    public void tearDown() throws Exception { }

    public void testGetBuildings() throws Exception {
        List<Building> result = getBuildings(handler);
        assertNotNull(result);

        // And check each item in result
        for (Building item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetLevelsInBuilding() throws Exception {
        List<Integer> result = getLevelsInBuilding(handler, "CUL");
        assertNotNull(result);

        // And check each item in result
        for (Integer num : result) {
            assertNotNull(num);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenities() throws Exception {
        List<Amenity> result = getAmenities(handler);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenities1() throws Exception {
        List<Amenity> result = getAmenities(handler, "CUL", "Bathroom", "male");
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenities2() throws Exception {
        String[] attributeList = new String[2];
        attributeList[0] = "male";
        attributeList[1] = "handicapped";
        List<Amenity> result = getAmenities(handler, "CUL", "Bathroom", attributeList);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenities3() throws Exception {
        List<Amenity> result = getAmenities(handler, "CUL", 1, "Bathroom", "male");
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenities4() throws Exception {
        List<Amenity> result = getAmenities(handler, "CUL", "Bathroom");
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenities5() throws Exception {
        List<Amenity> result = getAmenities(handler, "CUL", "Bathroom", 1);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesByBuildingId() throws Exception {
        List<Amenity> result = getAmenitiesByBuildingId(handler, "CUL");
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesByType() throws Exception {
        List<Amenity> result = getAmenitiesByType(handler, "Bathroom");
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesByTypeAndAttribute() throws Exception {
        List<Amenity> result = getAmenitiesByTypeAndAttribute(handler, "Bathroom", "male");
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesByTypeAndAttributes() throws Exception {
        String[] attributeList = new String[2];
        attributeList[0] = "male";
        attributeList[1] = "handicapped";
        List<Amenity> result = getAmenitiesByTypeAndAttributes(handler, "Bathroom", attributeList);
        assertNotNull(result);

        // And check each item in result
        for (Amenity item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenityTypesInBuilding() throws Exception {
        List<String> result = getAmenityTypesInBuilding(handler, "TST");
        assertNotNull(result);

        // And check each item in result
        for (String item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetFloorplanMetadata1() throws Exception {
        List<FloorplanMeta> result = getFloorplanMetadata(handler);
        assertNotNull(result);

        // And check each item in result
        for (FloorplanMeta item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetFloorplanMetadata2() throws Exception {
        FloorplanMeta result = getFloorplanMetadata(handler, "CUL", 1);
        assertNotNull(result);
    }

    public void testGetFloorplanHashes() throws Exception {
        List<String> result = getFloorplanHashes(handler);
        assertNotNull(result);

        // And check each item in result
        for (String item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetFloorplan() throws Exception {
        InputStream result = getFloorplan(handler, "CUL", 1);
        assertNotNull(result);
    }

    public void testGetFloorplans() throws Exception {
        List<InputStream> result = getFloorplans(handler, "CUL");
        assertNotNull(result);

        // And check each item in result
        for (InputStream item : result) {
            assertNotNull(item);
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetDistinctAmenityAttributes() throws Exception {
        Map<String, String> result = getDistinctAmenityAttributes(handler, "Bathroom");
        Set<String> keyset = result.keySet();
        assertNotNull(result);

        // And check each item in result
        for (String item : keyset) {
            assertNotNull(item);
            assertNotNull(result.get(item));
        }

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }
}