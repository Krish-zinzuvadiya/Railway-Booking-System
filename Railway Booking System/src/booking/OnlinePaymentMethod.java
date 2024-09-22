
package booking;

import java.util.*;

class OnlinePaymentMethod extends PaymentMethod {
    private String upiID;
    private String pin;

    public OnlinePaymentMethod() {
        super("Online Payment");
    }

    private void getOnlinePaymentDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your online payment details:");
        System.out.print("Enter Your UPI-ID : ");
        upiID = sc.next();
        while (!upiID.contains("@")) {
            System.out.println("Invalid upi-id. Please enter a valid upi-id.");
            System.out.print("Enter UPI-ID : ");
            upiID = sc.next();
        }

        System.out.print("Enter Pin : ");
        pin = sc.next();
        while (pin.length() != 4) {
            System.out.println("Invalid Pin number. Please enter a valid pin number of your bank.");
            System.out.print("Enter Pin : ");
            pin = sc.next();
        }
    }

    public void makePayment() {
        getOnlinePaymentDetails();
        try {
            System.out.println("Processing Online Payment...");
            Thread.sleep(3000); // 3 second sleep (Payment Confirmation...)
            System.out.println("Payment successful!");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
