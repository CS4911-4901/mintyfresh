package edu.gatech.cs4911.mintyfresh.db;

import edu.gatech.cs4911.mintyfresh.queryresponse.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        // TODO: complete
        return null;
    }
    public static String[] getFloorplan() { return null; }
}
