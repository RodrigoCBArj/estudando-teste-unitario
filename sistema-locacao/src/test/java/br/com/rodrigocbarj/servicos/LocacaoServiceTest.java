package br.com.rodrigocbarj.servicos;
/* é importante que os testes estejam numa estrutura de pacotes iguais as do seu teste,
 * pois assim o Java permitirá que a classe de teste tenha acesso às variáveis
 * default, public e protected, da classe testada. [Variáveis private não é possível ter acesso]
 */

import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LocacaoServiceTest {

    @Test
    public void teste() {

        // cenario
        LocacaoService locacao = new LocacaoService();
        Usuario u = new Usuario("Usuario 1");
        Filme f = new Filme("Filme 1", 1, 12.55);

        // ação
        Locacao teste = locacao.alugarFilme(u, f);

        // verificações
        Assert.assertEquals(teste.getFilme(), f);
        Assert.assertEquals(teste.getUsuario(), u);
        Assert.assertEquals(12.55, teste.getValor(), 0.01); // [experado], [atual], [margem de erro]
        Assert.assertTrue(DataUtils.isMesmaData(teste.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(teste.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}
