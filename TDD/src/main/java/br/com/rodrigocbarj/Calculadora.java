package br.com.rodrigocbarj;

import br.com.rodrigocbarj.exceptions.DivisaoPorZeroException;

public class Calculadora {

    public int somar(int x, int y) {
        return (x + y);
    }

    public int subtrair(int x, int y) {
        return (x - y);
    }

    public int dividir(int x, int y) throws DivisaoPorZeroException {
        if (y == 0)
            throw new DivisaoPorZeroException("Não é possível dividir por zero!");

        return (x / y);
    }
}
