import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomAllocationService {
    private Map<String, Integer> availableRooms;
    private Map<String, Set<String>> allocatedRooms;

    public RoomAllocationService() {
        availableRooms = new HashMap<>();
        availableRooms.put("Single", 2);

        allocatedRooms = new HashMap<>();
        allocatedRooms.put("Single", new LinkedHashSet<>());
    }

    public void allocateRoom(Reservation request) {
        String roomType = request.getRoomType();
        int availableCount = availableRooms.getOrDefault(roomType, 0);

        if (availableCount > 0) {
            Set<String> allocated = allocatedRooms.get(roomType);
            String roomId = roomType.substring(0, 3).toUpperCase() + "-" + (100 + allocated.size() + 1);

            availableRooms.put(roomType, availableCount - 1);
            allocated.add(roomId);

            System.out.println("ALLOCATED: " + request.getGuestName() + " assigned Room ID: " + roomId);
        } else {
            System.out.println("FAILED: No " + roomType + " rooms available for " + request.getGuestName());
        }
    }

    public void displayAllocationReport() {
        System.out.println("\n--- Final Allocation Report ---");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Allocated: " + entry.getValue());
        }
    }
}

public class BookMyStayApp  {
    public static void main(String[] args) {
        System.out.println("--- Hotel Booking System UC6: Room Allocation ---\n");

        RoomAllocationService allocationService = new RoomAllocationService();
        Queue<Reservation> requestQueue = new LinkedList<>();

        requestQueue.offer(new Reservation("Alice", "Single"));
        requestQueue.offer(new Reservation("Bob", "Single"));
        requestQueue.offer(new Reservation("Charlie", "Single"));

        System.out.println("Processing " + requestQueue.size() + " queued requests...\n");

        while (!requestQueue.isEmpty()) {
            Reservation request = requestQueue.poll();
            allocationService.allocateRoom(request);
        }

        allocationService.displayAllocationReport();
    }
}