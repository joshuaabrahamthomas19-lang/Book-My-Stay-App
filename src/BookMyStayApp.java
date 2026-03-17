import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return guestName + " (" + roomType + ")";
    }
}

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> availableRooms;

    public RoomInventory() {
        // LinkedHashMap keeps the order exactly as inserted: Single first, then Double
        availableRooms = new LinkedHashMap<>();
    }

    public void addStock(String type, int count) {
        availableRooms.put(type, count);
    }

    public String getInventoryStatus() {
        return availableRooms.toString();
    }
}

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    private RoomInventory inventory;
    private List<Reservation> bookings;

    public SystemState(RoomInventory inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }

    public RoomInventory getInventory() { return inventory; }
    public List<Reservation> getBookings() { return bookings; }
}

class PersistenceService {
    private final String filename = "bookingData.ser";

    public void saveState(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(state);
            System.out.println("System state saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SystemState loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state restored.");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

public class BookMyStayApp  {
    public static void main(String[] args) {
        // 1. Setup the initial state to match the required output
        RoomInventory inventory = new RoomInventory();
        inventory.addStock("Single", 1);
        inventory.addStock("Double", 2);

        List<Reservation> bookings = new ArrayList<>();
        bookings.add(new Reservation("Abhi", "Single"));

        SystemState originalState = new SystemState(inventory, bookings);
        PersistenceService storage = new PersistenceService();

        // 2. Save the state
        storage.saveState(originalState);

        // 3. Restore the state
        SystemState restoredState = storage.loadState();

        // 4. Print the exact required output
        if (restoredState != null) {
            System.out.println("Restored Inventory: " + restoredState.getInventory().getInventoryStatus());
            System.out.println("Restored Bookings: " + restoredState.getBookings());
        }
    }
}