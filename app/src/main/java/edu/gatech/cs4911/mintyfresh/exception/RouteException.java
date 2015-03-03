package edu.gatech.cs4911.mintyfresh.exception;

/**
 * A RouteException is thrown when there is no path between start and destination.
 */
public class RouteException extends Exception {
    /**
     * A RouteException is thrown when there is no path between start and destination.
     */
    public RouteException() {
        super("No route could be found between source and destination locations!");
    }
}
