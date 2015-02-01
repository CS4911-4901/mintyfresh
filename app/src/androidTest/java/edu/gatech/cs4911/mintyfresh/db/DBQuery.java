package edu.gatech.cs4911.mintyfresh.db;

import edu.gatech.cs4911.mintyfresh.queryresponse.*;

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
    // TODO: Complete common query strings

    /**
     * Queries the database and returns all Building objects.
     *
     * @param handler The database connection to use.
     * @return The database response, packaged in a Building list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Building> getBuildings(DBHandler handler) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT Building.id, name, latitude, " +
                "longitude FROM Building INNER JOIN Building_Location " +
                "WHERE Building.id = Building_Location.id;");

        List<Building> output = new ArrayList<Building>();
        while (result.next()) {
            output.add(new Building(
                    result.getString("id"),
                    result.getString("name"),
                    result.getFloat("latitude"),
                    result.getFloat("longitude")));
        }

        return output;
    }

    /**
     * Queries the database and returns all Amenity objects.
     *
     * @param handler The database connection to use.
     * @return The database response, packaged in an Amenity list.
     * @throws SQLException if the query was unsuccessful.
     */
    public static List<Amenity> getAmenities(DBHandler handler) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id;");

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

        ResultSet result = handler.submitQuery("SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id " +
                "WHERE Building.id = \"" + buildingId + "\"" +
                "AND Amenity.type = \"" + type + "\"" +
                "AND Amenity_Attribute.attribute = \"" + attribute + "\";");

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

        String queryString = "SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id " +
                "WHERE Building.id = \"" + buildingId + "\"" +
                "AND Amenity.type = \"" + type + "\"" +
                "AND (";

        // Append list of attributes to query string
        for (int i = 0; i < attributes.length; i++) {
            queryString += "Amenity_Attribute.attribute = \""
                    + attributes[i] + "\"";
            if (i != attributes.length - 1) {
                queryString += " AND ";
            } else {
                queryString += ");";
            }
        }

        return amenityPackager(handler.submitQuery(queryString));
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

        ResultSet result = handler.submitQuery("SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id " +
                "WHERE Building.id = \"" + buildingId + "\"" +
                "AND Amenity.building_level = \"" + floor + "\"" +
                "AND Amenity.type = \"" + type + "\"" +
                "AND Amenity_Attribute.attribute = \"" + attribute + "\";");

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
        ResultSet result = handler.submitQuery("SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id " +
                "WHERE Building.id = \"" + buildingId + "\";");

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
        ResultSet result = handler.submitQuery("SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id " +
                "WHERE Amenity.amenity_type = \"" + type + "\";");

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
    public static List<Amenity> getAmenitiesByTypeAndAttributes(DBHandler handler,
             String type, String attribute) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id " +
                "WHERE Amenity.amenity_type = \"" + type + "\" AND " +
                "Amenity_Attribute_Lookup.attribute =  \"" + attribute + "\";");

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

        String queryString = "SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id " +
                "AND Amenity.type = \"" + type + "\"" +
                "AND (";

        // Append list of attributes to query string
        for (int i = 0; i < attributes.length; i++) {
            queryString += "Amenity_Attribute.attribute = \""
                    + attributes[i] + "\"";
            if (i != attributes.length - 1) {
                queryString += " AND ";
            } else {
                queryString += ");";
            }
        }

        return amenityPackager(handler.submitQuery(queryString));
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
        Map<String, Integer> existanceCheck = new HashMap<String, Integer>();
        List<Amenity> output = new ArrayList<Amenity>();

        while (queryResult.next()) {
            if (existanceCheck.containsKey(queryResult.getString("id"))) {
                // We've already set up this Amenity!
                // We just need to add a new attribute to it
                output.get(
                        existanceCheck.get(queryResult.getString("id")))
                        .addAttribute(queryResult.getString("attribute"));
            } else {
                output.add(new Amenity(
                        queryResult.getString("building"),
                        queryResult.getString("name"),
                        queryResult.getString("amenity_type"),
                        queryResult.getInt("building_level"),
                        queryResult.getString("id"),
                        queryResult.getFloat("latitude"),
                        queryResult.getFloat("longitude"),
                        queryResult.getString("attribute"),
                        queryResult.getInt("x"),
                        queryResult.getInt("y")
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
        ResultSet result = handler.submitQuery("SELECT image FROM Floorplan " +
                "WHERE id = \"" + buildingId + "\" AND map_level = " + floor + ";");

        return result.getBinaryStream("image");
    }
}
