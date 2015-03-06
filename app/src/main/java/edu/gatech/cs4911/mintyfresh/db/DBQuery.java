package edu.gatech.cs4911.mintyfresh.db;

import android.util.LruCache;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.*;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A static class that groups all the query statements together.
 */
public class DBQuery {
    /**
     * The maximum number of entries to store in the cache.
     */
    private static final int MAX_CACHE_ENTRIES = 5;
    /**
     * A least-recently used (LRU) cache for database queries.
     */
    private static LruCache<String, ResultSet> DBCache = new LruCache<>(MAX_CACHE_ENTRIES);
    /**
     * The fields to select from the Amenity table to complete an Amenity object.
     */
    public static final String AMENITY_FIELDS_PREFIX = "SELECT Amenity.id, building, name, " +
            "amenity_type, building_level, floor_x, floor_y, attribute, latitude, longitude " +
            "FROM Amenity INNER JOIN Building ON Amenity.building = Building.id " +
            "INNER JOIN Building_Location ON Building.id = Building_Location.id " +
            "LEFT OUTER JOIN Amenity_MapLocation ON Amenity.id = Amenity_MapLocation.id " +
            "LEFT OUTER JOIN Amenity_Attribute_Lookup ON Amenity.id = Amenity_Attribute_Lookup.id ";

    /**
     * Queries the database and returns all Building objects.
     *
     * @param handler The database connection to use.
     * @return The database response, packaged in a Building list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Building> getBuildings(DBHandler handler) throws SQLException {
        String query = "SELECT Building.id, name, latitude, " +
                "longitude FROM Building INNER JOIN Building_Location " +
                "WHERE Building.id = Building_Location.id;";
        ResultSet result = DBCache.get(query);
        
        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }

        List<Building> output = new ArrayList<>();
        while (result.next()) {
            output.add(new Building(
                    result.getString("id"),
                    result.getString("name"),
                    result.getDouble("latitude"),
                    result.getDouble("longitude")));
        }

        return output;
    }

    /**
     * Queries the database and returns the number of floors in a given building.
     *
     * @param handler The database connection to use.
     * @param buildingId The ID of the Building to check.
     * @return The number of floors in a given building.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Integer> getLevelsInBuilding(DBHandler handler, String buildingId)
            throws SQLException {
        String query = "SELECT level FROM Building_Floors " + "WHERE id = \"" + buildingId + "\";";
        List<Integer> output = new ArrayList<>();
        ResultSet result = DBCache.get(query);
        
        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }

        while (result.next()) {
            output.add(result.getInt("level"));
        }

        return output;
    }

    /**
     * Queries the database and returns the number of floors in a given building.
     *
     * @param handler The database connection to use.
     * @param building The Building to check.
     * @return The number of floors in a given building.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Integer> getLevelsInBuilding(DBHandler handler, Building building)
            throws SQLException {
        return getLevelsInBuilding(handler, building.getId());
    }


    /**
     * Queries the database and returns all Amenity objects.
     *
     * @param handler The database connection to use.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenities(DBHandler handler) throws SQLException {
        String query = AMENITY_FIELDS_PREFIX + ";";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and only returns Amenity objects in the given building,
     * (idenfied by its ID), of the given type.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @param type The provided type to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenities(DBHandler handler, String buildingId,
              String type) throws SQLException {
        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Building.id = \"" + buildingId + "\" " +
                "AND amenity_type = \"" + type + "\"";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and only returns Amenity objects in the given building,
     * (idenfied by its ID), of the given type.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @param type The provided type to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenities(DBHandler handler, String buildingId,
             String type, int floor) throws SQLException {
        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Building.id = \"" + buildingId + "\" " +
                "AND amenity_type = \"" + type + "\" " +
                "AND building_level = \"" + floor + "\"";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and only returns Amenity objects in the given building,
     * (idenfied by its ID), of the given type and given (single) type attribute.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @param type The provided type to filter results.
     * @param attribute The provided attribute to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenities(DBHandler handler, String buildingId,
              String type, String attribute) throws SQLException {
        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Building.id = \"" + buildingId + "\" " +
                "AND amenity_type = \"" + type + "\" " +
                "AND attribute = \"" + attribute + "\";";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and only returns Amenity objects in the given building,
     * (idenfied by its ID), of the given type and given type attributes.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @param type The provided type to filter results.
     * @param attributes The provided attributes to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenities(DBHandler handler, String buildingId,
              String type, String[] attributes) throws SQLException {

        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Building.id = \"" + buildingId + "\" " +
                "AND amenity_type = \"" + type + "\" " +
                "AND (";

        // Append list of attributes to query string
        for (int i = 0; i < attributes.length; i++) {
            query += "attribute = \""
                    + attributes[i] + "\"";
            if (i != attributes.length - 1) {
                query += " OR ";
            } else {
                query += ") GROUP BY id HAVING COUNT(*) = " + attributes.length + ";";
            }
        }

        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }

        return amenityPackager(result);
    }

    /**
     * Queries the database and only returns Amenity objects in the given building level,
     * (idenfied by its ID), of the given type and given (single) type attribute.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @param floor The floor of the building to filter results.
     * @param type The provided type to filter results.
     * @param attribute The provided attribute to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenities(DBHandler handler, String buildingId,
             int floor, String type, String attribute) throws SQLException {

        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Building.id = \"" + buildingId + "\" " +
                "AND building_level = \"" + floor + "\" " +
                "AND amenity_type = \"" + type + "\" " +
                "AND attribute = \"" + attribute + "\";";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and only returns Amenity objects in
     * the given building, idenfied by its ID.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenitiesByBuildingId(DBHandler handler, String buildingId)
            throws SQLException {
        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Building.id = \"" + buildingId + "\";";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and returns all Amenity objects of the given type.
     *
     * @param handler The database connection to use.
     * @param type The provided type to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenitiesByType(DBHandler handler, String type)
            throws SQLException {
        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Amenity.amenity_type = \"" + type + "\";";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and returns all Amenity objects of the given type
     * and given (single) type attribute.
     *
     * @param handler The database connection to use.
     * @param type The provided type to filter results.
     * @param attribute The provided attribute to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenitiesByTypeAndAttribute(DBHandler handler,
             String type, String attribute) throws SQLException {
        String query = AMENITY_FIELDS_PREFIX +
                "WHERE Amenity.amenity_type = \"" + type + "\" AND " +
                "Amenity_Attribute_Lookup.attribute =  \"" + attribute + "\";";
        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }
        
        return amenityPackager(result);
    }

    /**
     * Queries the database and returns all Amenity objects of the given type
     * and given type attributes.
     *
     * @param handler The database connection to use.
     * @param type The provided type to filter results.
     * @param attributes The provided attributes to filter results.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenitiesByTypeAndAttributes(DBHandler handler,
              String type, String[] attributes) throws SQLException {
        String query = AMENITY_FIELDS_PREFIX +
                "WHERE amenity_type = \"" + type + "\" " +
                "AND (";

        // Append list of attributes to query string
        for (int i = 0; i < attributes.length; i++) {
            query += "attribute = \""
                    + attributes[i] + "\" ";
            if (i != attributes.length - 1) {
                query += " OR ";
            } else {
                query += ") GROUP BY id HAVING COUNT(*) = " + attributes.length + ";";
            }
        }

        ResultSet result = DBCache.get(query);

        if (result == null) {
            result = handler.submitQuery(query);
            DBCache.put(query, result);
        }

        return amenityPackager(result);
    }

    /**
     * A helper method for Amenity queries to package their differing ResultSets into
     * a common structure.
     *
     * @param queryResult The result of a database query.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    private static List<Amenity> amenityPackager(ResultSet queryResult) throws SQLException {
        /**
         * A map to prevent constructing multiple copies of the same Amenity to handle
         * multiple query results of the same Amenity resulting from multiple attributes.
         */
        Map<String, Integer> existanceCheck = new HashMap<>();
        List<Amenity> output = new ArrayList<>();

        while (queryResult.next()) {
            if (existanceCheck.containsKey(queryResult.getString("id"))) {
                // We've already set up this Amenity!
                // We just need to add a new attribute to it
                output.get(
                        existanceCheck.get(queryResult.getString("id")))
                        .addAttribute(queryResult.getString("attribute"));
            } else {
                output.add(new Amenity(
                        new Building(queryResult.getString("name"),
                                queryResult.getString("building"),
                                queryResult.getDouble("latitude"),
                                queryResult.getDouble("longitude")),
                        queryResult.getString("amenity_type"),
                        queryResult.getInt("building_level"),
                        queryResult.getString("id"),
                        queryResult.getString("attribute"),
                        queryResult.getInt("floor_x"),
                        queryResult.getInt("floor_y")
                ));

                // Now add this to our map, in case we have to deal with multiple attributes
                existanceCheck.put(queryResult.getString("id"), output.size() - 1);
            }
        }

        return output;
    }


    /**
     * Queries the database and returns the floorplan image of the provided
     * building and floor. Note that this will be relatively slow, and
     * the image cache should be consulted prior to calling getFloorplan.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @param floor The floor of the building to filter results.
     * @return A floorplan image of the provided building and floor, as an InputStream.
     * @throws SQLException if the query was unsuccessful.
     */
    public static InputStream getFloorplan(DBHandler handler, String buildingId, int floor)
            throws SQLException {
        ResultSet result = handler.submitQuery("SELECT image FROM Floorplan_Image " +
                "WHERE hash = (SELECT image_hash FROM Floorplan WHERE id = \"" + buildingId +
                "\" AND map_level = " + floor + ");");

        result.next();
        return result.getBinaryStream("image");
    }

    /**
     * Queries the database and returns all floorplan images of the provided
     * building. Note that this will be relatively slow, and the image cache
     * should be consulted prior to calling getFloorplan.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @return A floorplan image of the provided building and floor, as an InputStream.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<InputStream> getFloorplans(DBHandler handler, String buildingId)
            throws SQLException {
        ResultSet result = handler.submitQuery("SELECT image FROM Floorplan_Image " +
                "INNER JOIN Floorplan on hash = image_hash WHERE id = \"" + buildingId +
                "\"");
        List<InputStream> output = new ArrayList<>();

        while (result.next()) {
            output.add(result.getBinaryStream("image"));
        }

        return output;
    }

}
