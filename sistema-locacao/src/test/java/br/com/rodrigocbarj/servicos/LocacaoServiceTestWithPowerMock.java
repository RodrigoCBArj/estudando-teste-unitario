package br.com.rodrigocbarj.servicos;
/* é importante que os testes estejam numa estrutura de pacotes iguais as do seu deveAlugarFilme,
 * pois assim o Java permitirá que a classe de deveAlugarFilme tenha acesso às variáveis
 * default, public e protected, da classe testada. [Variáveis private não é possível ter acesso]
 */

import br.com.rodrigocbarj.daos.LocacaoDAO;
import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.utils.DataUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.com.rodrigocbarj.builders.FilmeBuilder.umFilme;
import static br.com.rodrigocbarj.builders.UsuarioBuilder.umUsuario;
import static br.com.rodrigocbarj.matchers.MatcherProprio.*;
import static br.com.rodrigocbarj.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.is;

@PowerMockIgnore("jdk.internal.reflect.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class, DataUtils.class})
public class LocacaoServiceTestWithPowerMock {

    @InjectMocks
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
        locacaoService = PowerMockito.spy(locacaoService);
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        // cenario
        Usuario u = umUsuario().finalizado();
        List<Filme> filmes = new ArrayList<>();
        filmes.add(umFilme().comValor(8d).finalizado());
        filmes.add(umFilme().comValor(5d).finalizado());

        PowerMockito.whenNew(Date.class).withNoArguments()
                .thenReturn(obterData(04, 11, 2022));

        // ação
        Locacao locacao = locacaoService.alugarFilme(u, filmes);

        // verificações
        error.checkThat(locacao.getValor(), is(13.0));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), hojeComAdicaoDias(1));
    }

    @Test
    public void devedevolverNaSegundaCasoAoAlugarNoSabado() throws Exception {
        Usuario u = umUsuario().finalizado();
        List<Filme> filmes = new ArrayList<>(Arrays.asList(umFilme().finalizado()));

        PowerMockito.whenNew(Date.class).withNoArguments()
                .thenReturn(obterData(05, 11, 2022));

        Locacao locacao = locacaoService.alugarFilme(u, filmes);

//        error.checkThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY)); // também funcona
        error.checkThat(locacao.getDataRetorno(), caiNaSegunda()); // mais legível
    }

    @Test
    public void deveCalcularValorTotal() throws Exception {
        List<Filme> filmes = Arrays.asList(umFilme().finalizado());

        Double valorTotal = (Double) Whitebox.invokeMethod(locacaoService, "calcularValorTotal", filmes);

        error.checkThat(valorTotal, is(4.0));
    }
}
