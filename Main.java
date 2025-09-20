import java.util.List;
import java.util.Optional;

import models.*;
import repositories.*;
import services.*;
import utils.ConsoleUtils;

public class Main {
    private AuthService authService;
    private HotelService hotelService;
    private ReservationService reservationService;

    public Main() {
        ClientRepository clientRepo = new ClientRepositoryImpl();
        HotelRepository hotelRepo = new HotelRepositoryImpl();
        ReservationRepository reservationRepo = new ReservationRepositoryImpl();
        authService = new AuthService(clientRepo);
        hotelService = new HotelService(hotelRepo, reservationRepo);
        reservationService = new ReservationService(reservationRepo, hotelRepo);
    }

    public void run() {
        System.out.println("Welcome to Hotel Manager");
        authService.register("admin", "admin@admin.com", "admin123", Role.Admin);
        authService.register("Mohamed Karroumi", "Mohamed@karroumi.com", "Mohamed", Role.Client);
        while (true) {
            Optional<Client> user = authService.user();
            if (user.isEmpty()) {
                showWelcomeMenu();
            } else {
                showUserMenu(user.get());
            }
        }
    }

    private void showWelcomeMenu() {
        String[] options = {"Register", "Login", "Exit"};
        ConsoleUtils.displayMenu("Choose one option", options);

        int choice = ConsoleUtils.readInt("Choose an option");

        switch (choice) {
            case 1:
                handleRegister();
                break;
            case 2:
                handleLogin();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private void handleRegister() {
        try {
            System.out.println("Registering...");
            String fullName = ConsoleUtils.readString("Enter your fullName");
            String email = ConsoleUtils.readEmail("Enter your email");
            String password = ConsoleUtils.readString("Enter your password");
            authService.register(fullName, email, password, Role.Client);
            System.out.println("Registered succesfully");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void handleLogin() {
        try {
            System.out.println("Login in...");
            String email = ConsoleUtils.readEmail("Email");
            String password = ConsoleUtils.readString("password");
            authService.login(email, password);
            System.out.println("Logged in succusfuly");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void showUserMenu(Client user) {
        if (user.getRole() == Role.Client) {
            showClientMenu(user);
        } else {
            showAdminMenu(user);
        }
    }

    private void showClientMenu(Client user) {
        String[] options = {
                "List hotels",
                "Reserve room",
                "Cancel reservation",
                "Reservation history",
                "Update profile",
                "Change password",
                "Logout"
        };
        ConsoleUtils.displayMenu("Choose one option", options);
        int choice = ConsoleUtils.readInt("Enter option");

        switch (choice) {
            case 1:
                List<Hotel> hotels = hotelService.listAllHotels();
                for (Hotel hotel : hotels) {
                    System.out.println("- " + hotel.getName());
                }
                System.out.println();
                break;
            case 2:
                handleReserveRoom(user);
                break;
            case 3:
                handleCancelReservation(user);
                break;
            case 4:
                handleReservationHistory(user);
                break;
            case 5:
                handleUpdateProfile(user);
                break;
            case 6:
                handleChangePassword(user);
                break;
            case 7:
                authService.logout();
                System.out.println("Logged out successfully!");
                return;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    private void showAdminMenu(Client user) {
        String[] options = {
                "Create hotel",
                "List hotels",
                "Update hotel",
                "Delete hotel",
                "Reserve room",
                "Cancel reservation",
                "Reservation history",
                "Update profile",
                "Change password",
                "Logout"
        };
        ConsoleUtils.displayMenu("Choose one option", options);
        int choice = ConsoleUtils.readInt("Enter option");

        switch (choice) {
            case 1:
                // create hotel
                handleCreateHotel(user);
                break;

            case 2:
                // list hotels
                List<Hotel> hotels = hotelService.listAllHotels();
                for (Hotel hotel : hotels) {
                    System.out.println("- " + hotel.getName());
                }
                System.out.println();
                break;
            case 3:
                // update hotel
                handleUpdateHotel(user);
                break;
            case 4:
                // delete
                handleDeleteHotel(user);
                break;
            case 5:
                // reserve room
                handleReserveRoom(user);
                break;
            case 6:
                handleCancelReservation(user);
                break;
            case 7:
                handleReservationHistory(user);
                break;
            case 8:
                handleUpdateProfile(user);
                break;
            case 9:
                handleChangePassword(user);
                break;
            case 10:
                authService.logout();
                System.out.println("Logged out successfully!");
                return;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    //admin handlers
    private void handleCreateHotel(Client user) {
        try {
            String name = readHotelName();
            String address = readHotelAddress();
            int rooms = readHotelRooms();
            double rating = readHotelRating();

            hotelService.createHotel(user, name, address, rooms, rating);
            System.out.println("Hotel created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleUpdateHotel(Client user) {
        try {
            System.out.println("Select hotel:");
            String chosenId = chooseHotelId();
            String name = readHotelName();
            String address = readHotelAddress();
            int rooms = readHotelRooms();
            double rating = readHotelRating();
            hotelService.updateHotel(user, chosenId, name, address, rooms, rating);
            System.out.println("Hotel updated successfully");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteHotel(Client user) {
        try {
            System.out.println("Select hotel:");
            String chosenId = chooseHotelId();

            hotelService.deleteHotel(user, chosenId);
            System.out.println("Hotel deleted successfully");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    //shared handlers
    private void handleReserveRoom(Client user) {
        try {
            System.out.println("Select hotel:");
            String chosenId = chooseHotelId();
            reservationService.reserveRoom(user, chosenId, 1);
            System.out.println("Room reserved successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //utils
    private String chooseHotelId() {
        List<Hotel> hotelsList = hotelService.listAllHotels();
        Hotel[] hotels = hotelsList.toArray(new Hotel[0]);

        if(hotels.length == 0){
            throw new IllegalArgumentException("There are no hotels");
        }
        for (int i = 0; i < hotels.length; i++) {
            System.out.println((i + 1) + ". " + hotels[i].getName());
        }

        int index = ConsoleUtils.readInt("Enter the N of the hotel");
        if (index < 1 || index > hotels.length) {
            System.out.println("Choose a number between 1 and " + hotels.length);
            return chooseHotelId();
        }

        String id = hotels[index - 1].getId();
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Invalid hotel id");
        }
        return id;
    }

    private void handleCancelReservation(Client user) {
        try {
            List<models.Reservation> activeReservations = reservationService.getActiveReservations(user);
            if (activeReservations.isEmpty()) {
                System.out.println("You have no active reservations to cancel.");
                return;
            }

            System.out.println("Your active reservations:");
            for (int i = 0; i < activeReservations.size(); i++) {
                models.Reservation reservation = activeReservations.get(i);
                System.out.println((i + 1) + ". Reservation ID: " + reservation.getId() +
                    " - Hotel: " + reservation.getHotelID() + " - Nights: " + reservation.getNights());
            }

            int choice = ConsoleUtils.readInt("Enter reservation number to cancel");
            if (choice < 1 || choice > activeReservations.size()) {
                System.out.println("Invalid choice!");
                return;
            }

            models.Reservation reservationToCancel = activeReservations.get(choice - 1);
            reservationService.cancelReservation(user, reservationToCancel.getId());
            System.out.println("Reservation cancelled successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleReservationHistory(Client user) {
        try {
            List<models.Reservation> history = reservationService.getHistory(user);
            if (history.isEmpty()) {
                System.out.println("You have no reservation history.");
                return;
            }

            System.out.println("Your reservation history:");
            for (models.Reservation reservation : history) {
                System.out.println("- ID: " + reservation.getId() +
                    "\n | Nights: " + reservation.getNights() +
                    "\n | Date: " + reservation.getTimestamp() +
                    "\n | Status: " + reservation.getStatus());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleUpdateProfile(Client user) {
        try {
            System.out.println("Current profile:");
            System.out.println("Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());

            String newName = ConsoleUtils.readString("Enter new full name (current: " + user.getFullName() + ")");
            String newEmail = ConsoleUtils.readEmail("Enter new email (current: " + user.getEmail() + ")");

            user.setFullName(newName);
            authService.updateEmail(user, newEmail);
            System.out.println("Profile updated successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleChangePassword(Client user) {
        try {
            String newPassword = ConsoleUtils.readString("Enter new password");
            authService.updatePassword(user, newPassword);
            System.out.println("Password changed successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String readHotelName() {
        while (true) {
            String name = ConsoleUtils.readString("Hotel name");
            if (name.isBlank()) {
                System.out.println("Hotel name is empty!");
            } else if (name.length() < 3) {
                System.out.println("Hotel name is too short!");
            } else {
                return name;
            }
        }
    }

    private String readHotelAddress() {
        while (true) {
            String address = ConsoleUtils.readString("Address");
            if (address.isBlank()) {
                System.out.println("Address is empty!");
            } else if (address.length() < 3) {
                System.out.println("Address is too short!");
            } else {
                return address;
            }
        }
    }

    private int readHotelRooms() {
        while (true) {
            int rooms = ConsoleUtils.readInt("Number of rooms");
            if (rooms < 1) {
                System.out.println("Number of rooms must be greater than zero!");
            } else {
                return rooms;
            }
        }
    }

    private double readHotelRating() {
        while (true) {
            double rating = ConsoleUtils.readDouble("Rating");
            if (rating < 1 || rating > 5) {
                System.out.println("Rating must be between 1.0 and 5.0");
            } else {
                return rating;
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
