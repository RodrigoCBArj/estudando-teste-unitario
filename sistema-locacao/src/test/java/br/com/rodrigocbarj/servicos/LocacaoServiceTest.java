package br.com.rodrigocbarj.servicos;
/* é importante que os testes estejam numa estrutura de pacotes iguais as do seu teste,
 * pois assim o Java permitirá que a classe de teste tenha acesso às variáveis
 * default, public e protected, da classe testada. [Variáveis private não é possível ter acesso]
 */

import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.exceptions.FilmeSemEstoqueException;
import br.com.rodrigocbarj.exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.com.rodrigocbarj.utils.DataUtils.isMesmaData;
import static br.com.rodrigocbarj.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LocacaoServiceTest {

    private LocacaoService service;

    private int contador = 0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    // roda antes de cada teste dessa classe
    @Before
    public void setup() {
        System.out.println("before");
        service = new LocacaoService();
    }

    // roda depois de cada teste dessa classe
    @After
    public void tearDown() {
        System.out.println("after");
        contador++;
        System.out.println(contador);
        /* o contador apresenta "1" em cada repetição, pois o JUnit "reinicia" as variáveis (não estáticas) sempre
         que um teste é finalizado, para garantir que nenhuma mudança de um teste implique em outro teste */
    }

    // roda uma vez, antes de rodar o primeiro teste da classe
    @BeforeClass
    public static void setupClass() {
        System.out.println("before class");
    }

    // roda uma vez, depois de rodar o ultimo teste da classe
    @AfterClass
    public static void tearDownClass() {
        System.out.println("after class");
    }

    @Test
    public void teste() throws Exception {

        // cenario
        Usuario u = new Usuario("Usuario 1");
        Filme f = new Filme("Filme 1", 2, 12.55);

        // ação
        Locacao locacao = service.alugarFilme(u, f);

        // verificações
        assertEquals(locacao.getFilme(), f);
        assertEquals(locacao.getUsuario(), u);
        error.checkThat(locacao.getValor(), is(12.55));
        error.checkThat(locacao.getValor(), is(not(0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Test
    public void testeLocacaoSemUsuario() throws FilmeSemEstoqueException {
        // cenario
        Filme f = new Filme("Filme 1", 20, 8.99);

        // ação
        try {
            service.alugarFilme(null, f);

            // validações:
            fail("Deveria lançar excessão para usuário nulo/inexistente.");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário inexistente!"));
        }
    }

    @Test
    public void testeLocacaoSemFilme() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario u = new Usuario("Usuario 1");

        //verificação (dessa forma, precisa ser declarado antes da ação)
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme inexistente!");

        // ação
        service.alugarFilme(u, null);
    }

    @Test(expected = FilmeSemEstoqueException.class) // verificação elegante =)
    public void testeFilmeSemEstoque() throws Exception {
        // cenario
        Usuario u = new Usuario("Usuario 1");
        Filme f = new Filme("Filme 1", 0, 12.55);

        // ação
        service.alugarFilme(u, f);
    }
}
