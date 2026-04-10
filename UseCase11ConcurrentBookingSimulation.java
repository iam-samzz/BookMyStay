import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// --- Supporting Domain Classes ---

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

// Queue needs to be thread-safe for concurrent access
class BookingRequestQueue {
    private Queue<Reservation> requestQueue = new LinkedList<>();

    public synchronized void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public synchronized Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public synchronized boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

// Allocation Service needs to be synchronized to prevent double-booking
class RoomAllocationService {
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

    public synchronized void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.containsKey(roomType) && availability.get(roomType) > 0) {
            assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());

            int nextRoomNumber = assignedRoomsByType.get(roomType).size() + 1;
            String roomId = roomType + "-" + nextRoomNumber;

            allocatedRoomIds.add(roomId);
            assignedRoomsByType.get(roomType).add(roomId);
            inventory.updateAvailability(roomType, availability.get(roomType) - 1);

            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() +
                    ", Room ID: " + roomId);
        } else {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName() +
                    ". No " + roomType + " rooms available.");
        }
    }
}

// --- New Classes for Use Case 11 ---

/**
 * Use Case 11: Concurrent Booking Simulation
 * Description:
 * This class processes booking requests in a multi-threaded environment.
 * @version 11.0
 */
class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue queue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(BookingRequestQueue queue, RoomInventory inventory, RoomAllocationService allocationService) {
        this.queue = queue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation request;
            // Synchronize the check and retrieval to prevent race conditions on the queue
            synchronized (queue) {
                if (!queue.hasPendingRequests()) {
                    break;
                }
                request = queue.getNextRequest();
            }

            if (request != null) {
                // Simulate processing time slightly to force concurrent contention
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                // Thread-safe allocation
                allocationService.allocateRoom(request, inventory);
            }
        }
    }
}

/**
 * Use Case 11: Concurrent Booking Simulation
 * Description:
 * This class demonstrates how concurrent access to shared resources can lead to
 * inconsistent system state and shows how synchronization ensures correctness
 * under multi-user conditions.
 * @version 11.0
 */
public class UseCase11ConcurrentBookingSimulation {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Concurrent Booking Simulation\n");

        // 1. Initialize System Components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService();

        // 2. Add multiple booking requests to simulate peak demand
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        // 3. Create two threads sharing the exact same resources
        Thread t1 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));

        // 4. Start concurrent processing
        t1.start();
        t2.start();

        // 5. Wait for both threads to finish
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }
    }
}