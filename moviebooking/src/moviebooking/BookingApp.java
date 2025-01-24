package moviebooking;
class InsufficientSeatsException extends Exception {
    public InsufficientSeatsException(String message) {
        super(messagee);
    }
}
final class BookingState {
    private final int availableSeats;
    public BookingState(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    public int getAvailableSeats() {
        return availableSeats;
    }
    public BookingState bookSeats(int seatsToBook) throws InsufficientSeatsException {
        if (seatsToBook <= availableSeats) {
            return new BookingState(availableSeats - seatsToBook);
        } else {
            throw new InsufficientSeatsException("Not enough seats available.");
        }
    }
}
class MovieBookingApp {
    private BookingState bookingState = new BookingState(50); 
    public synchronized void bookSeats(String user, int seatsToBook) {
        try {
            bookingState = bookingState.bookSeats(seatsToBook);
            System.out.println(user + " successfully booked " + seatsToBook + " seats.");
        } catch (InsufficientSeatsException e) {
            System.out.println(user + " booking failed: " + e.getMessage());
        }
        System.out.println("Seats remaining: " + bookingState.getAvailableSeats());
    }
}
public class BookingApp {
    public static void main(String[] args) {
        MovieBookingApp app = new MovieBookingApp(); // Shared object
        Thread userA = new Thread(() -> app.bookSeats("User  A", 42));
        Thread userB = new Thread(() -> app.bookSeats("User  B", 12));
        userA.start();
        userB.start();
        try {
            userA.join();
            userB.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }
}
