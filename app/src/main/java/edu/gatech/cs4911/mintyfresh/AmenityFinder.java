package edu.gatech.cs4911.mintyfresh;

import android.location.Location;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.DBQuery;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.exception.NoDbResultException;
import edu.gatech.cs4911.mintyfresh.router.RelativeAmenity;
import edu.gatech.cs4911.mintyfresh.router.RelativeBuilding;
import edu.gatech.cs4911.mintyfresh.router.Router;

/**
 * AmenityFinder is an interface between the view
 * and the underlying query and filtering logic.
 */
public class AmenityFinder {
    /**
     * A packaged set of database connection details.
     */
    private DBHandler handler;

    /**
     * Constructs a new AmenityFinder with given database information.
     *
     * @param handler A DBHandler object, containing database connection details.
     */
    public AmenityFinder(DBHandler handler) {
        this.handler = handler;
    }

    /**
     * Returns a heap of all RelativeBuilding objects, ordered by relative distance.
     *
     * @param location The current location.
     * @return A heap of nearby buildings.
     * @throws NoDbResultException if the database did not return any Building objects.
     */
    public PriorityQueue<RelativeBuilding> getNearbyBuildings(Location location)
            throws NoDbResultException {
        try {
            return buildingHeapPackager(location, DBQuery.getBuildings(handler));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns an Integer list of floors in a given building.
     *
     * @param building A given building.
     * @return A list of floors in the given building.
     * @throws NoDbResultException if the database did not return any floors.
     */
    public List<Integer> getFloorsInBuilding(Building building) throws NoDbResultException {
        try {
            return DBQuery.getLevelsInBuilding(handler, building);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns an Integer list of floors in a given building.
     *
     * @param buildingId A given building, identified by a String ID.
     * @return A list of floors in the given building.
     * @throws NoDbResultException if the database did not return any floors.
     */
    public List<Integer> getFloorsInBuilding(String buildingId) throws NoDbResultException {
        try {
            return DBQuery.getLevelsInBuilding(handler, buildingId);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of amenity types in a given building.
     *
     * @param buildingId A given building, identified by a String ID.
     * @return A list of amenity types in a given building.
     * @throws NoDbResultException if the database did not return any floors.
     */
    public List<String> getAmenityTypesInBuilding(String buildingId) throws NoDbResultException {
        try {
            return DBQuery.getAmenityTypesInBuilding(handler, buildingId);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of amenity types in a given building.
     *
     * @param building A given building.
     * @return A list of amenity types in a given building.
     * @throws NoDbResultException if the database did not return any floors.
     */
    public List<String> getAmenityTypesInBuilding(Building building) throws NoDbResultException {
        return getAmenityTypesInBuilding(building.getId());
    }

    /**
     * Returns a heap of all RelativeAmenity objects, ordered by relative distance.
     *
     * @param location The current location.
     * @return A heap of nearby amenities.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public PriorityQueue<RelativeAmenity> getNearbyAmenities(Location location)
            throws NoDbResultException {
        try {
            return amenityHeapPackager(location, DBQuery.getAmenities(handler));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a heap of all RelativeAmenity objects of a given type, ordered by relative distance.
     *
     * @param location The current location.
     * @param type The type of Amenity object.
     * @return A heap of nearby amenities.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public PriorityQueue<RelativeAmenity> getNearbyAmenitiesByType(Location location, String type)
            throws NoDbResultException {
        try {
            return amenityHeapPackager(location, DBQuery.getAmenitiesByType(handler, type));
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of all Amenity objects in a given Building.
     *
     * @param building A given building.
     * @return A Amenity objects in a given Building.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public List<Amenity> getAmenitiesInBuilding(Building building)
            throws NoDbResultException {
        try {
            return DBQuery.getAmenitiesByBuildingId(handler, building.getId());
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of all Amenity objects in a given Building.
     *
     * @param buildingId A given building, identified by a String ID.
     * @return A list of Amenity objects in a given Building.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public List<Amenity> getAmenitiesInBuilding(String buildingId)
            throws NoDbResultException {
        try {
            return DBQuery.getAmenitiesByBuildingId(handler, buildingId);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of all Amenity objects of a given type in a given Building.
     *
     * @param building A given building.
     * @param type The type of Amenity object.
     * @return A list of all Amenity objects of a given type in the given Building.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public List<Amenity> getAmenitiesInBuilding(Building building, String type)
            throws NoDbResultException {
        try {
            return DBQuery.getAmenities(handler, building.getId(), type);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of all Amenity objects of a given type in a given Building.
     *
     * @param buildingId A given building, identified by a String ID.
     * @param type The type of Amenity object.
     * @return A list of all Amenity objects of a given type in the given Building.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public List<Amenity> getAmenitiesInBuilding(String buildingId, String type)
            throws NoDbResultException {
        try {
            return DBQuery.getAmenities(handler, buildingId, type);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of all Amenity objects of a given type in a given Building level.
     *
     * @param building A given building.
     * @param type The type of Amenity object.
     * @param level The floor to filter results by.
     * @return A list of all Amenity objects of a given type in a given Building level
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public List<Amenity> getAmenitiesInBuilding(Building building,
             String type, int level) throws NoDbResultException {
        try {
            return DBQuery.getAmenities(handler, building.getId(), type, level);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a list of all Amenity objects of a given type in a given Building level.
     *
     * @param buildingId A given building, identified by a String ID.
     * @param type The type of Amenity object.
     * @param level The floor to filter results by.
     * @return A list of all Amenity objects of a given type in a given Building level.
     * @throws NoDbResultException if the database did not return any Amenity objects.
     */
    public List<Amenity> getAmenitiesInBuilding(String buildingId,
             String type, int level) throws NoDbResultException {
        try {
            return DBQuery.getAmenities(handler, buildingId, type, level);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Returns a map of all distinct amenity attributes for a given amenity
     * type - the map maps the attribute name to its human-readable form.
     *
     * @param type The type of Amenity object.
     * @return A list of all distinct amenity attributes for the given amenity type.
     * @throws NoDbResultException if the database did not return any attributes.
     */
    public Map<String, String> getDistinctAttributesByType(String type)
             throws NoDbResultException {
        try {
            return DBQuery.getDistinctAmenityAttributes(handler, type);
        } catch (Exception e) {
            throw new NoDbResultException();
        }
    }

    /**
     * Calculates relative distances between a given location and a set of Amenity objects,
     * and packages them into a PriorityQueue to rank by closest distance.
     *
     * @param location The current location.
     * @param dbResult A result list of Amenity objects returned from the database.
     * @return A ranked PriorityQueue of RelativeAmenity objects, sorted by closest distance.
     */
    private PriorityQueue<RelativeAmenity> amenityHeapPackager(Location location, 
             List<Amenity> dbResult) {
        PriorityQueue<RelativeAmenity> output = new PriorityQueue<>();
        
        // Calculate relative distances and rank
        for (Amenity amenity : dbResult) {
            output.add(new RelativeAmenity(amenity, Router.calcRelativeDistance(
                    location.getLatitude(),
                    location.getLongitude(),
                    amenity.getLatitude(),
                    amenity.getLongitude())));
        }

        return output;
    }

    /**
     * Calculates relative distances between a given location and a set of Building objects,
     * and packages them into a PriorityQueue to rank by closest distance.
     *
     * @param location The current location.
     * @param dbResult A result list of Building objects returned from the database.
     * @return A ranked PriorityQueue of RelativeBuilding objects, sorted by closest distance.
     */
    private PriorityQueue<RelativeBuilding> buildingHeapPackager(Location location, 
             List<Building> dbResult) {
        PriorityQueue<RelativeBuilding> output = new PriorityQueue<>();

        // Calculate relative distances and rank
        for (Building building : dbResult) {
            output.add(new RelativeBuilding(building, Router.calcRelativeDistance(
                    location.getLatitude(),
                    location.getLongitude(),
                    building.getLatitude(),
                    building.getLongitude())));
        }

        return output;
    }
}
