package br.com.rodrigocbarj.servicos;

import br.com.rodrigocbarj.daos.LocacaoDAO;
import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.exceptions.FilmeSemEstoqueException;
import br.com.rodrigocbarj.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.com.rodrigocbarj.builders.FilmeBuilder.umFilme;
import static br.com.rodrigocbarj.builders.UsuarioBuilder.umUsuario;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private LocacaoDAO locacaoDAO;
    @Mock
    private SerasaService serasaService;

    @Parameterized.Parameter//(value = 0) // (valor padrão)
    public String cenario;
    @Parameterized.Parameter(value = 1)
    public List<Filme> filmes;
    @Parameterized.Parameter(value = 2)
    public Double valorTotal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private static final Filme filme1 = umFilme().finalizado();
    private static final Filme filme2 = umFilme().finalizado();
    private static final Filme filme3 = umFilme().finalizado();
    private static final Filme filme4 = umFilme().finalizado();
    private static final Filme filme5 = umFilme().finalizado();
    private static final Filme filme6 = umFilme().finalizado();
    private static final Filme filme7 = umFilme().finalizado();

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                {"2° filme - sem desconto", Arrays.asList(filme1, filme2), 8.0},
                {"3° filme - 25% desconto", Arrays.asList(filme1, filme2, filme3), 11.0},
                {"4° filme - 50% desconto", Arrays.asList(filme1, filme2, filme3, filme4), 13.0},
                {"5° filme - 75% desconto", Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0},
                {"6° filme - 100% desconto",Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0},
                {"7° filme - sem desconto", Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0}
        });
    }

    @Test
    public void deveCalcularValorLocacaoComDescontos()
            throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().finalizado();

        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        assertEquals(locacao.getValor(), valorTotal);
    }
}
