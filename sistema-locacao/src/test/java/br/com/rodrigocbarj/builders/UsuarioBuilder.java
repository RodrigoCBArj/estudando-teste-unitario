package br.com.rodrigocbarj.builders;

import br.com.rodrigocbarj.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {
    } // privado, para que só possa ser construído pelo método abaixo

    // chaining method
    public static UsuarioBuilder umUsuario() {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario("Usuario");
        return builder;
    }

    public Usuario finalizado() {
        return usuario;
    }
}
