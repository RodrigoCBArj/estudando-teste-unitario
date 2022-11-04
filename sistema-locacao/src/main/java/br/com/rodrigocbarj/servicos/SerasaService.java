package br.com.rodrigocbarj.servicos;

import br.com.rodrigocbarj.entidades.Usuario;

public interface SerasaService {

    boolean possuiNegativacao(Usuario usuario) throws Exception;
}
