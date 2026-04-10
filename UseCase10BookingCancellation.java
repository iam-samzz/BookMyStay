import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// --- Supporting Domain Class ---

class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        // Default starting inventory
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

// --- New Classes for Use Case 10 ---

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Description:
 * This class is responsible for handling booking cancellations.
 * It ensures that:
 * - Cancelled room IDs are tracked
 * - Inventory is restored correctly
 * - Invalid cancellations are prevented
 *
 * A stack is used to model rollback behavior.
 *
 * @version 10.0
 */
class CancellationService {

    // Maps reservation ID to the room type to know what to restore
    private Map<String, String> reservationRoomTypeMap;

    // Stack to track recently released room IDs (LIFO behavior)
    private Stack<String> rollbackHistory;

    /** Initializes cancellation tracking structures. */
    public CancellationService() {
        reservationRoomTypeMap = new HashMap<>();
        rollbackHistory = new Stack<>();
    }

    /**
     * Registers a confirmed booking.
     * This method simulates storing confirmation data that will later be required for cancellation.
     *
     * @param reservationId confirmed reservation ID
     * @param roomType allocated room type
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels a confirmed booking and restores inventory safely.
     *
     * @param reservationId reservation to cancel
     * @param inventory centralized room inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {
        // Validate reservation existence before performing rollback
        if (reservationRoomTypeMap.containsKey(reservationId)) {
            String roomType = reservationRoomTypeMap.get(reservationId);

            // 1. Release room ID to stack (Rollback history)
            rollbackHistory.push(reservationId);

            // 2. Remove from active bookings mapping
            reservationRoomTypeMap.remove(reservationId);

            // 3. Restore inventory counts accurately and immediately
            Map<String, Integer> availability = inventory.getRoomAvailability();
            int currentCount = availability.getOrDefault(roomType, 0);
            inventory.updateAvailability(roomType, currentCount + 1);

            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        } else {
            System.out.println("Cancellation failed: Reservation ID not found.");
        }
    }

    /**
     * Displays recently cancelled reservations.
     * This method helps visualize rollback order.
     */
    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");

        // Iterate through the stack from top (most recent) to bottom
        for (int i = rollbackHistory.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + rollbackHistory.get(i));
        }
    }
}

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Description:
 * This class demonstrates how confirmed bookings can be cancelled safely.
 * Inventory is restored and rollback history is maintained.
 *
 * @version 10.0
 */
public class UseCase10BookingCancellation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Booking Cancellation\n");

        // 1. Initialize System Components
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // 2. Simulate an existing environment where a room was already booked
        // If 1 Single room was booked, availability drops from 5 to 4
        inventory.updateAvailability("Single", 4);

        // Register the existing booking so the system knows about it
        String reservationIdToCancel = "Single-1";
        cancellationService.registerBooking(reservationIdToCancel, "Single");

        // 3. Process the Cancellation
        cancellationService.cancelBooking(reservationIdToCancel, inventory);

        // 4. Display the Rollback History
        cancellationService.showRollbackHistory();

        // 5. Display the Updated Inventory to prove restoration
        System.out.println("\nUpdated Single Room Availability: " + inventory.getRoomAvailability().get("Single"));
    }
}