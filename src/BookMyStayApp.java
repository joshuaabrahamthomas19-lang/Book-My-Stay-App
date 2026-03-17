import java.util.HashMap;
import java.util.Map;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

class RoomInventory {
    private Map<String, Integer> availableRooms;

    public RoomInventory() {
        availableRooms = new HashMap<>();
        availableRooms.put("Single", 1);
        availableRooms.put("Double", 5);
    }

    public boolean isValidRoomType(String type) {
        return availableRooms.containsKey(type);
    }

    public boolean isRoomAvailable(String type) {
        return availableRooms.getOrDefault(type, 0) > 0;
    }

    public void reduceInventory(String type) {
        if (isRoomAvailable(type)) {
            availableRooms.put(type, availableRooms.get(type) - 1);
        }
    }
}

class BookingValidator {
    public void validate(Reservation reservation, RoomInventory inventory) throws InvalidBookingException {
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected: " + reservation.getRoomType());
        }
        if (!inventory.isRoomAvailable(reservation.getRoomType())) {
            throw new InvalidBookingException("Room type is out of stock: " + reservation.getRoomType());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Error Handling and Validation");
        System.out.println("-----------------------------\n");

        RoomInventory inventory = new RoomInventory();
        BookingValidator validator = new BookingValidator();

        Reservation[] requests = {
                new Reservation("Abhi", "Single"),
                new Reservation("Subha", "Penthouse"),
                new Reservation("Vanmathi", "Single")
        };

        for (Reservation req : requests) {
            try {
                validator.validate(req, inventory);
                inventory.reduceInventory(req.getRoomType());
                System.out.println("SUCCESS: Booking confirmed for " + req.getGuestName() + " (" + req.getRoomType() + ")");
            } catch (InvalidBookingException e) {
                System.out.println("FAILED: Booking failed for " + req.getGuestName() + " -> " + e.getMessage());
            }
        }
    }
}
