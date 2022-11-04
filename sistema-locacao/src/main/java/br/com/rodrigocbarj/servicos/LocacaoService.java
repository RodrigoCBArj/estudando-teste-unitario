package br.com.rodrigocbarj.servicos;

import br.com.rodrigocbarj.daos.LocacaoDAO;
import br.com.rodrigocbarj.entidades.Filme;
import br.com.rodrigocbarj.entidades.Locacao;
import br.com.rodrigocbarj.entidades.Usuario;
import br.com.rodrigocbarj.exceptions.FilmeSemEstoqueException;
import br.com.rodrigocbarj.exceptions.LocadoraException;
import br.com.rodrigocbarj.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.com.rodrigocbarj.utils.DataUtils.adicionarDias;

public class LocacaoService {

	private LocacaoDAO locacaoDAO;
	private SerasaService serasaService;
	private EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes)
			throws LocadoraException, FilmeSemEstoqueException
	{
		Double valorTotal = 0.0;

		boolean negativado;
		try {
			negativado = serasaService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas com o Serasa, tente novamente.");
		}

		if (negativado)
			throw new LocadoraException("Usuário negativado!");

		if (usuario == null)
			throw new LocadoraException("Usuário inexistente!");

		if (filmes == null || filmes.isEmpty())
			throw new LocadoraException("Filme inexistente!");

		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			if (filme.getEstoque() < 1 || filme.getEstoque() == null) {
				throw new FilmeSemEstoqueException();
			}

			Double valorFilme = filme.getPrecoLocacao();
			switch (i) {
				case 2: valorFilme = valorFilme * 0.75; break;
				case 3: valorFilme = valorFilme * 0.50; break;
				case 4: valorFilme = valorFilme * 0.25; break;
				case 5: valorFilme = 0.0; break;
			}
			valorTotal += valorFilme;
		}

		Locacao locacao = new Locacao();
		locacao.setUsuario(usuario);
		locacao.setFilmes(filmes);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		//Salvando a locacao...
		locacaoDAO.salvar(locacao);

		return locacao;
	}

	public void notificarAtrasos() {
		List<Locacao> locacoes = locacaoDAO.obterLocacoesAtrasadas();
		for (Locacao l : locacoes) {
			if (l.getDataRetorno().before(new Date()))
				emailService.notificarAtrasos(l.getUsuario());
		}
	}

	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setValor(locacao.getValor() * dias);
		novaLocacao.setDataLocacao(locacao.getDataLocacao());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		locacaoDAO.salvar(novaLocacao);
	}
}