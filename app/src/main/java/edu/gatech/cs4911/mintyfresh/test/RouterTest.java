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

    public void testATwoSecDelay() throws Exception {
        // "A" is so this test runs first, haha

        // Google Maps API is in danger of returning API_QUOTA_EXCEEDED
        // if both too many requests per day AND too many requests per
        // given timeframe

        // Use this to prevent this from happening to following test!!
        Thread.sleep(2000); // 2 sec
    }

    public void testGetDirectionsTo() throws Exception {

        // Student Center -> CULC
        List<LatLng> output = getDirectionsTo(33.774063, -84.398836, 33.774792, -84.396386);
        assertNotNull(output);

        // And check each item in list
        for (LatLng point : output) {
            assertNotNull(point);
        }
    }
}