package br.com.rodrigocbarj;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculatorTest {
    Calculator calculator = new Calculator();

    @Test
    public void testSum() {
        
        int sumTest01 = calculator.sum("1+2+3");
        assertEquals(6, sumTest01);
    }
}
