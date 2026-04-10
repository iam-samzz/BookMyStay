import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

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
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }
}

// --- New Classes for Use Case 9 ---

/**
 * Use Case 9: Error Handling & Validation
 * Description:
 * This custom exception represents invalid booking scenarios in the system.
 * Using a domain-specific exception makes error handling clearer and safer.
 * @version 9.0
 */
class InvalidBookingException extends Exception {
    /**
     * Creates an exception with a descriptive error message.
     * @param message error description
     */
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Use Case 9: Error Handling & Validation
 * Description:
 * This class is responsible for validating booking requests before they are processed.
 * All validation rules are centralized to avoid duplication and inconsistency.
 * @version 9.0
 */
class ReservationValidator {
    /**
     * Validates booking input provided by the user.
     *
     * @param guestName name of the guest
     * @param roomType requested room type
     * @param inventory centralized inventory
     * @throws InvalidBookingException if validation fails
     */
    public void validate(String guestName, String roomType, RoomInventory inventory) throws InvalidBookingException {
        // Validate Guest Name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate Room Type (Case Sensitive Match)
        if (!inventory.getRoomAvailability().containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }
}

/**
 * Use Case 9: Error Handling & Validation
 * Description:
 * This class demonstrates how user input is validated before a booking is processed.
 * The system: Accepts user input, Validates input centrally, and Handles errors gracefully.
 * @version 9.0
 */
public class UseCase9ErrorHandlingValidation {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Booking Validation\n");

        Scanner scanner = new Scanner(System.in);

        // Initialize required components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Accept user input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate the input centrally
            validator.validate(guestName, roomType, inventory);

            // If validation passes, process the booking
            bookingQueue.addRequest(new Reservation(guestName, roomType));
            System.out.println("Booking successfully added to the queue!");

        } catch (InvalidBookingException e) {
            // Handle domain-specific validation errors gracefully
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}