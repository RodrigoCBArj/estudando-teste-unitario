package br.com.rodrigocbarj;

import br.com.rodrigocbarj.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test() {
        // booleanos
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        // numerais
        Assert.assertEquals("Esta mensagem aparece caso dê erro",1, 1);
        // para valores do tipo double, o método assertEquals necessita de um delta, para a margem de erro:
        Assert.assertEquals(0.5120, 0.5128, 0.001);
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        int i1 = 5;
        Integer i2 = 5;
//        Assert.assertEquals(i1, i2); // dessa forma não é possível
        Assert.assertEquals(i1, i2.intValue());
        // ou:
        Assert.assertEquals(Integer.valueOf(i1), i2);

        // strings
        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("BOLA", "bola");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola". startsWith("bol"));

        // objetos
        Usuario u1 = new Usuario("Usuario x");
        Usuario u2 = new Usuario("Usuario x");
        Usuario u3 = u1;

        Assert.assertEquals(u1, u2); // é necessário os métodos equals e hashCode no objeto testado

        Assert.assertSame(u1, u3); // verifica se é exatamente a mesma variável/objeto (memória)
        Assert.assertNotSame(u1, u2);

        u3 = null;
        Assert.assertTrue(u3 == null);
        Assert.assertNull(u3);
        Assert.assertNotNull(u1);
    }
}
