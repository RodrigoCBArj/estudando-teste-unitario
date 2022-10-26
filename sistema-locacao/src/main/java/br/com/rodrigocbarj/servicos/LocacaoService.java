package br.com.rodrigocbarj.servicos;

import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.exceptions.FilmeSemEstoqueException;
import br.com.rodrigocbarj.exceptions.LocadoraException;

import java.util.Date;

import static br.com.rodrigocbarj.utils.DataUtils.adicionarDias;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme)
			throws LocadoraException, FilmeSemEstoqueException
	{
		if (usuario == null)
			throw new LocadoraException("Usuário inexistente!");

		if (filme == null)
			throw new LocadoraException("Filme inexistente!");

		if (filme.getEstoque() == 0 || filme.getEstoque() == null)
			throw new FilmeSemEstoqueException();

		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
}