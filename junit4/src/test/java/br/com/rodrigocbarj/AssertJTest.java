package br.com.rodrigocbarj;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class AssertJTest {

    @Test
    public void checkEquality() {
        Person person = new Person("Rodrigo");
        Person personClone = person;
//        Person personClone = new Person("Rodrigo"); // esse teste não passa

        assertThat(person).isEqualTo(personClone);
    }
}
