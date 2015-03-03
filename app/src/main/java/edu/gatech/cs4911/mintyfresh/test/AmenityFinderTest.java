package edu.gatech.cs4911.mintyfresh.test;

import android.location.Location;
import android.test.InstrumentationTestCase;


import java.util.List;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.AmenityFinder;
import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.router.RelativeAmenity;
import edu.gatech.cs4911.mintyfresh.router.RelativeBuilding;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class AmenityFinderTest extends InstrumentationTestCase {
    AmenityFinder finder;
    Location curLocation;
    Building building;

    public void setUp() throws Exception {
        super.setUp();
        finder = new AmenityFinder(new DBHandler(STEAKSCORP_READ_ONLY));

        // Sidewalk near north end of CULC
        curLocation = new Location("TEST_PROVIDER");
        curLocation.setLatitude(33.7751878);
        curLocation.setLongitude(-84.39687341);

        building = new Building("Clough Undergraduate Learning Commons",
                "CUL", 33.774792, -84.396386);
    }

    public void tearDown() throws Exception { }

    public void testGetNearbyBuildings() throws Exception {
        PriorityQueue<RelativeBuilding> result = finder.getNearbyBuildings(curLocation);

        assertNotNull(result);
        assertNotNull(result.peek());

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetFloorsInBuilding1() throws Exception {
        List<Integer> result = finder.getFloorsInBuilding(building);

        assertNotNull(result);
        assertNotNull(result.get(0));

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetFloorsInBuilding2() throws Exception {
        List<Integer> result = finder.getFloorsInBuilding(building.getId());

        assertNotNull(result);
        assertNotNull(result.get(0));

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetNearbyAmenities() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getNearbyAmenities(curLocation);

        assertNotNull(result);
        assertNotNull(result.peek());

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetNearbyAmenitiesByType() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getNearbyAmenitiesByType(curLocation,
                "Bathroom");

        assertNotNull(result);
        assertNotNull(result.peek());

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }

    public void testGetAmenitiesInBuilding() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getAmenitiesInBuilding(curLocation,
                building);

        assertNotNull(result);
        assertNotNull(result.peek());

        // Make sure result actually holds something
        assertTrue(result.size() > 0);

    }

    public void testGetAmenitiesInBuilding1() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getAmenitiesInBuilding(curLocation,
                building, "Bathroom");

        assertNotNull(result);
        assertNotNull(result.peek());

        // Make sure result actually holds something
        assertTrue(result.size() > 0);

    }

    public void testGetAmenitiesInBuilding2() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getAmenitiesInBuilding(curLocation,
                building, "Bathroom", 1);

        assertNotNull(result);
        assertNotNull(result.peek());

        // Make sure result actually holds something
        assertTrue(result.size() > 0);
    }
}