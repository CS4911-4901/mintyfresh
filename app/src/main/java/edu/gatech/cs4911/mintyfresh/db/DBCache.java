package edu.gatech.cs4911.mintyfresh.db;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * A cache of database queries and results to speed up repeated query times.
 */
public class DBCache {
    /**
     * The maximum number of ResultSet objects to hold in the cache.
     */
    public static int MAX_RESULTS = 10;
    /**
     * The container mapping String queries to ResultSet objects.
     */
    private Map<String, ResultSet> dbResult;
    /**
     * A heap tracking the cache entries least-recently used.
     */
    private PriorityQueue<DBCacheNode> lruTracker;
    /**
     * Constructs a new DBCache object.
     */
    public DBCache() {
        dbResult = new HashMap<>();
        lruTracker = new PriorityQueue<>();
    }

    /**
     * Adds a new query response to the cache. If the number of
     * responses in the cache will exceed MAX_RESULTS, the
     */
    public void add(String query, ResultSet result) {

    }

    public void remove(String query) {

    }
}
