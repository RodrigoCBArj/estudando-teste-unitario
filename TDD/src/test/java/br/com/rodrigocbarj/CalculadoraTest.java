package br.com.rodrigocbarj;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculadoraTest {

    Calculadora calculadora;
    @Before
    public void criaCalculadora() {
        calculadora = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        // cenario
        int x = 5;
        int y = 3;

        // ação
        int resultado = calculadora.somar(x, y);

        // verificação
        assertEquals(8, resultado);
    }
}
