package br.com.rodrigocbarj.builders;

import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;

import java.util.Arrays;
import java.util.Date;

import static br.com.rodrigocbarj.builders.FilmeBuilder.umFilme;
import static br.com.rodrigocbarj.builders.UsuarioBuilder.umUsuario;
import static br.com.rodrigocbarj.utils.DataUtils.obterDataComDiferencaDias;


public class LocacaoBuilder {
    private Locacao locacao;

    private LocacaoBuilder() {
    }

    public static LocacaoBuilder umLocacao() {
        LocacaoBuilder builder = new LocacaoBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(LocacaoBuilder builder) {
        builder.locacao = new Locacao();
        Locacao l = builder.locacao;


        l.setUsuario(umUsuario().finalizado());
        l.setFilmes(Arrays.asList(umFilme().finalizado()));
        l.setDataLocacao(new Date());
        l.setDataRetorno(obterDataComDiferencaDias(1));
        l.setValor(4.0);
    }

    public LocacaoBuilder comUsuario(Usuario param) {
        locacao.setUsuario(param);
        return this;
    }

    public LocacaoBuilder comListaFilmes(Filme... params) {
        locacao.setFilmes(Arrays.asList(params));
        return this;
    }

    public LocacaoBuilder comDataLocacao(Date param) {
        locacao.setDataLocacao(param);
        return this;
    }

    public LocacaoBuilder comDataRetorno(Date param) {
        locacao.setDataRetorno(param);
        return this;
    }

    public LocacaoBuilder comValor(Double param) {
        locacao.setValor(param);
        return this;
    }

    public Locacao finalizada() {
        return locacao;
    }
}