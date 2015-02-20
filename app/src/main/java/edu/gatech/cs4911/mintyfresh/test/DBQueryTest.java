package edu.gatech.cs4911.mintyfresh.test;

import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.util.List;

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
    }

    public void testGetAmenities() throws Exception {
        List<Amenity> result = getAmenities(handler);
        assertNotNull(result);
    }

    public void testGetAmenities1() throws Exception {
        List<Amenity> result = getAmenities(handler, "CUL", "Bathroom", "male");
        assertNotNull(result);
    }

    public void testGetAmenities2() throws Exception {
        String[] attributeList = new String[2];
        attributeList[0] = "male";
        attributeList[1] = "handicapped";
        List<Amenity> result = getAmenities(handler, "CUL", "Bathroom", attributeList);
        assertNotNull(result);
    }

    public void testGetAmenities3() throws Exception {
        // TODO: Run AFTER adding map information to database
        List<Amenity> result = getAmenities(handler, "CUL", 1, "Bathroom", "male");
        assertNotNull(result);
    }

    public void testGetAmenitiesByBuildingId() throws Exception {
        List<Amenity> result = getAmenitiesByBuildingId(handler, "CUL");
        assertNotNull(result);
    }

    public void testGetAmenitiesByType() throws Exception {
        List<Amenity> result = getAmenitiesByType(handler, "Bathroom");
        assertNotNull(result);
    }

    public void testGetAmenitiesByTypeAndAttribute() throws Exception {
        List<Amenity> result = getAmenitiesByTypeAndAttribute(handler, "Bathroom", "male");
        assertNotNull(result);
    }

    public void testGetAmenitiesByTypeAndAttributes() throws Exception {
        String[] attributeList = new String[2];
        attributeList[0] = "male";
        attributeList[1] = "handicapped";
        List<Amenity> result = getAmenitiesByTypeAndAttributes(handler, "Bathroom", attributeList);
        assertNotNull(result);
    }

    public void testGetFloorplan() throws Exception {
        InputStream result = getFloorplan(handler, "CUL", 1);
        assertNotNull(result);
    }

    public void testGetFloorplans() throws Exception {
        List<InputStream> result = getFloorplans(handler, "CUL");
        assertNotNull(result);
    }
}