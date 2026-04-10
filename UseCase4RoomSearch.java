import java.util.HashMap;
import java.util.Map;

// Domain Model: Room objects provide descriptive information
class Room {
    private String name;
    private int beds;
    private int size;
    private double pricePerNight;

    public Room(String name, int beds, int size, double pricePerNight) {
        this.name = name;
        this.beds = beds;
        this.size = size;
        this.pricePerNight = pricePerNight;
    }

    public void printRoomDetails(int availableRooms) {
        System.out.println(name + ":");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + pricePerNight);
        System.out.println("Available: " + availableRooms);
    }
}

// Inventory as State Holder: Accessed only to retrieve current availability
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        // Keys updated to match the Use Case 4 search logic
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

/**
 * Description:
 * This class provides search functionality for guests to view available rooms.
 * It reads room availability from inventory and room details from Room objects.
 * No inventory mutation or booking logic is performed in this class.
 * @version 4.0
 */
class RoomSearchService {

    /**
     * Displays available rooms along with their details and pricing.
     * This method performs read-only access to inventory and room data.
     *
     * @param inventory centralized room inventory
     * @param singleRoom single room definition
     * @param doubleRoom double room definition
     * @param suiteRoom suite room definition
     */
    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Check and display Single Room availability
        if (availability.containsKey("Single") && availability.get("Single") > 0) {
            singleRoom.printRoomDetails(availability.get("Single"));
        }

        // Check and display Double Room availability
        if (availability.containsKey("Double") && availability.get("Double") > 0) {
            doubleRoom.printRoomDetails(availability.get("Double"));
        }

        // Check and display Suite Room availability
        if (availability.containsKey("Suite") && availability.get("Suite") > 0) {
            suiteRoom.printRoomDetails(availability.get("Suite"));
        }
    }
}

/**
 * MAIN CLASS UseCase4RoomSearch
 * Use Case 4: Room Search & Availability Check
 * Description:
 * This class demonstrates how guests can view available rooms without modifying inventory data.
 * The system enforces read-only access by design and usage discipline.
 * @version 4.0
 */
public class UseCase4RoomSearch {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // 1. Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // 2. Initialize Room domain objects with details requested in Use Case 4
        Room singleRoom = new Room("Single Room", 1, 250, 1500.0);
        Room doubleRoom = new Room("Double Room", 2, 400, 2500.0);
        Room suiteRoom = new Room("Suite Room", 3, 750, 5000.0);

        // 3. Initialize Search Service
        RoomSearchService searchService = new RoomSearchService();

        // 4. Perform Read-Only Search
        searchService.searchAvailableRooms(inventory, singleRoom, doubleRoom, suiteRoom);
    }
}