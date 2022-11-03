package br.com.rodrigocbarj.servicos;
/* é importante que os testes estejam numa estrutura de pacotes iguais as do seu deveAlugarFilme,
 * pois assim o Java permitirá que a classe de deveAlugarFilme tenha acesso às variáveis
 * default, public e protected, da classe testada. [Variáveis private não é possível ter acesso]
 */

import br.com.rodrigocbarj.daos.LocacaoDAO;
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

import static br.com.rodrigocbarj.builders.FilmeBuilder.umFilme;
import static br.com.rodrigocbarj.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.com.rodrigocbarj.builders.LocacaoBuilder.umaLocacao;
import static br.com.rodrigocbarj.builders.UsuarioBuilder.umUsuario;
import static br.com.rodrigocbarj.matchers.MatcherProprio.*;
import static br.com.rodrigocbarj.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    private LocacaoService locacaoService;
    private LocacaoDAO locacaoDAO;
    private SerasaService serasaService;
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        locacaoService = new LocacaoService();

        locacaoDAO = mock(LocacaoDAO.class);
        locacaoService.setLocacaoDAO(locacaoDAO);

        serasaService = mock(SerasaService.class);
        locacaoService.setSerasaService(serasaService);

        emailService = mock(EmailService.class);
        locacaoService.setEmailService(emailService);
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        // teste não executa nos sábados
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        // cenario
        Usuario u = umUsuario().finalizado();
        List<Filme> filmes = new ArrayList<>();
        filmes.add(umFilme().comValor(8d).finalizado());
        filmes.add(umFilme().comValor(5d).finalizado());

        // ação
        Locacao locacao = locacaoService.alugarFilme(u, filmes);

        // verificações
        error.checkThat(locacao.getValor(), is(13.0));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), hojeComAdicaoDias(1));
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        // cenario
        List<Filme> filmes = new ArrayList<>();
        filmes.add(umFilme().finalizado());
        filmes.add(umFilme().finalizado());

        // ação
        try {
            locacaoService.alugarFilme(null, filmes);

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
        Usuario u = umUsuario().finalizado();

        //verificação (dessa forma, precisa ser declarado antes da ação)
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme inexistente!");

        // ação
        locacaoService.alugarFilme(u, null);
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoFilmeSemEstoque()
            throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario u = umUsuario().finalizado();
//        List<Filme> filmes = new ArrayList<>(Arrays.asList(umFilme().semEstoque().finalizado()));
        List<Filme> filmes = new ArrayList<>(Arrays.asList(umFilmeSemEstoque().finalizado()));

        // ação
        locacaoService.alugarFilme(u, filmes);
    }

    @Test
    public void devedevolverNaSegundaCasoAoAlugarNoSabado()
            throws FilmeSemEstoqueException, LocadoraException {
        // teste só executa nos sábados
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario u = umUsuario().finalizado();
        List<Filme> filmes = new ArrayList<>(Arrays.asList(umFilme().finalizado()));

        Locacao locacao = locacaoService.alugarFilme(u, filmes);

//        error.checkThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY)); // também funcona
        error.checkThat(locacao.getDataRetorno(), caiNaSegunda()); // mais legível
    }

    @Test
    public void deveLancarExcecaoSeUsuarioEstiverNegativadoSerasa()
            throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().finalizado();
        List<Filme> filmes = Arrays.asList(umFilme().finalizado());

        when(serasaService.possuiNegativacao(usuario)).thenReturn(true);

        exception.expect(LocadoraException.class);
        exception.expectMessage("Usuário negativado!");

        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveNotificarLocacoesAtrasadas() {
        //cenário
        Usuario usuario = umUsuario().finalizado();
        List<Locacao> locacoes =
                Arrays.asList(umaLocacao()
                                .comUsuario(usuario)
                                .comDataRetorno(obterDataComDiferencaDias(-1))
                                .finalizada());
        when(locacaoDAO.obterLocacoesAtrasadas()).thenReturn(locacoes);

        //ação
        locacaoService.notificarAtrasos();

        //verificação
        verify(emailService).notificarAtrasos(usuario);
    }
}
