import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * This class represents an optional service that can be added to a confirmed reservation.
 * Examples: Breakfast, Spa, Airport Pickup.
 * @version 7.0
 */
class Service {
    /** Name of the service. */
    private String serviceName;

    /** Cost of the service. */
    private double cost;

    /**
     * Creates a new add-on service.
     * @param serviceName name of the service
     * @param cost cost of the service
     */
    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    /** @return service name */
    public String getServiceName() {
        return serviceName;
    }

    /** @return service cost */
    public double getCost() {
        return cost;
    }
}

/**
 * CLASS AddOnServiceManager
 * Use Case 7: Add-On Service Selection
 * Description:
 * This class manages optional services associated with confirmed reservations.
 * It supports attaching multiple services to a single reservation.
 * @version 7.0
 */
class AddOnServiceManager {
    /**
     * Maps reservation ID to selected services.
     * Key -> Reservation ID
     * Value -> List of selected services
     */
    private Map<String, List<Service>> servicesByReservation;

    /**
     * Initializes the service manager.
     */
    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    /**
     * Attaches a service to a reservation.
     * @param reservationId confirmed reservation ID
     * @param service add-on service
     */
    public void addService(String reservationId, Service service) {
        // If there is no list for this reservation ID yet, create one
        servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());
        // Add the service to the reservation's list
        servicesByReservation.get(reservationId).add(service);
    }

    /**
     * Calculates total add-on cost for a reservation.
     * @param reservationId reservation ID
     * @return total service cost
     */
    public double calculateTotalServiceCost(String reservationId) {
        double totalCost = 0.0;
        List<Service> services = servicesByReservation.getOrDefault(reservationId, new ArrayList<>());

        for (Service service : services) {
            totalCost += service.getCost();
        }

        return totalCost;
    }
}

/**
 * Use Case 7: Add-On Service Selection
 * Description:
 * This class demonstrates how optional services can be attached to a confirmed booking.
 * Services are added after room allocation and do not affect inventory.
 * @version 7.0
 */
public class UseCase7AddOnServiceSelection {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("Add-On Service Selection\n");

        // 1. Initialize the Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // 2. Create some available Add-On Services
        Service breakfast = new Service("Breakfast", 500.0);
        Service spa = new Service("Spa Massage", 1000.0);

        // 3. Assume a confirmed reservation exists from Use Case 6
        String confirmedReservationId = "Single-1";

        // 4. Attach selected services to the reservation
        serviceManager.addService(confirmedReservationId, breakfast);
        serviceManager.addService(confirmedReservationId, spa);

        // 5. Calculate and display the total cost
        double totalAddOnCost = serviceManager.calculateTotalServiceCost(confirmedReservationId);

        System.out.println("Reservation ID: " + confirmedReservationId);
        System.out.println("Total Add-On Cost: " + totalAddOnCost);
    }
}