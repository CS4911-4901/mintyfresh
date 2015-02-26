package edu.gatech.cs4911.mintyfresh.test;

import android.location.Location;
import android.test.InstrumentationTestCase;

import com.google.android.gms.maps.model.LatLng;

import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.AmenityFinder;
import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.router.RelativeAmenity;

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

    public void testGetNearbyAmenities() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getNearbyAmenities(curLocation);

        assertNotNull(result);
        assertNotNull(result.peek());
    }

    public void testGetNearbyAmenitiesByType() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getNearbyAmenitiesByType(curLocation,
                "Bathroom");

        assertNotNull(result);
        assertNotNull(result.peek());
    }

    public void testGetAmenitiesInBuilding() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getAmenitiesInBuilding(curLocation,
                building);

        assertNotNull(result);
        assertNotNull(result.peek());

    }

    public void testGetAmenitiesInBuilding1() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getAmenitiesInBuilding(curLocation,
                building, "Bathroom");

        assertNotNull(result);
        assertNotNull(result.peek());

    }

    public void testGetAmenitiesInBuilding2() throws Exception {
        PriorityQueue<RelativeAmenity> result = finder.getAmenitiesInBuilding(curLocation,
                building, "Bathroom", 1);

        assertNotNull(result);
        assertNotNull(result.peek());

    }
}
