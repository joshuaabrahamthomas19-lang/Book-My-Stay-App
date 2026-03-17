import java.util.*;

class RoomAllocationService {

    // Stores all allocated room IDs to prevent duplicate assignments.
    private Set<String> allocatedRoomIds;

    // Key -> Room type | Value -> Set of assigned room IDs
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        this.allocatedRoomIds = new HashSet<>();
        this.assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();

        // Generate a unique ID (e.g., Single-1)
        String roomId = generateRoomId(roomType);

        // Update tracking structures
        allocatedRoomIds.add(roomId);
        assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

        // Update the centralized inventory
        inventory.reduceInventory(roomType);

        System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() +
                ", Room ID: " + roomId);
    }
    
    private String generateRoomId(String roomType) {
        // Find how many rooms of this type are already assigned and add 1
        int currentCount = assignedRoomsByType.getOrDefault(roomType, new HashSet<>()).size();
        return roomType + "-" + (currentCount + 1);
    }
}

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
 * Model representing the Centralized Room Inventory
 */
class RoomInventory {
    private Map<String, Integer> stock = new HashMap<>();

    public void addStock(String type, int count) {
        stock.put(type, count);
    }

    public void reduceInventory(String type) {
        if (stock.containsKey(type) && stock.get(type) > 0) {
            stock.put(type, stock.get(type) - 1);
        }
    }
}

public class UseCase6RoomAllocation {

    public static void main(String[] args) {
        System.out.println("Room Allocation Processing");

        // 1. Setup Service and Inventory
        RoomAllocationService allocationService = new RoomAllocationService();
        RoomInventory inventory = new RoomInventory();
        inventory.addStock("Single", 10);
        inventory.addStock("Suite", 5);

        // 2. Initialize FIFO Queue for Booking Requests
        Queue<Reservation> bookingRequests = new LinkedList<>();
        bookingRequests.add(new Reservation("Abhi", "Single"));
        bookingRequests.add(new Reservation("Subha", "Single"));
        bookingRequests.add(new Reservation("Vanmathi", "Suite"));

        // 3. Process requests until the queue is empty
        while (!bookingRequests.isEmpty()) {
            Reservation currentRequest = bookingRequests.poll();
            allocationService.allocateRoom(currentRequest, inventory);
        }
    }
}
