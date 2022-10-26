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

import java.util.Date;

import static br.com.rodrigocbarj.utils.DataUtils.isMesmaData;
import static br.com.rodrigocbarj.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Test
    public void teste() {

        // cenario
        LocacaoService service = new LocacaoService();
        Usuario u = new Usuario("Usuario 1");
        Filme f = new Filme("Filme 1", 1, 12.55);

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
}
