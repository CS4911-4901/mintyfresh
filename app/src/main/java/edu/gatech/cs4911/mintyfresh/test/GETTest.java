package edu.gatech.cs4911.mintyfresh.test;

import android.test.InstrumentationTestCase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class GETTest extends InstrumentationTestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception { }

    public void testConnection() throws Exception {
        HttpURLConnection test1 = testSetUpGet();
        InputStream test2 = testRequestConnect(test1);
        JSONObject test3 = testDocumentParse(test2);
        assertNotNull(test3);
    }

    public HttpURLConnection testSetUpGet() throws Exception {
        String uri = "https://maps.googleapis.com/maps/api/directions/json?mode=walking" +
                "&origin=33.774063,-84.398836&destination=33.774792,-84.396386";
        HttpURLConnection request = (HttpURLConnection) new URL(uri).openConnection();
        request.setRequestMethod("GET");
        request.setDoInput(true);
        request.setDoOutput(false); // true will force request method to POST!

        assertNotNull(request);
        return request;
    }

    public InputStream testRequestConnect(HttpURLConnection request) throws Exception {
        InputStream output;
        int status;

        request.connect();
        output = request.getInputStream();
        status = request.getResponseCode();

        if (status != 200) {
            request.disconnect();
            output.close();
            throw new Exception("Error: Request return code was " + status);
        }
        //request.disconnect();

        return output;
    }

    public JSONObject testDocumentParse(InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder jsonOutput = new StringBuilder();
        String buffer = reader.readLine();
        while (buffer != null) {
            jsonOutput.append(buffer + "\n");
            buffer = reader.readLine();
        }

        stream.close();
        return new JSONObject(jsonOutput.toString());
    }
}