package edu.gatech.cs4911.mintyfresh.db;

import edu.gatech.cs4911.mintyfresh.queryresponse.*;
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
     * @throws SQLException if the query was unsuccessful.
     * @return The database response, packaged in a Building list.
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
     * @throws SQLException if the query was unsuccessful.
     * @return The database response, packaged in an Amenity list.
     */
    public static List<Amenity> getAmenities(DBHandler handler) throws SQLException {
        ResultSet result = handler.submitQuery("SELECT * FROM Amenity " +
                "INNER JOIN Building ON Amenity.building = Building.id " +
                "INNER JOIN Amenity_Attribute_Lookup " +
                "ON Amenity.id = Amenity_Attribute_Lookup.id;");
        List<Amenity> output = new ArrayList<Amenity>();

        /**
         * A map to prevent constructing multiple copies of the same Amenity to handle
         * multiple query results of the same Amenity resulting from multiple attributes.
         */
        Map<String, Integer> existanceCheck = new HashMap<String, Integer>();

        while (result.next()) {
            if (existanceCheck.containsKey(result.getString("id"))) {
                // We've already set up this Amenity!
                // We just need to add a new attribute to it
                output.get(
                        existanceCheck.get(result.getString("id")))
                        .addAttribute(result.getString("attribute"));
            } else {
                output.add(new Amenity(
                        result.getString("building"),
                        result.getString("name"),
                        result.getString("amenity_type"),
                        result.getInt("building_level"),
                        result.getString("id"),
                        result.getFloat("latitude"),
                        result.getFloat("longitude"),
                        result.getString("attribute"),
                        result.getInt("x"),
                        result.getInt("y")
                ));

                // Now add this to our map, in case we have to deal with multiple attributes
                existanceCheck.put(result.getString("id"), output.size() - 1);
            }
        }

        return output;
    }
    public static String[] getFloorplan() { return null; }
}
