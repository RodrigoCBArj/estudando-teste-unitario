package br.com.rodrigocbarj.servicos;
/* é importante que os testes estejam numa estrutura de pacotes iguais as do seu teste,
 * pois assim o Java permitirá que a classe de teste tenha acesso às variáveis
 * default, public e protected, da classe testada. [Variáveis private não é possível ter acesso]
 */

import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.com.rodrigocbarj.utils.DataUtils.isMesmaData;
import static br.com.rodrigocbarj.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void teste() throws Exception {

        // cenario
        LocacaoService service = new LocacaoService();
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

    @Test(expected = Exception.class) // verificação elegante =)
    public void testFilmeSemEstoque() throws Exception {
        // cenario
        LocacaoService service = new LocacaoService();
        Usuario u = new Usuario("Usuario 1");
        Filme f = new Filme("Filme 1", 0, 12.55);

        // ação
        service.alugarFilme(u, f);
    }
    @Test
    public void testFilmeSemEstoque2() {
        // cenario
        LocacaoService service = new LocacaoService();
        Usuario u = new Usuario("Usuario 1");
        Filme f = new Filme("Filme 1", 0, 12.55);

        // ação
        try {
            service.alugarFilme(u, f);
            fail("Deveria ter lançado uam excessao");
        } catch (Exception e) {
            // verificação
            assertThat(e.getMessage(), is("Filme sem estoque!"));
        }
    }

    @Test
    public void testFilmeSemEstoque3() throws Exception {
        // cenario
        LocacaoService service = new LocacaoService();
        Usuario u = new Usuario("Usuario 1");
        Filme f = new Filme("Filme 1", 0, 12.55);

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque!");

        // ação
        service.alugarFilme(u, f);
    }
}
