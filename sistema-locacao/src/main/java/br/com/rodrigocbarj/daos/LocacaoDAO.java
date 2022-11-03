package br.com.rodrigocbarj.daos;

import br.com.rodrigocbarj.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {

    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesAtrasadas();
}
