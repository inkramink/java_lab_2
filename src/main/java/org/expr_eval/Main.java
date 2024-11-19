package org.expr_eval;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter expression:");
        String expression = scanner.nextLine();

        try {
            double result = ExpressionEvaluator.evaluate(expression);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}