import java.util.LinkedList;
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

    @Override
    public String toString() {
        return "Reservation Request -> Guest: " + guestName + " | Room: " + roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        this.requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation request) {
        requestQueue.offer(request);
        System.out.println("Queued: " + request.getGuestName() + " is waiting for a " + request.getRoomType());
    }

    public boolean hasPendingRequest() {
        return !requestQueue.isEmpty();
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public void displayQueue() {
        System.out.println("\n--- Current Booking Queue (FIFO Order) ---");
        if (requestQueue.isEmpty()) {
            System.out.println("The queue is empty.");
        } else {
            for (Reservation res : requestQueue) {
                System.out.println(res);
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("--- Hotel Booking System UC5: Request Intake ---\n");

        BookingRequestQueue queueSystem = new BookingRequestQueue();

        queueSystem.addRequest(new Reservation("Alice", "Single"));
        queueSystem.addRequest(new Reservation("Bob", "Double"));
        queueSystem.addRequest(new Reservation("Charlie", "Suite"));

        queueSystem.displayQueue();

        System.out.println("\n[System Note]: Requests are stored. No inventory mutation has occurred at this stage.");

        System.out.println("\n--- Processing Requests ---");
        while (queueSystem.hasPendingRequest()) {
            Reservation next = queueSystem.getNextRequest();
            System.out.println("Processing: " + next.getGuestName());
        }
    }
}