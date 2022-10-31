package br.com.rodrigocbarj.servicos;

import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.exceptions.FilmeSemEstoqueException;
import br.com.rodrigocbarj.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    @Parameterized.Parameter//(value = 0) // (valor padrão)
    public String cenario;
    @Parameterized.Parameter(value = 1)
    public List<Filme> filmes;
    @Parameterized.Parameter(value = 2)
    public Double valorTotal;

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    private static Filme filme1 = new Filme("Filme 1", 1, 4.0);
    private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
    private static Filme filme3 = new Filme("Filme 3", 3, 4.0);
    private static Filme filme4 = new Filme("Filme 4", 4, 4.0);
    private static Filme filme5 = new Filme("Filme 5", 5, 4.0);
    private static Filme filme6 = new Filme("Filme 6", 6, 4.0);
    private static Filme filme7 = new Filme("Filme 7", 7, 4.0);

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
            {"2 filmes - sem desconto", Arrays.asList(filme1, filme2), 8.0},
            {"3 filmes - 25% desconto", Arrays.asList(filme1, filme2, filme3), 11.0},
            {"4 filmes - 50% desconto", Arrays.asList(filme1, filme2, filme3, filme4), 13.0},
            {"5 filmes - 75% desconto", Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0},
            {"6 filmes - 100% desconto", Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0},
            {"7 filmes - sem desconto", Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0}
        });
    }

    @Test
    public void deveCalcularValorLocacaoComDescontos()
            throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");

        Locacao locacao = service.alugarFilme(usuario, filmes);

        assertEquals(locacao.getValor(), valorTotal);
    }
}
