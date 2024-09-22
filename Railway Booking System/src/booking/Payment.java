package booking;

import java.util.Scanner;
import dsImplimentation.*;

public class Payment {
    private LinkedList paymentMethods;

    public Payment() {
        paymentMethods = new LinkedList();
        paymentMethods.add(new CardPaymentMethod());
        paymentMethods.add(new CashPaymentMethod());
        paymentMethods.add(new OnlinePaymentMethod());
        paymentMethods.add(new NetBankingMethod());
    }

    public void makePayment() {
        System.out.println("=========================");
        System.out.println("1.Credit/Debit Card.");
        System.out.println("2.Cash.");
        System.out.println("3.Online Payment [UPI].");
        System.out.println("4.Net Banking Method.");
        System.out.println("=========================");
        System.out.print("Enter your choice (1-4): ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        if (choice >= 1 && choice <= paymentMethods.size()) {
            PaymentMethod paymentMethod = paymentMethods.get(choice - 1);
            paymentMethod.makePayment();
        } else {
            System.out.println("Invalid payment method choice.");
        }
    }
}