package booking;

import java.util.*;

class CardPaymentMethod extends PaymentMethod {
    private String cardNumber;
    private int cvv;
    private String expirationDate;

    public CardPaymentMethod() {
        super("Debit/Credit Card"); // Pass the payment method name and name to the superclass
    }

    private void getCardDetails() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your debit/credit card details:");
        System.out.print("Card number (14-16 digits): ");
        cardNumber = sc.next();

        if (!isValidCardNumber(cardNumber)) {
            System.out.println("Invalid card number. Please try again.");
            getCardDetails();
            return;
        }

        System.out.print("CVV (3 digits): ");
        cvv = sc.nextInt();

        if (!isValidCvv(cvv)) {
            System.out.println("Invalid CVV. Please try again.");
            getCardDetails();
            return;
        }

        System.out.print("Expiration date (MM/YY): ");
        expirationDate = sc.next();

        if (!isValidExpirationDate(expirationDate)) {
            System.out.println("Invalid expiration date. Please try again.");
            getCardDetails();
            return;
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber.length() < 14 || cardNumber.length() > 16) {
            return false;
        }

        try {
            Long.parseLong(cardNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidCvv(int cvv) {
        if (cvv < 100 || cvv > 999) {
            return false;
        }

        return true;
    }

    private boolean isValidExpirationDate(String expirationDate) {
        String[] parts = expirationDate.split("/");

        if (parts.length != 2) {
            return false;
        }

        String month = parts[0];
        String year = parts[1];

        if (month.length() != 2 || year.length() != 2) {
            return false;
        }

        try {
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);

            if (monthInt < 1 || monthInt > 12) {
                return false;
            }

            if (yearInt < 22) {
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void makePayment() {
        getCardDetails();
        try {
            System.out.println("Processing payment...");
            Thread.sleep(2000); // 2 second sleep
            System.out.println("Payment successful!");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}