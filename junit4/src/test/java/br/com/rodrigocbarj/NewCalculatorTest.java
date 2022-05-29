package br.com.rodrigocbarj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NewCalculatorTest {

    @Test
    public void mustSumTwoValues() {
        int valueA = 1;
        int valueB = 2;

        NewCalculator calculator = new NewCalculator();
        int result = calculator.sum(valueA, valueB);

        assertEquals(3, result);
    }

    @Test
    public void mustSumThreeValues() {
        int valueA = 1;
        int valueB = 2;
        int valueC = 3;

        NewCalculator calculator = new NewCalculator();
        int result = calculator.sum(valueA, valueB, valueC);

        assertEquals(6, result);
    }
}
