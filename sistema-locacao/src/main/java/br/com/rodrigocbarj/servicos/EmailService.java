package br.com.rodrigocbarj.servicos;

import br.com.rodrigocbarj.entidades.Usuario;

public interface EmailService {

    void notificarAtrasos(Usuario usuario);
}
