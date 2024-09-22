package booking;

import railway.*;
import dsImplimentation.Queue;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    // Color code (ANSI Foreground..)
    public static String RED = "\u001B[31m";
    public static String GREEN = "\u001B[32m";
    public static String YELLOW = "\u001B[33m";
    public static String BLUE = "\u001B[34m";
    public static String MAGENTA = "\u001B[35m";
    public static String CYAN = "\u001B[36m";
    public static String BEIGE = "\u001B[38;5;187m";
    public static String RESET = "\u001B[0m";

    public static void main(String[] args) throws Exception {
        RailwayBookingSystem railwayBookingSystem = new RailwayBookingSystem();
        Scanner sc = new Scanner(System.in);
        UserData UserData = new UserData();
        List<User> users = UserData.getAllUsers();
        for (User user : users) {
            railwayBookingSystem.createUser(user.getUsername(), user.getPassword(), user.getName(), user.getEmail(),
                    user.getPhoneNumber());
        }

        System.out.println(MAGENTA + "=============================================");
        System.out.println("=========== Railway Booking System ==========");
        System.out.println("=============================================" + RESET);

        Thread.sleep(1000);

        int choice = 0;
        do {
            try {
                System.out.println("---------------------------");
                System.out.println("1. Admin Portal"); // Entering The Admin Portal...
                System.out.println("2. User Portal"); // Entering The User Portal...
                System.out.println("3. Exit The Program"); // Exiting The Whole Program...
                System.out.println("---------------------------");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                if (choice == 1) {
                    adminLogin(railwayBookingSystem);
                } else if (choice == 2) {
                    userPortal(railwayBookingSystem);
                } else if (choice == 3) {
                    System.out.println(GREEN + "Thank You For Using Railway Booking System.");
                    System.out.println("Please Visit Again !" + RESET);
                    Thread.sleep(3000);
                } else {
                    System.out.println(RED + "Invalid choice" + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please Enter Integer Value." + RESET);
                sc.next();
            }
        } while (choice != 3);
    }

    private static boolean adminLogin(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter admin username: ");
        String username = sc.next();
        System.out.print("Enter admin password: ");
        String password = sc.next();

        if (username.equalsIgnoreCase("Krish") && password.equalsIgnoreCase("Krish@123")) {
            System.out.println("Admin logged in successfully.");
            adminPortal(railwayBookingSystem);
            return true;
        } else {
            System.out.println(RED + "Invalid admin credentials. Please try again." + RESET);
            return false;
        }
    }

    private static void adminPortal(RailwayBookingSystem railwayBookingSystem) {
        int choice = 0;
        do {
            try {
                System.out.println("---------------------------");
                System.out.println("===== Admin Portal =====");
                System.out.println("1. Create Train"); // Add Train Using SQL Procedure.
                System.out.println("2. Update Train"); // Update Train Using SQL Query.
                System.out.println("3. Delete Train"); // Delete Train Using SQL Procedure.
                System.out.println("4. View All Trains"); // View Train Using ArrayList Method.
                System.out.println("5. View All Bookings"); // View All Booking Using SQL Query.
                System.out.println("6. View All User"); // View All User Using SQL Procedure.
                System.out.println("7. View Last Signed Up User"); // View who's Last Signup By
                                                                   // New User Using Stack Class
                System.out.println("8. Remove Last Signed Up User"); // Remove Last SignUp User Using Stack.
                System.out.println("9. Exit"); // Exiting The Admin Portal...
                System.out.println("---------------------------");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        createTrain(railwayBookingSystem);
                        break;
                    case 2:
                        updateTrain(railwayBookingSystem);
                        break;
                    case 3:
                        deleteTrain(railwayBookingSystem);
                        break;
                    case 4:
                        viewAllTrains(railwayBookingSystem);
                        break;
                    case 5:
                        viewAllBookings(railwayBookingSystem);
                        break;
                    case 6:
                        railwayBookingSystem.viewAllPassenger();
                        break;
                    case 7:
                        railwayBookingSystem.viewLastSignedUpUser();
                        break;
                    case 8:
                        railwayBookingSystem.removeLastSignedUpUser();
                        break;
                    case 9:
                        System.out.println(BLUE + "Exiting Admin Portal" + RESET);
                        try {
                            Thread.sleep(1000); // Use Thread-Sleep For 1 Second.
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println(RED + "Invalid choice" + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please Enter Integer Value." + RESET);
                sc.next();
            }
        } while (choice != 9);
    }

    private static void userPortal(RailwayBookingSystem railwayBookingSystem) {
        while (true) {
            try {
                System.out.println("---------------------------");
                System.out.println("===== User Portal =====");
                System.out.println("1. Existing User Login"); // Login Wia Already Signup User
                                                              // Also Signup Details Add In users.txt File
                System.out.println("2. Signup"); // Signup Using Thread Runnable.
                System.out.println("3. Logout"); // Logout User Interface.
                System.out.println("---------------------------");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        login(railwayBookingSystem);
                        break;
                    case 2:
                        signup(railwayBookingSystem);
                        break;
                    case 3:
                        System.out.println(RED + "Logging out. Goodbye!");
                        System.out.println("Thank you for using our Railway Booking System." + RESET);
                        try {
                            Thread.sleep(2000); // Use Thread-Sleep For 2 Second.
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    default:
                        System.out.println(RED + "Invalid choice. Please try again." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please Enter Integer Value." + RESET);
                sc.next();
            }
        }
    }

    private static boolean login(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();

        if (railwayBookingSystem.validateUser(username, password)) {
            System.out.println("Login successful!");
            userDashboard(railwayBookingSystem);
            return true;
        } else {
            System.out.println(RED + "Invalid username or password. Please try again." + RESET);
            return false;
        }
    }

    private static void signup(RailwayBookingSystem railwayBookingSystem) {
        Queue userQueue = new Queue();
        Thread signupThread = new Thread(new SignupTask(railwayBookingSystem, userQueue));
        signupThread.start();

        try {
            signupThread.join();
        } catch (InterruptedException e) {
            System.out.println(RED + "Signup interrupted: " + e.getMessage() + RESET);
        }
    }

    private static void userDashboard(RailwayBookingSystem railwayBookingSystem) {
        while (true) {
            try {
                System.out.println("===== User Dashboard =====");
                System.out.println("1. Book Ticket"); // Book Tickets Using SQL Queries For Add In Database.
                System.out.println("2. Cancel Booking"); // Cancel Ticket
                System.out.println("3. Search Train"); // Search Train By Name.
                System.out.println("4. View All Train"); // View All Train Using ArrayList.
                System.out.println("5. View My Bookings"); // View All Booking Using SQL Query.
                System.out.println("6. Logout"); // Logout The User Portal.
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        bookMultipleTickets(railwayBookingSystem);
                        break;
                    case 2:
                        cancelBooking(railwayBookingSystem);
                        break;
                    case 3:
                        searchTrainByName(railwayBookingSystem);
                        break;
                    case 4:
                        viewAllTrains(railwayBookingSystem);
                        break;
                    case 5:
                        viewMyBookings(railwayBookingSystem);
                        break;
                    case 6:
                        System.out.println(RED + "Logging out. Goodbye!");
                        System.out.println("Thank you for using our Railway Booking System." + RESET);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    default:
                        System.out.println(RED + "Invalid choice. Please try again." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please Enter Integer Value." + RESET);
                sc.next();
            }
        }
    }

    private static void createTrain(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter train name: ");
        String trainName = sc.next();
        sc.nextLine();
        System.out.print("Enter source: ");
        String source = sc.nextLine();
        System.out.print("Enter destination: ");
        String destination = sc.nextLine();
        System.out.print("Enter departure time: ");
        String departureTime = sc.nextLine();
        System.out.print("Enter arrival time: ");
        String arrivalTime = sc.nextLine();
        System.out.print("Enter fare: ");
        double fare = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter number of stations between source and destination: ");
        int numStations = sc.nextInt();
        sc.nextLine();
        List<String> stations = new ArrayList<>();
        for (int i = 0; i < numStations; i++) {
            System.out.print("Enter station " + (i + 1) + ": ");
            stations.add(sc.nextLine());
        }

        railwayBookingSystem.createTrain(trainName, source, destination, departureTime, arrivalTime, fare, stations);
    }

    private static void updateTrain(RailwayBookingSystem railwayBookingSystem) {
        sc.nextLine();
        System.out.print("Enter train no to update: ");
        int trainNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new train name (or press enter to skip): ");
        String trainName = sc.nextLine();
        if (trainName.isEmpty()) {
            trainName = null;
        }

        System.out.print("Enter new source (or press enter to skip): ");
        String source = sc.nextLine();
        if (source.isEmpty()) {
            source = null;
        }

        System.out.print("Enter new destination (or press enter to skip): ");
        String destination = sc.nextLine();
        if (destination.isEmpty()) {
            destination = null;
        }

        System.out.print("Enter new departure time (HH:MM AM/PM) (or press enter to skip): ");
        String departureTime = sc.nextLine();
        if (departureTime.isEmpty()) {
            departureTime = null;
        }

        System.out.print("Enter new arrival time (HH:MM AM/PM) (or press enter to skip): ");
        String arrivalTime = sc.nextLine();
        if (arrivalTime.isEmpty()) {
            arrivalTime = null;
        }

        System.out.print("Enter new fare (or press enter to skip): ");
        String farest = sc.next();
        int fare = farest.isEmpty() ? 0 : Integer.parseInt(farest);
        sc.nextLine();

        railwayBookingSystem.updateTrain(trainNo, trainName, source, destination, departureTime, arrivalTime, fare);
    }

    private static void deleteTrain(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter train no: ");
        int trainNo = sc.nextInt();
        railwayBookingSystem.deleteTrain(trainNo);
    }

    private static void viewAllTrains(RailwayBookingSystem railwayBookingSystem) {
        if (railwayBookingSystem == null) {
            System.out.println("Railway booking system is not available");
            return;
        }

        List<Train> trains = railwayBookingSystem.getAllTrains();
        if (trains == null || trains.isEmpty()) {
            System.out.println("No trains available");
        } else {
            for (Train train : trains) {
                System.out.println(BEIGE + "==============================");
                System.out.println("Train No: " + train.getTrainNo());
                System.out.println("Train Name: " + train.getTrainName());
                System.out.println("Source: " + train.getSource());
                System.out.println("Destination: " + train.getDestination());
                System.out.println("Departure Time: " + train.getDepartureTime());
                System.out.println("Arrival Time: " + train.getArrivalTime());
                System.out.printf("Fare: %.2f%n", train.getFare()); // Display fare with two decimal places
                System.out.println("Stations:");
                List<String> stations = railwayBookingSystem.getStationsByTrainNo(train.getTrainNo());
                for (String station : stations) {
                    System.out.println("- " + station);
                }
                System.out.println("==============================" + RESET);
                System.out.println();
            }
        }
    }

    private static void viewAllBookings(RailwayBookingSystem railwayBookingSystem) {
        List<Booking> bookings = railwayBookingSystem.getAllBookings();
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                System.out.println(BEIGE + "==============================");
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("Train No: " + booking.getTrainNo());
                System.out.println("User ID: " + booking.getPassengerId());
                System.out.println("Booking Date: " + booking.getBookingDate());
                System.out.println("==============================" + RESET);
                System.out.println();
            }
        } else {
            System.out.println("No bookings available");
        }
    }

    private static void bookMultipleTickets(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter train no: ");
        int trainNo = sc.nextInt();
        System.out.print("Enter user ID: ");
        int userId = sc.nextInt();
        System.out.print("Enter number of seats to book: ");
        int numSeats = sc.nextInt();
        sc.nextLine();

        String bookingDateInput;
        LocalDate bookingDate;
        LocalDate currentDate = LocalDate.now();
        do {
            System.out.print("Enter booking date (yyyy-MM-dd): ");
            bookingDateInput = sc.nextLine();
            if (bookingDateInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    bookingDate = LocalDate.parse(bookingDateInput);
                    if (bookingDate.isAfter(currentDate)) {
                        break;
                    } else if (bookingDate.isEqual(currentDate)) {
                        System.out.println("Booking date cannot be today. Please enter a future date.");
                    } else {
                        System.out.println("Booking date cannot be in the past. Please enter a future date.");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
                }
            } else {
                System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
            }
        } while (true);

        railwayBookingSystem.bookTicket(trainNo, userId, numSeats, bookingDateInput);
    }

    private static void cancelBooking(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter booking ID: ");
        int bookingId = sc.nextInt();
        railwayBookingSystem.cancelBooking(bookingId);
    }

    private static void viewMyBookings(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter user ID: ");
        int userId = sc.nextInt();
        List<Booking> bookings = railwayBookingSystem.getMyBookings(userId);
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                System.out.println(BEIGE + "---------------------");
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("Train No: " + booking.getTrainNo());
                System.out.println("Booking Date: " + booking.getBookingDate());
                System.out.println("---------------------" + RESET);
            }
        } else {
            System.out.println(RED + "No bookings available for user ID " + userId + RESET);
        }
    }

    private static void searchTrainByName(RailwayBookingSystem railwayBookingSystem) {
        System.out.print("Enter train name to search: ");
        String trainName = sc.next();
        sc.nextLine();
        List<Train> matchingTrains = railwayBookingSystem.searchTrainByName(trainName);
        if (matchingTrains.isEmpty()) {
            System.out.println("No trains found with name " + trainName);
        } else {
            System.out.println("Trains found:");
            for (Train train : matchingTrains) {
                System.out.println(BEIGE + "==============================");
                System.out.println("Train No: " + train.getTrainNo());
                System.out.println("Train Name: " + train.getTrainName());
                System.out.println("Source: " + train.getSource());
                System.out.println("Destination: " + train.getDestination());
                System.out.println("Departure Time: " + train.getDepartureTime());
                System.out.println("Arrival Time: " + train.getArrivalTime());
                System.out.println("Fare: " + train.getFare());
                System.out.println("==============================" + RESET);
            }
        }
    }
}
