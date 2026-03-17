import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
        availableRooms = new LinkedHashMap<>();
    }

    public void addStock(String type, int count) {
        availableRooms.put(type, count);
    }

    public boolean isAvailable(String type) {
        return availableRooms.getOrDefault(type, 0) > 0;
    }

    public void reduceInventory(String type) {
        if (isAvailable(type)) {
            availableRooms.put(type, availableRooms.get(type) - 1);
        }
    }

    public String getInventoryStatus() {
        return availableRooms.toString();
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private final RoomInventory inventory;
    private final Queue<Reservation> requests;

    public ConcurrentBookingProcessor(RoomInventory inventory, Queue<Reservation> requests) {
        this.inventory = inventory;
        this.requests = requests;
    }

    @Override
    public void run() {
        while (true) {
            Reservation request;

            synchronized (requests) {
                if (requests.isEmpty()) {
                    break;
                }
                request = requests.poll();
            }

            synchronized (inventory) {
                if (inventory.isAvailable(request.getRoomType())) {
                    inventory.reduceInventory(request.getRoomType());
                    System.out.println(Thread.currentThread().getName() + " successfully booked a " + request.getRoomType() + " room for " + request.getGuestName());
                } else {
                    System.out.println(Thread.currentThread().getName() + " failed to book a " + request.getRoomType() + " room for " + request.getGuestName() + " (Out of Stock)");
                }
            }
        }
    }
}

public class BookMyStayApp  {
    public static void main(String[] args) {
        System.out.println("Concurrent Booking Simulation\n");

        RoomInventory inventory = new RoomInventory();
        inventory.addStock("Single", 1);
        inventory.addStock("Double", 2);

        Queue<Reservation> requests = new LinkedList<>();
        requests.add(new Reservation("Abhi", "Single"));
        requests.add(new Reservation("Subha", "Double"));
        requests.add(new Reservation("Vanmathi", "Single"));

        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(inventory, requests);

        Thread t1 = new Thread(processor, "Thread-0");
        Thread t2 = new Thread(processor, "Thread-1");
        Thread t3 = new Thread(processor, "Thread-2");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Inventory: " + inventory.getInventoryStatus());
    }
}