package br.com.rodrigocbarj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExceptionTest {
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void empty() {
        List<String> list = new ArrayList<String>();
        // list.add("@rodrigocbarj");

        list.get(0);
    } // teste de excessão em array vazio

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTestExpectException() throws IndexOutOfBoundsException {
        List<Object> list = new ArrayList<Object>();

        thrown.expect(IndexOutOfBoundsException.class); // exception esperada
        thrown.expectMessage("Index 0 out of bounds for length 0"); // mensagem esperada
        list.get(0);
    }

    @Test
    public void testExceptionMessage() {
        try {
            new ArrayList<Object>().get(0);
            fail("Esperado que IndexOutOfBoundsException seja lançada");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(e.getMessage(), "Index 0 out of bounds for length 0");
        }
    }
}
