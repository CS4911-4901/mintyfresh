package edu.gatech.cs4911.mintyfresh.test;

import android.test.InstrumentationTestCase;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static edu.gatech.cs4911.mintyfresh.router.Router.getDirectionsTo;

public class RouterTest extends InstrumentationTestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception { }

    public void testGetCurrentLocation() throws Exception {
        // TODO: hook up
    }

    public void testGetDirectionsTo() throws Exception {
        // Student Center -> CULC
        List<LatLng> output = getDirectionsTo(33.774063f, -84.398836f, 33.774792f, -84.396386f);
        assertNotNull(output);
    }
}