import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// --- Domain Classes from Previous Use Cases ---

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
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

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

// --- New Classes for Use Case 6 ---

/**
 * Description:
 * This class is responsible for confirming booking requests and assigning rooms.
 * It ensures:
 * - Each room ID is unique
 * - Inventory is updated immediately
 * - No room is double-booked
 * @version 6.0
 */
class RoomAllocationService {
    // Stores assigned room IDs globally to prevent reuse across all allocations
    private Set<String> allocatedRoomIds;

    // Stores assigned room IDs by room type (Key -> Room type, Value -> Set of assigned IDs)
    private Map<String, Set<String>> assignedRoomsByType;

    /**
     * Initializes allocation tracking structures.
     */
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning a unique room ID and updating inventory.
     * @param reservation booking request
     * @param inventory centralized room inventory
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Check if the requested room type is available
        if (availability.containsKey(roomType) && availability.get(roomType) > 0) {

            // 1. Generate a unique room ID for the given room type
            String roomId = generateRoomId(roomType);

            // 2. Add to sets to prevent double booking
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.get(roomType).add(roomId);

            // 3. Update inventory immediately after successful allocation
            inventory.updateAvailability(roomType, availability.get(roomType) - 1);

            // Print confirmation
            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() +
                    ", Room ID: " + roomId);
        } else {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName() +
                    ". No " + roomType + " rooms available.");
        }
    }

    /**
     * Generates a unique room ID for the given room type.
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        // Ensure the inner set is initialized for this room type
        assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());

        // Count currently assigned rooms of this type to append as a suffix
        int nextRoomNumber = assignedRoomsByType.get(roomType).size() + 1;
        return roomType + "-" + nextRoomNumber;
    }
}

/**
 * Use Case 6: Reservation Confirmation & Room Allocation
 * Description:
 * This class demonstrates how booking requests are confirmed and rooms
 * are allocated safely. It consumes booking requests in FIFO order
 * and updates inventory immediately.
 * @version 6.0
 */
public class UseCase6RoomAllocation {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Room Allocation Processing\n");

        // Initialize required services
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Create booking requests (Configured to match the exact output requested)
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Process queued booking requests
        while (queue.hasPendingRequests()) {
            Reservation request = queue.getNextRequest();
            allocationService.allocateRoom(request, inventory);
        }
    }
}
