package edu.gatech.cs4911.mintyfresh.exception;

/**
 * A DisplayFloorplanException is thrown when there was a problem accessing or
 * parsing a floorplan image.
 */
public class DisplayFloorplanException extends Exception {
    /**
     * A DisplayFloorplanException is thrown when there was a problem accessing or
     * parsing a floorplan image.
     */
    public DisplayFloorplanException() {
        super("Error displaying floorplan image! Is the file missing, or was it malformed?");
    }
}
