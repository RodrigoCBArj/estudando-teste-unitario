package br.com.rodrigocbarj;

import org.junit.Assert;
import org.junit.Test;

public class CalculadoraTest {

    @Test
    public void deveSomarDoisValores() {
        // cenario
        int x = 5;
        int y = 3;

        Calculadora calculadora = new Calculadora();

        // ação
        int resultado = calculadora.somar(x, y);

        // verificação
        Assert.assertEquals(8, resultado);
    }
}
