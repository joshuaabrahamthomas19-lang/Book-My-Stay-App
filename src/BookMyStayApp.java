import java.util.HashMap;
import java.util.Map;

/**
 * DOMAIN MODELS
 */
abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;
    protected String type;

    public Room(String type, int numberOfBeds, int squareFeet, double pricePerNight) {
        this.type = type;
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayDetails(int availableCount) {
        System.out.println(type + ": Beds: " + numberOfBeds + " | Size: " + squareFeet + " sqft | Price: " + pricePerNight + " | Left: " + availableCount);
    }
    public String getType() { return type; }
}

class SingleRoom extends Room { public SingleRoom() { super("Single", 1, 250, 1500.0); } }
class DoubleRoom extends Room { public DoubleRoom() { super("Double", 2, 400, 2500.0); } }

/**
 * INVENTORY MANAGEMENT
 */
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 2); // Setting low for demonstration
        roomAvailability.put("Double", 3);
    }

    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    public void reduceInventory(String roomType) {
        int currentCount = roomAvailability.get(roomType);
        roomAvailability.put(roomType, currentCount - 1);
    }
}

/**
 * BOOKING SERVICE - Use Case 5 (Mutation)
 */
class BookingService {
    /**
     * Attempts to book a room. If available, reduces inventory.
     */
    public void processBooking(String guestName, Room room, RoomInventory inventory) {
        Map<String, Integer> availability = inventory.getRoomAvailability();
        String type = room.getType();

        System.out.println("\n>>> Processing Booking for: " + guestName);

        if (availability.get(type) > 0) {
            inventory.reduceInventory(type);
            System.out.println("SUCCESS: " + type + " booked for " + guestName);
            System.out.println("New " + type + " inventory: " + inventory.getRoomAvailability().get(type));
        } else {
            System.out.println("FAILED: No " + type + "s available for " + guestName);
        }
    }
}


public class BookMyStayApp {
    public static void main(String[] args) {
        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService();
        Room single = new SingleRoom();

        System.out.println("--- Initial Inventory ---");
        single.displayDetails(inventory.getRoomAvailability().get("Single"));

        // Guest 1 tries to book
        bookingService.processBooking("Alice", single, inventory);

        // Guest 2 tries to book
        bookingService.processBooking("Bob", single, inventory);

        // Guest 3 tries to book (Should fail as inventory was 2)
        bookingService.processBooking("Charlie", single, inventory);

        System.out.println("\n--- Final Inventory Status ---");
        single.displayDetails(inventory.getRoomAvailability().get("Single"));
    }
}
