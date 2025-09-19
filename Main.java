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
        while (true) {
            if (user.getRole() == Role.Client) {
                showClientMenu(user);
            } else {
                showAdminMenu(user);
            }
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

            default:
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
            default:
                break;
        }
    }

    //admin handlers
    private void handleCreateHotel(Client user) {
        try {

            String name = ConsoleUtils.readString("Hotel name");
            String address = ConsoleUtils.readString("Address");
            int rooms = ConsoleUtils.readInt("Number of rooms");
            double rating = ConsoleUtils.readDouble("Rating");
            hotelService.createHotel(user, name, address, rooms, rating);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleUpdateHotel(Client user) {
        try {
            System.out.println("Select hotel:");
            String chosenId = chooseHotelId();
            String name = ConsoleUtils.readString("Hotel name");
            String address = ConsoleUtils.readString("Address");
            int rooms = ConsoleUtils.readInt("Number of rooms");
            double rating = ConsoleUtils.readDouble("Rating");
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

        for (int i = 0; i < hotels.length; i++) {
            System.out.println((i + 1) + ". " + hotels[i].getName());
        }

        int index = ConsoleUtils.readInt("Enter the N of the hotel");
        if (index >= hotels.length) {
            System.out.println("Choose a number between 1 and " + (hotels.length - 1));
        }

        String id = hotels[index].getId();
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Invalid hotel id");
        }
        return id;
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
