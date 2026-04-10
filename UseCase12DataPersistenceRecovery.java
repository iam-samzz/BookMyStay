import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

// --- Supporting Domain Class ---

class RoomInventory {
    // Using LinkedHashMap to maintain the exact insertion order (Single, Double, Suite)
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new LinkedHashMap<>();
    }

    public void initializeDefaultInventory() {
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

// --- New Classes for Use Case 12 ---

/**
 * Use Case 12: Data Persistence & System Recovery
 * Description:
 * This class is responsible for persisting critical system state to a plain text file.
 * It supports saving room inventory state and restoring inventory on system startup.
 * No database or serialization framework is used in this use case.
 *
 * @version 12.0
 */
class FilePersistenceService {

    /**
     * Saves room inventory state to a file.
     * Each line follows the format: roomType=availableCount
     *
     * @param inventory centralized room inventory
     * @param filePath path to persistence file
     */
    public void saveInventory(RoomInventory inventory, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Map<String, Integer> availability = inventory.getRoomAvailability();
            for (Map.Entry<String, Integer> entry : availability.entrySet()) {
                // Writing in the format: roomType=availableCount
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory to file: " + e.getMessage());
        }
    }

    /**
     * Loads room inventory state from a file.
     *
     * @param inventory centralized room inventory
     * @param filePath path to persistence file
     */
    public void loadInventory(RoomInventory inventory, String filePath) {
        File file = new File(filePath);

        // Handle missing file gracefully to allow system to start fresh
        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            inventory.initializeDefaultInventory();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean hasData = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String roomType = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    inventory.updateAvailability(roomType, count);
                    hasData = true;
                }
            }

            // If file was empty or corrupted
            if (!hasData) {
                System.out.println("No valid inventory data found. Starting fresh.");
                inventory.initializeDefaultInventory();
            } else {
                System.out.println("Inventory successfully loaded from file.");
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("No valid inventory data found. Starting fresh.");
            inventory.initializeDefaultInventory();
        }
    }
}

/**
 * Use Case 12: Data Persistence & System Recovery
 * Description:
 * This class demonstrates how system state can be restored after an application restart.
 * Inventory data is loaded from a file before any booking operations occur.
 *
 * @version 12.0
 */
public class UseCase12DataPersistenceRecovery {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("System Recovery\n");

        // 1. Initialize core system components
        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistenceService = new FilePersistenceService();
        String storageFile = "inventory_data.txt";

        // 2. Attempt to load inventory from file before operations begin
        persistenceService.loadInventory(inventory, storageFile);

        // 3. Display current loaded (or default) inventory
        System.out.println("\nCurrent Inventory:");
        Map<String, Integer> currentAvailability = inventory.getRoomAvailability();
        for (Map.Entry<String, Integer> entry : currentAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();

        // 4. Save state back to the file (simulating shutdown persistence)
        persistenceService.saveInventory(inventory, storageFile);
    }
}