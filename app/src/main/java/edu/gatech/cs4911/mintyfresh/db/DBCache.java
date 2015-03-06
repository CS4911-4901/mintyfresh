package edu.gatech.cs4911.mintyfresh.db;

import android.util.LruCache;

import java.util.List;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;

/**
 * A DBCache is a cache for DBQuery results. As such, it caches
 * Building, Integer, and Amenity output lists based on a LRU
 * (least-recently used) caching scheme.
 */
public class DBCache {
    /**
     * The maximum number of cached results to store for Building queries.
     */
    private static final int MAX_ENTRIES_BUILDING = 2;
    /**
     * The maximum number of cached results to store for Integer queries.
     */
    private static final int MAX_ENTRIES_INTEGER = 2;
    /**
     * The maximum number of cached results to store for Amenity queries.
     */
    private static final int MAX_ENTRIES_AMENITY = 10;
    /**
     * A least-recently used (LRU) cache for Building queries.
     */
    private LruCache<String, List<Building>> buildingCache;
    /**
     * A least-recently used (LRU) cache for Integer queries.
     */
    private LruCache<String, List<Integer>> integerCache;
    /**
     * A least-recently used (LRU) cache for Amenity queries.
     */
    private LruCache<String, List<Amenity>> amenityCache;

    /**
     * Constructs a new DBCache. The backing LRU cache objects will
     * be instantiated with their default values for number of entries.
     */
    public DBCache() {
        this.buildingCache = new LruCache<>(MAX_ENTRIES_BUILDING);
        this.integerCache = new LruCache<>(MAX_ENTRIES_INTEGER);
        this.amenityCache = new LruCache<>(MAX_ENTRIES_AMENITY);
    }

    /**
     * Adds a Building list to the cache.
     *
     * @param query The query associated with the query result.
     * @param cacheEntry The query result.
     */
    public void putBuildingList(String query, List<Building> cacheEntry) {
        buildingCache.put(query, cacheEntry);
    }

    /**
     * Adds an Integer list to the cache.
     *
     * @param query The query associated with the query result.
     * @param cacheEntry The query result.
     */
    public void putIntegerList(String query, List<Integer> cacheEntry) {
        integerCache.put(query, cacheEntry);
    }

    /**
     * Adds an Amenity list to the cache.
     *
     * @param query The query associated with the query result.
     * @param cacheEntry The query result.
     */
    public void putAmenityList(String query, List<Amenity> cacheEntry) {
        amenityCache.put(query, cacheEntry);
    }

    /**
     * Fetches a Building list cache entry from the cache. If there is a
     * cache miss, this will return null.
     *
     * @param query The query associated with the cache entry.
     * @return A Building list cache entry, or null if it doesn't exist in the cache.
     */
    public List<Building> getBuildingList(String query) {
        return buildingCache.get(query);
    }

    /**
     * Fetches an Integer list cache entry from the cache. If there is a
     * cache miss, this will return null.
     *
     * @param query The query associated with the cache entry.
     * @return An Integer list cache entry, or null if it doesn't exist in the cache.
     */
    public List<Integer> getIntegerList(String query) {
        return integerCache.get(query);
    }

    /**
     * Fetches an Amenity list cache entry from the cache. If there is a
     * cache miss, this will return null.
     *
     * @param query The query associated with the cache entry.
     * @return An Amenity list cache entry, or null if it doesn't exist in the cache.
     */
    public List<Amenity> getAmenityList(String query) {
        return amenityCache.get(query);
    }
}
