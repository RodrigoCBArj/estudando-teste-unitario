package br.com.rodrigocbarj.servicos;
/* é importante que os testes estejam numa estrutura de pacotes iguais as do seu deveAlugarFilme,
 * pois assim o Java permitirá que a classe de deveAlugarFilme tenha acesso às variáveis
 * default, public e protected, da classe testada. [Variáveis private não é possível ter acesso]
 */

import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.exceptions.FilmeSemEstoqueException;
import br.com.rodrigocbarj.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.com.rodrigocbarj.utils.DataUtils.isMesmaData;
import static br.com.rodrigocbarj.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilme() throws Exception {

        // cenario
        Usuario u = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 12.55));
        filmes.add(new Filme("Filme 2", 4, 8.99));

        Double valorTotal = 0.0;
        for (Filme filme : filmes) valorTotal += filme.getPrecoLocacao();

        // ação
        Locacao locacao = service.alugarFilme(u, filmes);

        // verificações
        assertEquals(locacao.getFilmes(), filmes);
        assertEquals(locacao.getUsuario(), u);
        error.checkThat(locacao.getValor(), is(valorTotal));
//        error.checkThat(locacao.getValor(), is(not(0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        // cenario
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 12.55));
        filmes.add(new Filme("Filme 2", 4, 8.99));

        // ação
        try {
            service.alugarFilme(null, filmes);

            // validações:
            fail("Deveria lançar excessão para usuário nulo/inexistente.");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário inexistente!"));
        }
    }

    @Test
    public void naoDeveAlugarSemFilme()
            throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario u = new Usuario("Usuario 1");

        //verificação (dessa forma, precisa ser declarado antes da ação)
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme inexistente!");

        // ação
        service.alugarFilme(u, null);
    }

    @Test(expected = FilmeSemEstoqueException.class) // verificação elegante =)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        // cenario
        Usuario u = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 12.55));
        filmes.add(new Filme("Filme 2", 0, 8.99));

        // ação
        service.alugarFilme(u, filmes);
    }

    @Test
    public void devePagar75pctoNo3Filme()
            throws FilmeSemEstoqueException, LocadoraException {
        Usuario u = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 1, 4.0));
        filmes.add(new Filme("Filme 2", 2, 4.0));
        filmes.add(new Filme("Filme 3", 3, 4.0));

        Locacao resultado = service.alugarFilme(u, filmes);

        error.checkThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50pctoNo4Filme()
            throws FilmeSemEstoqueException, LocadoraException {
        Usuario u = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 1, 4.0));
        filmes.add(new Filme("Filme 2", 2, 4.0));
        filmes.add(new Filme("Filme 3", 3, 4.0));
        filmes.add(new Filme("Filme 4", 4, 4.0));

        Locacao resultado = service.alugarFilme(u, filmes);

        error.checkThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25pctoNo5Filme()
            throws FilmeSemEstoqueException, LocadoraException {
        Usuario u = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 1, 4.0));
        filmes.add(new Filme("Filme 2", 2, 4.0));
        filmes.add(new Filme("Filme 3", 3, 4.0));
        filmes.add(new Filme("Filme 4", 4, 4.0));
        filmes.add(new Filme("Filme 5", 5, 4.0));

        Locacao resultado = service.alugarFilme(u, filmes);

        error.checkThat(resultado.getValor(), is(14.0));
    }
}
