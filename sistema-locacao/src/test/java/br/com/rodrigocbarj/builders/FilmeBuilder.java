package br.com.rodrigocbarj.builders;

import br.com.rodrigocbarj.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {
    }

    public static FilmeBuilder umFilme() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme("Filme", 1, 4.0);
        return builder;
    }

    public static FilmeBuilder umFilmeSemEstoque() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme("Filme", 0, 4.0);
        return builder;
    }

    public FilmeBuilder comValor(Double valor) {
        filme.setPrecoLocacao(valor);
        return this;
    }

    public Filme finalizado() {
        return filme;
    }
}
