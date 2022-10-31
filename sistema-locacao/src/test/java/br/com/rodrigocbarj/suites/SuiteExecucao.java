package br.com.rodrigocbarj.suites;

import br.com.rodrigocbarj.servicos.CalculoValorLocacaoTest;
import br.com.rodrigocbarj.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runners.Suite;

//@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocacaoServiceTest.class,
        CalculoValorLocacaoTest.class
})
public class SuiteExecucao {

    @BeforeClass
    public static void before() {
        System.out.println("Before1");
    }

    @AfterClass
    public static void after() {
        System.out.println("After");
    }
}
