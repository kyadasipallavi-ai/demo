import java.util.Scanner;

public class SimpleCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double num1, num2, result;
        char operator;

        System.out.println("=== Java Console Calculator ===");

        // Input first operand
        System.out.print("Enter first number: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); 
        }
        num1 = scanner.nextDouble();

        // Input mathematical operator
        System.out.print("Enter an operator (+, -, *, /): ");
        operator = scanner.next().charAt(0);

        // Input second operand
        System.out.print("Enter second number: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        num2 = scanner.nextDouble();

        // Evaluate operation
        switch (operator) {
            case '+':
                result = num1 + num2;
                System.out.printf("Result: %.2f + %.2f = %.2f\n", num1, num2, result);
                break;

            case '-':
                result = num1 - num2;
                System.out.printf("Result: %.2f - %.2f = %.2f\n", num1, num2, result);
                break;

            case '*':
                result = num1 * num2;
                System.out.printf("Result: %.2f * %.2f = %.2f\n", num1, num2, result);
                break;

            case '/':
                // Prevent runtime crash caused by division by zero
                if (num2 == 0) {
                    System.out.println("Error: Division by zero is undefined.");
                } else {
                    result = num1 / num2;
                    System.out.printf("Result: %.2f / %.2f = %.2f\n", num1, num2, result);
                }
                break;

            default:
                System.out.println("Error: Invalid math operator entered.");
                break;
        }

        scanner.close();
        System.out.println("===============================");
    }
}
