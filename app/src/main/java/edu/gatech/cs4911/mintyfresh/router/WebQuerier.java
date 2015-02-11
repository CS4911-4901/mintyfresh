package edu.gatech.cs4911.mintyfresh.router;

import org.w3c.dom.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * A WebQuerier object manages the RESTful calls to a remote HTTP server.
 */
public class WebQuerier {

    /**
     * Returns a Document object obtained from performing a GET on a remote URI.
     *
     * @param uri The URI to GET.
     * @return A Document object obtained from performing a GET on a remote URI.
     * @throws IOException if the connection could not be established.
     */
    public static Document getHttpDoc(String uri) throws IOException {
        try {
            Document output;
            HttpURLConnection request = setUpGet(uri);
            DocumentBuilder docParser = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // Connect to server
            request.connect();

            // Parses output as Document
            output = docParser.parse(request.getInputStream());

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
        webRequest.setDoOutput(true);

        return webRequest;
    }
}
