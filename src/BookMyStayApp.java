import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

class RoomInventory {
    private Map<String, Integer> availableRooms;

    public RoomInventory() {
        // LinkedHashMap maintains insertion order for predictable output
        availableRooms = new LinkedHashMap<>();
        availableRooms.put("Single", 1);
        availableRooms.put("Suite", 2);
    }

    public void reduceInventory(String type) {
        if (availableRooms.containsKey(type) && availableRooms.get(type) > 0) {
            availableRooms.put(type, availableRooms.get(type) - 1);
        }
    }

    public void increaseInventory(String type) {
        if (availableRooms.containsKey(type)) {
            availableRooms.put(type, availableRooms.get(type) + 1);
        }
    }

    public String getInventoryStatus() {
        return availableRooms.toString();
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
}

class CancellationService {
    private Stack<String> cancelledRooms;

    public CancellationService() {
        this.cancelledRooms = new Stack<>();
    }

    public void cancelBooking(Reservation reservation, RoomInventory inventory) {
        cancelledRooms.push(reservation.getRoomId());

        inventory.increaseInventory(reservation.getRoomType());

        System.out.println("Guest " + reservation.getGuestName() + " cancelled the booking.");
        System.out.println("Rolled back room: " + cancelledRooms.peek());
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Booking Cancellation and Rollback");

        RoomInventory inventory = new RoomInventory();
        System.out.println("Original Inventory: " + inventory.getInventoryStatus());

        Reservation res = new Reservation("Subha", "Single", "Single-1");
        inventory.reduceInventory("Single");

        System.out.println("Guest Subha booked a Single room.");
        System.out.println("Inventory after booking: " + inventory.getInventoryStatus());

        CancellationService cancellationService = new CancellationService();
        cancellationService.cancelBooking(res, inventory);

        System.out.println("Inventory after cancellation: " + inventory.getInventoryStatus());
    }
}