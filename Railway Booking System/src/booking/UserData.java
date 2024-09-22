package booking;

import java.util.*;
import java.io.*;

public class UserData {
    private static final String FILE_NAME = "users.txt";

    public void createUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getName() + "," + user.getEmail()
                    + "," + user.getPhoneNumber() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                User user = new User(userData[0], userData[1], userData[2], userData[3], userData[4]);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return users;
    }
}