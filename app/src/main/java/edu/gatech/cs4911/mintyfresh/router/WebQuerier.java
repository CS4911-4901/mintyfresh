package edu.gatech.cs4911.mintyfresh.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * A WebQuerier object manages the RESTful calls to a remote HTTP server.
 */
public class WebQuerier {

    /**
     * Returns a String obtained from performing a GET on a remote URI.
     *
     * @param uri The URI to GET.
     * @return A String of the response obtained from performing a GET on a remote URI.
     * @throws IOException if the connection could not be established.
     */
    public static String getHttpDoc(String uri) throws IOException {
        try {
            String output, buffer;
            BufferedReader reader;
            StringBuilder streamParser = new StringBuilder();
            HttpURLConnection request = setUpGet(uri);

            // Connect to server
            request.connect();

            // Build String from result stream
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            buffer = reader.readLine();
            while (buffer != null) {
                streamParser.append(buffer);
                buffer = reader.readLine();
            }

            output = streamParser.toString();

            // Closes the connection
            request.disconnect();

            return output;
        } catch (Exception e) {
            // Don't bother with specific parsing errors, just fail generally
            throw new IOException();
        }
    }

    /**
     * Sets up an HTTP GET as an HttpURLConnection object, ready for IO.
     *
     * @param uri The URI to set up a connection to.
     * @return An HTTP GET as an HttpURLConnection object, ready for IO.
     * @throws IOException if the connection could not be established or the URI is malformed.
     */
    private static HttpURLConnection setUpGet(String uri) throws IOException {
        HttpURLConnection webRequest = (HttpURLConnection) new URL(uri).openConnection();
        webRequest.setRequestMethod("GET");
        webRequest.setDoInput(true);
        webRequest.setDoOutput(false); // true = POST

        return webRequest;
    }
}
