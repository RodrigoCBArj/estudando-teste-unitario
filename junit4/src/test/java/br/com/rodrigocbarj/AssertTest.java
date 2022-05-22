package br.com.rodrigocbarj;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class AssertTest {
    
    byte[] expected = "test".getBytes();
    byte[] actual = "test".getBytes();
    
    @Test
    public void testAssertArrayEquals() {
        assertArrayEquals(expected, actual); // teste comparando igualdade de Arrays
    }

    @Test
    public void testAssertEquals() {
        String string = "text";
        assertEquals("text", string); // teste comparando Strings
    }

    @Test
    public void testAssertFalse() {
        assertFalse(false); // teste booleano
    }

    @Test
    public void testAssertNotNull() {
        assertNotNull(new Object()); // verifica se um objeto não é nulo
    }

    @Test
    public void testAssertNotSame() {
        assertNotSame(new Object(), new Object()); // verifica se dois objetos não são iguais
    }

    @Test
    public void testAssertNull() {
        assertNull(null); // verifica se o objeto é nulo
    }
    
    @Test
    public void testAssertSame() {
        Integer number = Integer.valueOf(45);
        assertSame(number, number); // verifica se o objeto é o mesmo
    }
}
