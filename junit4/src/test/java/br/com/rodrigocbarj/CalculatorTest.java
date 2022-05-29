package br.com.rodrigocbarj;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CalculatorTest {
    
    @Test
    public void testSum() {
        Calculator calculator = new Calculator();
        
        int sumTest01 = calculator.sum("1+2+3");

        assertEquals(6, sumTest01);
    }

    @Test
    public void testSumMock() { // teste utilizando Mock/Mockito
        Calculator calculator = mock(Calculator.class);

        when(calculator.sum("1+2")).thenReturn(10);
        
        int result = calculator.sum("1+2");

        assertEquals(10, result);
    }
}
