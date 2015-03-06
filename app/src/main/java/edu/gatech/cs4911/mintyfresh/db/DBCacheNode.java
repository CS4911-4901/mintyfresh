package edu.gatech.cs4911.mintyfresh.db;

/**
 * A DBCacheNode is a node for tracking the least-recently used
 * cache entry in a DBCache.
 */
public class DBCacheNode implements Comparable<DBCacheNode> {
    /**
     * The String database query to track.
     */
    String query;
    /**
     * The number of cache queries since this node was last accessed.
     */
    private int lastUse;

    public DBCacheNode(String query) {
        this.query = query;
        this.lastUse = 0;
    }

    /**
     * Increments the number of times since this node was last accessed.
     *
     * A DBCacheNode should be incremented every time a cache query
     * did NOT return this node.
     */
    public void increment() {
        lastUse++;
    }

    /**
     * Return the String database query this node corresponds to.
     *
     * @return The String database query this node corresponds to.
     */
    public String getQuery() {
        return query;
    }

    /**
     * Return the number of cache queries since this node was last accessed.
     *
     * @return The number of cache queries since this node was last accessed.
     */
    public int getLastUse() {
        return lastUse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DBCacheNode) {
            return ((DBCacheNode) obj).getQuery().equals(query);
        }

        return false;
    }

    @Override
    public int compareTo(DBCacheNode node) {
        return lastUse - node.getLastUse();
    }
}
