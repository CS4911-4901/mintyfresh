package edu.gatech.cs4911.mintyfresh.db;

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
     * A least-recently used (LRU) cache for database queriess.
     */
    private static DBCache cache = new DBCache();
    /**
     * The fields to select from the Amenity table to complete an Amenity object.
     */
    public static final String AMENITY_FIELDS_PREFIX = "SELECT Amenity.id, building, name, " +
            "amenity_type, building_level, floor_x, floor_y, Amenity_Attribute_Lookup.attribute, " +
            "full_name AS attr_f, latitude, longitude FROM Amenity " +
            "INNER JOIN Building ON Amenity.building = Building.id " +
            "INNER JOIN Building_Location ON Building.id = Building_Location.id " +
            "LEFT OUTER JOIN Amenity_MapLocation ON Amenity.id = Amenity_MapLocation.id " +
            "LEFT OUTER JOIN Amenity_Attribute_Lookup ON Amenity.id = Amenity_Attribute_Lookup.id " +
            "INNER JOIN Amenity_Attribute ON Amenity_Attribute_Lookup.attribute = Amenity_Attribute.attribute ";

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

        // Query cache
        List<Building> output = cache.getBuildingList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = new ArrayList<>();

            while (result.next()) {
                output.add(new Building(
                        result.getString("id"),
                        result.getString("name"),
                        result.getDouble("latitude"),
                        result.getDouble("longitude")));
            }

            // And add to cache
            cache.putBuildingList(query, output);
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

        // Query cache
        List<Integer> output = cache.getIntegerList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = new ArrayList<>();

            while (result.next()) {
                output.add(result.getInt("level"));
            }

            // And add to cache
            cache.putIntegerList(query, output);
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

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }
        
        return output;
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

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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
                "AND Amenity_Attribute_Lookup.attribute = \"" + attribute + "\";";

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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
            query += "Amenity_Attribute_Lookup.attribute = \""
                    + attributes[i] + "\"";
            if (i != attributes.length - 1) {
                query += " OR ";
            } else {
                query += ") GROUP BY id HAVING COUNT(*) = " + attributes.length + ";";
            }
        }

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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
                "AND Amenity_Attribute_Lookup.attribute = \"" + attribute + "\";";

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
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
            query += "Amenity_Attribute_Lookup.attribute = \""
                    + attributes[i] + "\" ";
            if (i != attributes.length - 1) {
                query += " OR ";
            } else {
                query += ") GROUP BY id HAVING COUNT(*) = " + attributes.length + ";";
            }
        }

        // Query cache
        List<Amenity> output = cache.getAmenityList(query);

        if (output == null) {
            // Cache miss! Query database
            ResultSet result = handler.submitQuery(query);
            output = amenityPackager(result);

            // And add to cache
            cache.putAmenityList(query, output);
        }

        return output;
    }

    /**
     * Queries the database and returns all distinct Amenity types of Amenity objects
     * in the given building.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID.
     * @return A list of all distinct Amenity types in a given building.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<String> getAmenityTypesInBuilding(DBHandler handler,
              String buildingId) throws SQLException {
        String query = "SELECT DISTINCT full_name AS attribute FROM Amenity_Attribute " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity_Attribute.attribute = Amenity_Attribute_Lookup.attribute " +
                "WHERE id IN (SELECT id FROM Amenity WHERE building = \"" + buildingId + "\");";
        List<String> output = new ArrayList<>();
        ResultSet result = handler.submitQuery(query);
        while (result.next()) {
            output.add(result.getString("attribute"));
        }

        result.close();
        return output;
    }

    /**
     * Queries the database and returns all distinct Amenity types of Amenity objects
     * in the given building.
     *
     * @param handler The database connection to use.
     * @param building The building to filter results.
     * @return A list of all distinct Amenity types in a given building.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<String> getAmenityTypesInBuilding(DBHandler handler,
              Building building) throws SQLException {
        return getAmenityTypesInBuilding(handler, building.getId());
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
                        .addAttribute(queryResult.getString("attr_f"));
            } else {
                output.add(new Amenity(
                        new Building(queryResult.getString("building"),
                                queryResult.getString("name"),
                                queryResult.getDouble("latitude"),
                                queryResult.getDouble("longitude")),
                        queryResult.getString("amenity_type"),
                        queryResult.getInt("building_level"),
                        queryResult.getString("id"),
                        queryResult.getString("attr_f"),
                        queryResult.getInt("floor_x"),
                        queryResult.getInt("floor_y")
                ));

                // Now add this to our map, in case we have to deal with multiple attributes
                existanceCheck.put(queryResult.getString("id"), output.size() - 1);
            }
        }

        queryResult.close();
        return output;
    }


    /**
     * Queries the database and returns a list of all metadata associated with
     * floorplan images stored in the database, packaged as a List of
     * FloorplanMeta objects.
     *
     * @param handler The database connection to use.
     * @return A list of all metadata associated with floorplan images in the database.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<FloorplanMeta> getFloorplanMetadata(DBHandler handler) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT * FROM Floorplan;");
        List<FloorplanMeta> output = new ArrayList<>();

        while (result.next()) {
            output.add(new FloorplanMeta(result.getString("id"), result.getInt("map_level"), 
                    result.getString("image_hash")));
        }

        result.close();
        return output;
    }
    
    /**
     * Queries the database and returns the metadata associated with the floorplan
     * image of the provided building and floor.
     *
     * @param handler The database connection to use.
     * @param buildingId The building to filter results, identified by a String ID..
     * @param level The floor of the building to filter results.
     * @return The metadata associated with the floorplan image of the building and floor.
     * @throws SQLException if the query was unsuccessful.
     */
    public static FloorplanMeta getFloorplanMetadata(DBHandler handler, String buildingId, 
            int level) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT * FROM Floorplan "
                + "WHERE id = \"" + buildingId + "\" AND map_level = " + level + ";");
        FloorplanMeta output;

        result.next();
        output = new FloorplanMeta(result.getString("id"), result.getInt("map_level"), 
                result.getString("image_hash"));
        result.close();

        return output;
    }
    
    /**
     * Queries the database and returns the metadata associated with the floorplan
     * image of the provided building and floor.
     *
     * @param handler The database connection to use.
     * @param building The building to filter results.
     * @param level The floor of the building to filter results.
     * @return The metadata associated with the floorplan image of the building and floor.
     * @throws SQLException if the query was unsuccessful.
     */
    public static FloorplanMeta getFloorplanMetadata(DBHandler handler, Building building, 
            int level) throws SQLException {
        return getFloorplanMetadata(handler, building.getId(), level);
    }

    /**
     * Queries the database and returns a list of all floorplan image hashes.
     *
     * @param handler The database connection to use.
     * @return A list of all floorplan image hashes.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<String> getFloorplanHashes(DBHandler handler) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT hash FROM Floorplan_Image;");
        List<String> output = new ArrayList<>();

        while (result.next()) {
            output.add(result.getString("hash"));
        }

        result.close();
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
        InputStream output;

        result.next();
        output = result.getBinaryStream("image");
        result.close();

        return output;
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

        result.close();
        return output;
    }

    /**
     * Returns a Building object associated with a String building ID.
     *
     * @param handler The database connection to use.
     * @param buildingId The ID associated with a building.
     * @return A Building object associated with a String building ID.
     * @throws SQLException if the query was unsuccessful.
     */
    public static Building getBuildingById(DBHandler handler, String buildingId)
            throws SQLException {
        ResultSet result = handler.submitQuery("SELECT Building.id, name, latitude, longitude " +
                "FROM Building INNER JOIN Building_Location ON Building.id = " +
                "Building_Location.id WHERE Building.id = \"" + buildingId + "\";");
        Building output;

        result.next();
        output = new Building(result.getString("id"), result.getString("name"),
                result.getDouble("latitude"), result.getDouble("longitude"));
        result.close();

        return output;
    }

    /**
     * Queries the database and returns a map of all distinct amenity attributes
     * for a given amenity type - the map maps the attribute name to its
     * human-readable form.
     *
     * @param handler The database connection to use.
     * @param amenityType The type of amenity to filter by.
     * @return A map of all distinct amenity attributes for a given amenity type.
     * @throws SQLException if the query was unsuccessful.
     */
    public static Map<String, String> getDistinctAmenityAttributes(DBHandler handler,
             String amenityType) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT DISTINCT B.attribute, full_name FROM " +
                "((SELECT DISTINCT attribute FROM Amenity_Attribute_Lookup INNER JOIN (SELECT " +
                "id FROM Amenity WHERE amenity_type = \"" + amenityType + "\") AS A ON " +
                "Amenity_Attribute_Lookup.id = A.id) AS B " +
                "INNER JOIN Amenity_Attribute ON B.attribute = Amenity_Attribute.attribute);");

        Map<String, String> output = new HashMap<>();

        while (result.next()) {
            output.put(result.getString("attribute"), result.getString("full_name"));
        }

        result.close();
        return output;
    }
}