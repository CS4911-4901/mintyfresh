package edu.gatech.cs4911.mintyfresh.exception;

/**
 * A NoDbResultException is thrown when a query to a database returns an empty set.
 */
public class NoDbResultException extends Exception {
    /**
     * A NoDbResultException is thrown when a query to a database returns an empty set.
     */
    public NoDbResultException() {
        super("The call to the database returned an empty set!");
    }
}
