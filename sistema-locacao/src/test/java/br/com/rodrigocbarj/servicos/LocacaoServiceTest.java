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
import br.com.rodrigocbarj.runners.ParallelRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.rodrigocbarj.builders.FilmeBuilder.umFilme;
import static br.com.rodrigocbarj.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.com.rodrigocbarj.builders.LocacaoBuilder.umaLocacao;
import static br.com.rodrigocbarj.builders.UsuarioBuilder.umUsuario;
import static br.com.rodrigocbarj.matchers.MatcherProprio.caiNaSegunda;
import static br.com.rodrigocbarj.matchers.MatcherProprio.hojeComAdicaoDias;
import static br.com.rodrigocbarj.utils.DataUtils.isMesmaData;
import static br.com.rodrigocbarj.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {

    @InjectMocks
    @Spy
    private LocacaoService locacaoService;

    @Mock
    private LocacaoDAO locacaoDAO;
    @Mock
    private SerasaService serasaService;
    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        System.out.println("iniciando");
    }

    @After
    public void end(){
        System.out.println("finalizando");
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        // cenario
        Usuario u = umUsuario().finalizado();
        List<Filme> filmes = new ArrayList<>();
        filmes.add(umFilme().comValor(8d).finalizado());
        filmes.add(umFilme().comValor(5d).finalizado());

        doReturn(obterData(04, 11, 2022)).when(locacaoService).obterData();

        // ação
        Locacao locacao = locacaoService.alugarFilme(u, filmes);

        // verificações
        error.checkThat(locacao.getValor(), is(13.0));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(04, 11, 2022)), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(05, 11, 2022)), is(true));
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
    public void devedevolverNaSegundaCasoAoAlugarNoSabado() throws Exception {
        Usuario u = umUsuario().finalizado();
        List<Filme> filmes = new ArrayList<>(Arrays.asList(umFilme().finalizado()));

        doReturn(obterData(05, 11, 2022)).when(locacaoService).obterData();

        Locacao locacao = locacaoService.alugarFilme(u, filmes);

        error.checkThat(locacao.getDataRetorno(), caiNaSegunda());
    }

    @Test
    public void deveLancarExcecaoSeUsuarioEstiverNegativadoSerasa() throws Exception {
        //cenário:
        Usuario usuario = umUsuario().finalizado();
        List<Filme> filmes = Arrays.asList(umFilme().finalizado());

        when(serasaService.possuiNegativacao(any(Usuario.class))) //para qualquer instancia de Usuario
                .thenReturn(true);

        //ação:
        try {
            locacaoService.alugarFilme(usuario, filmes);

            //verificação:
            fail("Deveria ter lançado uma LocadoraException! ");
        } catch (LocadoraException e) {
            error.checkThat(e.getMessage(), is("Usuário negativado!"));
        }
        verify(serasaService).possuiNegativacao(usuario);
        // tomar cuidado ao usar o verify(), pois isso deixa o teste menos "flexível,
        // nesse caso não era preciso, mas optei por utilizar apenas por questão de estudo
    }

    @Test
    public void deveNotificarLocacoesAtrasadas() {
        //cenário
        Usuario usuario1 = umUsuario().finalizado();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").finalizado();
        Usuario usuario3 = umUsuario().comNome("Usuario em atraso").finalizado();
        List<Locacao> locacoes =
                Arrays.asList(
                        umaLocacao().comUsuario(usuario1).atrasada().finalizada(),
                        umaLocacao().comUsuario(usuario2).finalizada(),
                        umaLocacao().comUsuario(usuario3).atrasada().finalizada(),
                        umaLocacao().comUsuario(usuario3).atrasada().finalizada());
        when(locacaoDAO.obterLocacoesAtrasadas()).thenReturn(locacoes);

        //ação
        locacaoService.notificarAtrasos();

        //verificação
        verify(emailService).notificarAtrasos(usuario1);
        verify(emailService, never()).notificarAtrasos(usuario2); //verifica se o usuario2 realmente não foi notificado
        verify(emailService, times(2)).notificarAtrasos(usuario3);
        verifyNoMoreInteractions(emailService); // verifica se realmente não houme mais nenhuma interação com o emailService
        verifyZeroInteractions(serasaService); // verifica se realmente não houve nenhuma interação com o serasaService

        // *Importante: deixar o mínimo de verify(), só coloquei todos esses para fins de conhecimento
    }

    @Test
    public void deveTestarErroNoSerasa() throws Exception {
        Usuario usuario = umUsuario().finalizado();
        List<Filme> filmes = Arrays.asList(umFilme().finalizado());

        when(serasaService.possuiNegativacao(usuario))
                .thenThrow(new Exception("Falha na consulta!"));

        exception.expect(LocadoraException.class);
        exception.expectMessage("Problemas com o Serasa, tente novamente.");

        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao() {
        Locacao locacao = umaLocacao().finalizada();

        locacaoService.prorrogarLocacao(locacao, 3);

        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(locacaoDAO).salvar(argCapt.capture());
        Locacao locacaoRetornada = argCapt.getValue();

        error.checkThat(locacaoRetornada.getValor(), is(12.0));
        error.checkThat(locacaoRetornada.getDataRetorno(), is(hojeComAdicaoDias(3)));
    }

    @Test
    public void deveCalcularValorTotal() throws Exception {
        List<Filme> filmes = Arrays.asList(umFilme().finalizado());

        Class<LocacaoService> locacaoServiceClass = LocacaoService.class;
        Method metodo = locacaoServiceClass.getDeclaredMethod("calcularValorTotal", List.class);
        metodo.setAccessible(true);
        Double valorTotal = (Double) metodo.invoke(locacaoService, filmes);

        error.checkThat(valorTotal, is(4.0));
    }
}
