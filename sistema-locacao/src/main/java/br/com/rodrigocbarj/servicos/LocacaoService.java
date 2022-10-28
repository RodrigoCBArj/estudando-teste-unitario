package br.com.rodrigocbarj.servicos;

import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.exceptions.FilmeSemEstoqueException;
import br.com.rodrigocbarj.exceptions.LocadoraException;

import java.util.Date;
import java.util.List;

import static br.com.rodrigocbarj.utils.DataUtils.adicionarDias;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes)
			throws LocadoraException, FilmeSemEstoqueException
	{
		Double valorTotal = 0.0;

		if (usuario == null)
			throw new LocadoraException("Usuário inexistente!");

		if (filmes == null || filmes.isEmpty())
			throw new LocadoraException("Filme inexistente!");

		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			if (filme.getEstoque() == 0 || filme.getEstoque() == null) {
				throw new FilmeSemEstoqueException();
			}

			Double valorFilme = filme.getPrecoLocacao();
			if (i == 2)	{
				valorTotal += valorFilme * 0.75;
			} else if (i == 3) {
				valorTotal += valorFilme * 0.50;
			} else if (i == 4) {
				valorTotal += valorFilme * 0.25;
			} else {
				valorTotal += valorFilme;
			}
		}

		Locacao locacao = new Locacao();
		locacao.setUsuario(usuario);
		locacao.setFilmes(filmes);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
}