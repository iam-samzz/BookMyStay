import java.util.HashMap;
import java.util.Map;

// Room class handles the characteristics of a room
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
        System.out.println("Available Rooms: " + availableRooms);
        System.out.println();
    }
}

// RoomInventory class acts as the single source of truth for availability [cite: 173, 174, 180]
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

// Main public class to run the application
public class UseCase3InventorySetup {
    public static void main(String[] args) {
        // 1. Initialize the centralized inventory [cite: 177]
        RoomInventory inventory = new RoomInventory();

        // 2. Setup Room instances with characteristics (Beds, Size, Price) [cite: 205]
        Room singleRoom = new Room("Single Room", 1, 200, 1500.0);
        Room doubleRoom = new Room("Double Room", 2, 400, 2500.0);
        Room suiteRoom = new Room("Suite Room", 3, 750, 5000.0);

        // 3. Retrieve availability directly from the centralized inventory HashMap [cite: 180]
        int singleAvailable = inventory.getRoomAvailability().get("Single Room");
        int doubleAvailable = inventory.getRoomAvailability().get("Double Room");
        int suiteAvailable = inventory.getRoomAvailability().get("Suite Room");

        // 4. Print the output as shown in the document's Use Case 3 instructions [cite: 309]
        singleRoom.printRoomDetails(singleAvailable);
        doubleRoom.printRoomDetails(doubleAvailable);
        suiteRoom.printRoomDetails(suiteAvailable);
    }
}