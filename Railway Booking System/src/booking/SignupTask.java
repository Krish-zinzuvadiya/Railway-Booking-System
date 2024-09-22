package booking;

import railway.*;
import dsImplimentation.Queue;

import java.util.*;
import java.sql.*;
import java.io.*;

public class SignupTask implements Runnable {
    private RailwayBookingSystem railwayBookingSystem;
    private Scanner sc = new Scanner(System.in);
    private Queue queue;

    public SignupTask(RailwayBookingSystem railwayBookingSystem, Queue queue) {
        this.railwayBookingSystem = railwayBookingSystem;
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();
        System.out.print("Enter name: ");
        String name = sc.next();
        System.out.print("Enter email: ");
        String email = sc.next();
        while (!email.endsWith("@gmail.com")) {
            System.out.println("Invalid email. Please enter a valid Gmail address.");
            System.out.print("Enter email: ");
            email = sc.next();
        }
        System.out.print("Enter phone number: ");
        String phoneNumber = sc.next();
        while (phoneNumber.length() != 10) {
            System.out.println("Invalid phone number. Please enter a 10-digit phone number.");
            System.out.print("Enter phone number: ");
            phoneNumber = sc.next();
        }
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        System.out.print("Enter gender (M/F): ");
        String gender = sc.next().trim().toUpperCase();
        while (!gender.equals("M") && !gender.equals("F")) {
            System.out.println("Invalid gender. Please enter M for Male, F for Female");
            System.out.print("Enter gender (M/F): ");
            gender = sc.next().trim().toUpperCase();
        }

        User user = new User(username, password, name, email, phoneNumber);
        UserData UserData = new UserData();
        UserData.createUser(user);
        // Add user to the queue
        queue.enqueue(user);

        railwayBookingSystem.createUser(username, password, name, email, phoneNumber);
        railwayBookingSystem.createUserTable(name, age, gender, phoneNumber, email);

        // Add code to write user details to file
        writeUserDataToFile(user);
        // Add code to add user to passenger database
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/railway_db", "root", "");
                PreparedStatement pstmt = con
                        .prepareStatement("INSERT INTO passenger (name, age, gender) VALUES (?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding user to passenger database: " + e.getMessage());
        }

        // Get the user ID from the database
        int userId = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/railway_db", "root", "");
                PreparedStatement pstmt = con
                        .prepareStatement("SELECT id FROM passenger WHERE name = ? AND age = ? AND gender = ?")) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting user ID from database: " + e.getMessage());
        }

        System.out.println("Signup successful! Your user ID is " + userId);

    }

    public void writeUserDataToFile(User user) {
        boolean userAlreadyExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(user.getUsername()) &&
                        userData[1].equals(user.getPassword()) &&
                        userData[2].equals(user.getName()) &&
                        userData[3].equals(user.getEmail()) &&
                        userData[4].equals(user.getPhoneNumber())) {
                    userAlreadyExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }

        if (!userAlreadyExists) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
                writer.write(
                        user.getUsername() + "," + user.getPassword() + "," + user.getName() + "," + user.getEmail()
                                + "," + user.getPhoneNumber() + "\n");
                writer.flush(); // Make sure to flush the buffer
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        }
    }
}