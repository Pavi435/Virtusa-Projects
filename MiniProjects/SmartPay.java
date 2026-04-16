import java.util.Scanner;

interface Billable {
    double calculateTotal(int units);
}

class UtilityBill implements Billable {

    double taxAmount = 0;

    public double calculateTotal(int units) {
        double total = 0;

        if (units <= 100) {
            total = units * 1.0;
        } else if (units <= 300) {
            total = (100 * 1.0) + (units - 100) * 2.0;
        } else {
            total = (100 * 1.0) + (200 * 2.0) + (units - 300) * 5.0;
        }

        taxAmount = total * 0.05;

        return total + taxAmount;
    }

    public double getTax() {
        return taxAmount;
    }
}

public class SmartPay {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UtilityBill bill = new UtilityBill();

        while (true) {
            System.out.println("\nEnter Customer Name (or type Exit to stop): ");
            String name = sc.nextLine();

            if (name.equalsIgnoreCase("Exit")) {
                System.out.println("Exiting program...");
                break;
            }

            System.out.print("Enter Previous Meter Reading: ");
            int previous = sc.nextInt();

            System.out.print("Enter Current Meter Reading: ");
            int current = sc.nextInt();
            sc.nextLine(); 

            if (previous > current) {
                System.out.println("Error: Previous reading cannot be greater than current reading.");
                continue;
            }

            int units = current - previous;
            double finalAmount = bill.calculateTotal(units);

            System.out.println("\n----- DIGITAL RECEIPT -----");
            System.out.println("Customer Name   : " + name);
            System.out.println("Units Consumed  : " + units);
            System.out.println("Tax Amount (5%) : " + bill.getTax());
            System.out.println("Final Total     : " + finalAmount);
            System.out.println("---------------------------");
        }
        sc.close();
    }
}