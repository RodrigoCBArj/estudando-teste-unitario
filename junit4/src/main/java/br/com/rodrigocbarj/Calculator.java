package br.com.rodrigocbarj;

public class Calculator {
    
    public int sum(String expression) {
        int total = 0;
        for (String value : expression.split("\\+")) {
            total += Integer.valueOf(value);
        }

        System.out.println(expression + " = " + total);
        return total;
    }
}
