package br.com.rodrigocbarj;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

// testes executando por ordem alfabética
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTestes {

    private static int contador = 0;

    @Test
    public void inicial() {
        contador = 1;
    }

    @Test
    public void verifica() {
        Assert.assertEquals(1, contador);
    }
}
