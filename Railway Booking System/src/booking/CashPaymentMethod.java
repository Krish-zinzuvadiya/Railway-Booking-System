package booking;

public class CashPaymentMethod extends PaymentMethod {
    public CashPaymentMethod() {
        super("Cash");
    }

    public void makePayment() {
        System.out.println("Processing cash payment...");
        try {
            Thread.sleep(1000); // 1 second sleep
            System.out.println("Payment successful!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}