package br.com.rodrigocbarj.servicos;

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

        LocacaoService locacao = new LocacaoService();
        Usuario u = new Usuario("Teste Um");
        Filme f = new Filme("Filme 1", 1, 12.55);

        Locacao teste = locacao.alugarFilme(u, f);

        Assert.assertTrue(teste.getFilme().equals(f));
        Assert.assertTrue(teste.getUsuario().equals(u));
        Assert.assertTrue(teste.getValor().equals(12.55));
        Assert.assertTrue(DataUtils.isMesmaData(teste.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(teste.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}
