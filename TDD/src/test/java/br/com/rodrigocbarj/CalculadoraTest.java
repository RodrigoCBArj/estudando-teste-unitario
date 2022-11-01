package br.com.rodrigocbarj;

import br.com.rodrigocbarj.exceptions.DivisaoPorZeroException;
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

    @Test
    public void deveSubtrairDoisValores() {
        int x = 8;
        int y = 3;

        int resultado = calculadora.subtrair(x, y);

        assertEquals(5, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws DivisaoPorZeroException {
        int x = 8;
        int y = 3;

        int resultado = calculadora.dividir(x, y);

        assertEquals(2, resultado);
    }

    @Test(expected = DivisaoPorZeroException.class)
    public void deveLancarExcessaoAoDividirPorZero() throws DivisaoPorZeroException {
        int x = 8;
        int y = 0;

        calculadora.dividir(x, y);
    }
}
