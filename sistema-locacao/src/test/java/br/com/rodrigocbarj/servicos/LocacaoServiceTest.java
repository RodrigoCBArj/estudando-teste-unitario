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
import br.com.rodrigocbarj.utils.DataUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.*;

import static br.com.rodrigocbarj.matchers.MatcherProprio.*;
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
        // teste não executa nos sábados
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

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
        error.checkThat(locacao.getValor(), is(valorTotal));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), hojeComAdicaoDias(1));
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

    @Test
    public void devedevolverNaSegundaCasoAoAlugarNoSabado()
            throws FilmeSemEstoqueException, LocadoraException {
        // teste só executa nos sábados
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario u = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>(
                Arrays.asList(new Filme("Filme 1", 1, 4.0)));

        Locacao locacao = service.alugarFilme(u, filmes);

//        error.checkThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY)); // também funcona
        error.checkThat(locacao.getDataRetorno(), caiNaSegunda()); // mais legível
    }
}
