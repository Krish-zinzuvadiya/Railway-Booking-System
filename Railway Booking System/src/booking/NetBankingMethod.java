package booking;

import java.util.*;

public class NetBankingMethod extends PaymentMethod {
    String username;
    String password;

    public NetBankingMethod() {
        super("Net Banking Payment");
    }

    private void getNetBankingDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your net banking details:");
        System.out.print("Enter Your Username : ");
        username = sc.next();

        System.out.print("Enter Password : ");
        password = sc.next();
        while (password.length() != 6) {
            System.out.println(
                    "Invalid password. Please enter a valid password of your bank. and must be 6 letter.");
            System.out.print("Re-Enter Password : ");
            password = sc.next();
        }
    }

    public void makePayment() {
        getNetBankingDetails();
        try {
            System.out.println("Processing Online Payment...");
            Thread.sleep(4000); // 4 second sleep (Payment Confirmation...)
            System.out.println("Payment successful!");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}