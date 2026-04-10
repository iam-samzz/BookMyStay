import java.util.ArrayList;
import java.util.List;

/**
 * Domain class representing a booking intent.
 */
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

/**
 * Use Case 8: Booking History & Reporting
 * Description:
 * This class maintains a record of confirmed reservations.
 * It provides ordered storage for historical and reporting purposes.
 * @version 8.0
 */
class BookingHistory {
    /** List that stores confirmed reservations. */
    private List<Reservation> confirmedReservations;

    /** Initializes an empty booking history. */
    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    /**
     * Adds a confirmed reservation to booking history.
     * @param reservation confirmed booking
     */
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    /**
     * Returns all confirmed reservations.
     * @return list of reservations
     */
    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

/**
 * Use Case 8: Booking History & Reporting
 * Description:
 * This class generates reports from booking history data.
 * Reporting logic is separated from data storage.
 * @version 8.0
 */
class BookingReportService {
    /**
     * Displays a summary report of all confirmed bookings.
     * @param history booking history
     */
    public void generateReport(BookingHistory history) {
        System.out.println("Booking History Report");
        List<Reservation> reservations = history.getConfirmedReservations();

        for (Reservation res : reservations) {
            System.out.println("Guest: " + res.getGuestName() + ", Room Type: " + res.getRoomType());
        }
    }
}

/**
 * Use Case 8: Booking History & Reporting
 * Description:
 * This class demonstrates how confirmed bookings are stored and reported.
 * The system maintains an ordered audit trail of reservations.
 * @version 8.8
 */
public class UseCase8BookingHistoryReport {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Booking History and Reporting\n");

        // 1. Initialize Booking History and Reporting Service
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // 2. Simulate completed bookings to add to the history
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vanmathi", "Suite"));

        // 3. Generate the report using the service
        reportService.generateReport(history);
    }
}