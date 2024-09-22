package railway;

import booking.*;
import dsImplimentation.Stack;

import java.io.*;
import java.sql.*;
import java.util.*;

public class RailwayBookingSystem {
    private HashMap<String, User> users;
    private HashMap<Integer, Train> trains;
    private HashMap<String, User> usersMap;
    private Stack<User> lastSignedUpUsers;
    Scanner sc = new Scanner(System.in);
    Connection con;

    // Colors....
    public static final String CYAN = "\u001B[36m";
    public static final String BEIGE = "\u001B[38;5;187m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    public RailwayBookingSystem() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String dburl = "jdbc:mysql://localhost:3306/railway_db";
        String dbuser = "root";
        String dbpass = "";
        this.users = new HashMap<>();
        this.trains = new HashMap<>();
        this.usersMap = new HashMap<>();
        this.lastSignedUpUsers = new Stack<>(100);

        con = DriverManager.getConnection(dburl, dbuser, dbpass);
        if (con != null) {
            System.out.println(YELLOW + "Conected To The Database" + RESET);
        } else {
            System.out.println("Failed to conect to the database");
        }

    }

    public boolean containsUser(String username) {
        return usersMap.containsKey(username);
    }

    public boolean validateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public void createUser(String username, String password, String name, String email, String phoneNumber) {
        User user = new User(username, password, name, email, phoneNumber);
        users.put(username, user);
        lastSignedUpUsers.push(user);
    }

    public User getLastSignedUpUser() {
        return lastSignedUpUsers.peek();

    }

    public void addUser(User user) {
        lastSignedUpUsers.push(user);
    }

    public void viewLastSignedUpUser() {
        if (!lastSignedUpUsers.isEmpty()) {
            User user = lastSignedUpUsers.peek();
            System.out.println(CYAN + "Last signed up Username: " + user.getName());
            System.out.println("Last signed up Email: " + user.getEmail() + RESET);
        } else {
            System.out.println("No users have signed up yet.");
        }
    }

    public void removeLastSignedUpUser() {
        if (!lastSignedUpUsers.isEmpty()) {
            User user = lastSignedUpUsers.pop();
            System.out.println("Removed user: " + user.getName() + " (" + user.getEmail() + ")");

            // Remove the user from the database
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/railway_db", "root", "");
                    PreparedStatement pstmt = con.prepareStatement("DELETE FROM user WHERE name = ?")) {
                pstmt.setString(1, user.getName());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error deleting user from database: " + e.getMessage());
            }
        } else {
            System.out.println("No users to remove.");
        }
    }

    public void close() throws Exception {
        if (con != null) {
            con.close();
        }
    }

    public void createTrain(String trainName, String source, String destination, String departureTime,
            String arrivalTime, double fare, List<String> stations) {
        if (con != null) {
            try (CallableStatement cst = con.prepareCall(
                    "call insertTrain(?, ?, ?, ?, ?, ?, ?)")) {
                cst.setString(1, trainName);
                cst.setString(2, source);
                cst.setString(3, destination);
                cst.setString(4, departureTime);
                cst.setString(5, arrivalTime);
                cst.setDouble(6, fare);
                cst.setInt(7, 50);
                cst.executeUpdate();
                System.out.println("Train created successfully");

                // Add stations between source and destination
                int trainNo = getLastInsertedTrainNo();
                addStationsBetween(trainNo, source, destination, stations);
            } catch (SQLException e) {
                System.out.println("Error creating train: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
    }

    private int getLastInsertedTrainNo() throws SQLException {
        String query = "SELECT LAST_INSERT_ID()";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to retrieve last inserted train no.");
    }

    private void addStationsBetween(int trainNo, String source, String destination, List<String> stations) {
        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO station (train_no, station_name, sequence) VALUES (?, ?, ?)")) {
                int sequence = 1;
                String previousStation = source;
                for (String station : stations) {
                    if (station.equals(destination)) {
                        break;
                    }
                    pstmt.setInt(1, trainNo);
                    pstmt.setString(2, station);
                    pstmt.setInt(3, sequence);
                    pstmt.addBatch();

                    sequence++;
                    previousStation = station;
                }
                pstmt.executeBatch();
                System.out.println("Stations added between source and destination successfully");
            } catch (SQLException e) {
                System.out.println("Error adding stations between source and destination: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
    }

    public void updateTrain(int trainNo, String name, String source, String destination, String departureTime,
            String arrivalTime, double fare) {
        String query = "UPDATE train SET ";
        boolean hasUpdates = false;
        int index = 1;

        if (name != null) {
            query += "name = ? ";
            hasUpdates = true;
        }
        if (source != null) {
            if (hasUpdates) {
                query += ", ";
            }
            query += "source = ? ";
            hasUpdates = true;
        }
        if (destination != null) {
            if (hasUpdates) {
                query += ", ";
            }
            query += "destination = ? ";
            hasUpdates = true;
        }
        if (departureTime != null) {
            if (hasUpdates) {
                query += ", ";
            }
            query += "departure_time = ? ";
            hasUpdates = true;
        }
        if (arrivalTime != null) {
            if (hasUpdates) {
                query += ", ";
            }
            query += "arrival_time = ? ";
            hasUpdates = true;
        }
        if (fare != 0) {
            if (hasUpdates) {
                query += ", ";
            }
            query += "fare = ? ";
            hasUpdates = true;
        }
        query += "WHERE train_no = ?";

        if (hasUpdates) {
            try (PreparedStatement pst = con.prepareStatement(query)) {
                if (name != null) {
                    pst.setString(index++, name);
                }
                if (source != null) {
                    pst.setString(index++, source);
                }
                if (destination != null) {
                    pst.setString(index++, destination);
                }
                if (departureTime != null) {
                    pst.setString(index++, departureTime);
                }
                if (arrivalTime != null) {
                    pst.setString(index++, arrivalTime);
                }
                if (fare != 0) {
                    pst.setDouble(index++, fare);
                }
                pst.setInt(index++, trainNo);
                pst.executeUpdate();
                System.out.println("Train updated successfully.");
            } catch (SQLException e) {
                System.out.println("Error updating train: " + e.getMessage());
            }
        } else {
            System.out.println("No updates made.");
        }
    }

    public void deleteTrain(int trainNo) {
        if (con != null) {
            try (CallableStatement cst = con.prepareCall("call deleteTrain(?)")) {
                cst.setInt(1, trainNo);
                cst.executeUpdate();
                System.out.println("Train deleted successfully");
            } catch (SQLException e) {
                System.out.println("Error deleting train: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
    }

    public List<Train> searchTrainByName(String trainName) {
        List<Train> matchingTrains = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM train WHERE train_name LIKE ?")) {
                pstmt.setString(1, "%" + trainName + "%");
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        Train train = new Train();
                        train.setTrainNo(resultSet.getInt("train_no"));
                        train.setTrainName(resultSet.getString("train_name"));
                        train.setSource(resultSet.getString("source"));
                        train.setDestination(resultSet.getString("destination"));
                        train.setDepartureTime(resultSet.getString("departure_time"));
                        train.setArrivalTime(resultSet.getString("arrival_time"));
                        train.setFare(resultSet.getDouble("fare"));
                        matchingTrains.add(train);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error searching for train by name: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
        return matchingTrains;
    }

    public List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM train")) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        Train train = new Train();
                        train.setTrainNo(resultSet.getInt("train_no"));
                        train.setTrainName(resultSet.getString("train_name"));
                        train.setSource(resultSet.getString("source"));
                        train.setDestination(resultSet.getString("destination"));
                        train.setDepartureTime(resultSet.getString("departure_time"));
                        train.setArrivalTime(resultSet.getString("arrival_time"));
                        train.setFare(resultSet.getDouble("fare")); // Retrieve the fare value from the database
                        trains.add(train);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving all trains: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
        return trains;
    }

    public void bookTicket(int trainNo, int userId, int numSeats, String bookingDate) {
        Train train = getTrainByTrainNo(trainNo);
        if (train != null) {
            System.out.println("Fare for Train No " + trainNo + " is: " + train.getFare());
        } else {
            System.out.println("Train not found.");
            return;
        }

        if (con != null) {
            String seatNumbers = ""; // create an empty string to store the seat numbers
            for (int i = 0; i < numSeats; i++) {
                while (true) {
                    System.out.print("Enter seat number " + (i + 1) + ": ");
                    int seatNo = sc.nextInt();
                    if (isSeatAlreadyBooked(trainNo, seatNo) || isSeatAlreadyBookedByUser(trainNo, userId, seatNo)) {
                        System.out.println("Seat " + seatNo + " is already booked. Please choose a different seat.");
                    } else {
                        seatNumbers += seatNo;
                        if (i < numSeats - 1) {
                            seatNumbers += ",";
                        }
                        break;
                    }
                }
            }

            double total_fare = numSeats * train.getFare();
            System.out.println("Your Total Fare Is = " + total_fare);
            Payment p = new Payment();
            p.makePayment();
            try (PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO booking (train_no, user_id, booking_date, seat_no, total_fare) VALUES (?, ?, ?, ?,?)")) {

                pstmt.setInt(1, trainNo);
                pstmt.setInt(2, userId);
                pstmt.setString(3, bookingDate);
                pstmt.setString(4, seatNumbers);
                pstmt.setDouble(5, total_fare);
                pstmt.executeUpdate();
                System.out.println("Ticket booked successfully");
            } catch (SQLException e) {
                System.out.println("Error booking ticket: " + e.getMessage());
            }

            createUserFile(userId);
            // Write the booked ticket details to the user file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_" + userId + ".txt", true))) {
                writer.write("Booked Ticket:");
                writer.newLine();
                writer.write("--------------------------------------------");
                writer.newLine();
                writer.write("User ID: " + userId);
                writer.newLine();
                // also want to write user name
                writer.write("Train No: " + trainNo);
                writer.newLine();
                writer.write("Train Name: " + train.getTrainName());
                writer.newLine();
                writer.write("Source: " + train.getSource());
                writer.newLine();
                writer.write("Destination: " + train.getDestination());
                writer.newLine();
                writer.write("Departure Time: " + train.getDepartureTime());
                writer.newLine();
                writer.write("Arrival Time: " + train.getArrivalTime());
                writer.newLine();
                writer.write("Total Fare: " + total_fare);
                writer.newLine();
                writer.write("Seats: " + numSeats);
                writer.newLine();
                writer.write("Booking Date: " + bookingDate);
                writer.newLine();
                writer.write("--------------------------------------------");
                writer.newLine();
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error writing to user file: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
    }

    private boolean isSeatAlreadyBooked(int trainNo, int seatNo) {
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT * FROM booking WHERE train_no = ? AND seat_no LIKE ?")) {
            pstmt.setInt(1, trainNo);
            pstmt.setString(2, "%" + seatNo + "%");
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking if seat is already booked: " + e.getMessage());
            return false;
        }
    }

    private boolean isSeatAlreadyBookedByUser(int trainNo, int userId, int seatNo) {
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT * FROM booking WHERE train_no = ? AND user_id = ? AND seat_no LIKE ?")) {
            pstmt.setInt(1, trainNo);
            pstmt.setInt(2, userId);
            pstmt.setString(3, "%" + seatNo + "%");
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking if seat is already booked by user: " + e.getMessage());
            return false;
        }
    }
    // --> file ......

    private int fileCounter = 1;

    private void createUserFile(int userId) {
        String fileName = "user-" + userId + "_" + fileCounter + ".txt";
        File file = new File(fileName);
        try {
            file.createNewFile();
            fileCounter++;
        } catch (IOException e) {
            System.out.println("Error creating user file: " + e.getMessage());
        }
    }

    private Train getTrainByTrainNo(int trainNo) {
        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM train WHERE train_no = ?")) {
                pstmt.setInt(1, trainNo);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        Train train = new Train();
                        train.setTrainNo(resultSet.getInt("train_no"));
                        train.setTrainName(resultSet.getString("train_name"));
                        train.setSource(resultSet.getString("source"));
                        train.setDestination(resultSet.getString("destination"));
                        train.setDepartureTime(resultSet.getString("departure_time"));
                        train.setArrivalTime(resultSet.getString("arrival_time"));
                        train.setFare(resultSet.getDouble("fare"));
                        return train;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving train by train no: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
        return null;
    }

    public void cancelBooking(int bookingId) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter booking date (yyyy-mm-dd): ");
        String bookingDate = sc.next();

        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement(
                    "SELECT * FROM booking WHERE booking_id = ? AND booking_date = ?")) {
                pstmt.setInt(1, bookingId);
                pstmt.setString(2, bookingDate);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        try (PreparedStatement deleteStmt = con.prepareStatement(
                                "DELETE FROM booking WHERE booking_id = ? AND booking_date = ?")) {
                            deleteStmt.setInt(1, bookingId);
                            deleteStmt.setString(2, bookingDate);
                            deleteStmt.executeUpdate();
                            System.out.println("Booking cancelled successfully");
                        }
                    } else {
                        System.out.println("No booking found with the given details");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error cancelling booking: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM booking")) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        Booking booking = new Booking();
                        booking.setBookingId(resultSet.getInt("booking_id"));
                        booking.setTrainNo(resultSet.getInt("train_no"));
                        booking.setPassengerId(resultSet.getInt("user_id"));
                        booking.setBookingDate(resultSet.getString("booking_date"));
                        bookings.add(booking);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving all bookings: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
        return bookings;
    }

    public List<Booking> getMyBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM booking WHERE user_id = ?")) {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        Booking booking = new Booking();
                        booking.setBookingId(resultSet.getInt("booking_id"));
                        booking.setTrainNo(resultSet.getInt("train_no"));
                        booking.setPassengerId(resultSet.getInt("user_id"));
                        booking.setBookingDate(resultSet.getString("booking_date"));
                        bookings.add(booking);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving my bookings: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
        return bookings;
    }

    public void viewAllPassenger() {
        String sql = "call getPassenger()";
        try (CallableStatement cst = con.prepareCall(sql)) {
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                System.out.println(BEIGE + "-------------------");
                System.out.println("Id :- " + rs.getInt("id"));
                System.out.println("Name :- " + rs.getString("name"));
                System.out.println("Age :- " + rs.getInt("age"));
                System.out.println("Gender :- " + rs.getString("gender"));
                System.out.println("-------------------" + RESET);
            }
        } catch (SQLException e) {
            System.out.println("Error while trying to display passengers: " + e.getMessage());
        }
    }

    public void createUserTable(String name, int age, String gender, String phoneNumber, String email) {
        String tableName = name.replaceAll("\\s+", "_").toLowerCase();
        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS user (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT, gender VARCHAR(10), phone_number VARCHAR(15), email VARCHAR(255))")) {
                pstmt.executeUpdate();
                System.out.println("User table created successfully");
            } catch (SQLException e) {
                System.out.println("Error creating user table: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }

        if (con != null) {
            try (PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO user (name, age, gender, phone_number, email) VALUES (?, ?, ?, ?, ?)")) {
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setString(3, gender);
                pstmt.setString(4, phoneNumber);
                pstmt.setString(5, email);
                pstmt.executeUpdate();
                System.out.println("User added to the table successfully");
            } catch (SQLException e) {
                System.out.println("Error adding user to the table: " + e.getMessage());
            }
        } else {
            System.out.println("conection is null");
        }
    }

    public List<String> getStationsByTrainNo(int trainNo) {
        List<String> stations = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/railway_db", "root", "");
                PreparedStatement pstmt = con
                        .prepareStatement("SELECT station_name FROM station WHERE train_no = ?")) {
            pstmt.setInt(1, trainNo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stations.add(rs.getString("station_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving stations for train no " + trainNo + ": " + e.getMessage());
        }
        return stations;
    }
}
