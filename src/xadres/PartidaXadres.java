package xadres;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadres.pecas.Bispo;
import xadres.pecas.Cavalo;
import xadres.pecas.Peao;
import xadres.pecas.Rainha;
import xadres.pecas.Rei;
import xadres.pecas.Torre;

public class PartidaXadres {

	private int turn;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaXadres vuneravelEnPassant;
	private PecaXadres promocao;
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaXadres() {
		tabuleiro = new Tabuleiro(8, 8);
		turn = 1;
		jogadorAtual = Cor.BRANCO;
		configuracaoInicial();
	}

	public int getTurn() {
		return turn;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaXadres getVuneravelEnPassant() {
		return vuneravelEnPassant;
	}
	
	public PecaXadres getPromocao() {
		return promocao;
	}

	public PecaXadres[][] getPecas() {
		PecaXadres[][] mat = new PecaXadres[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadres) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] movimentosPossiveis(PosicaoXadres posicaoOrigem) {
		Posicao posicao = posicaoOrigem.paraPosicao();
		validaPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}

	public PecaXadres realizarMovimentoXadres(PosicaoXadres posicaoOrigem, PosicaoXadres posicaoDestino, String tipoPromocao) {
		Posicao origem = posicaoOrigem.paraPosicao();
		Posicao destino = posicaoDestino.paraPosicao();
		validaPosicaoOrigem(origem);
		validaPosicaoDestino(origem, destino);
		Peca pecaCapturada = realizarMovimento(origem, destino);
		if (testeCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadresExcecao("Você não pode se colocar em check");
		}
		PecaXadres pecaMovida = (PecaXadres) tabuleiro.peca(destino);
		
		// Movimento especial promoção
		promocao = null;
		if(pecaMovida instanceof Peao) {
			if(pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0  || pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7 ) {
				promocao = (PecaXadres)tabuleiro.peca(destino);
				promocao = substituirPecaPromovida(tipoPromocao);
			}
		}
		
		
		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
		if (testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			trocaTurn();
		}

		// Movimento especial en passant
		if (pecaMovida instanceof Peao && destino.getLinha() == origem.getLinha() - 2
				|| destino.getLinha() == origem.getLinha() + 2) {
			vuneravelEnPassant = pecaMovida;
		} else {
			vuneravelEnPassant = null;
		}
		return (PecaXadres) pecaCapturada;
	}

	private void validaPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.existePeca(posicao)) {
			throw new XadresExcecao("Não existe Peça na posição de origem");
		}
		if (jogadorAtual != ((PecaXadres) tabuleiro.peca(posicao)).getCor()) {
			throw new XadresExcecao("Essa peça não é sua");
		}
		if (!tabuleiro.peca(posicao).existeUmMovimentoPossivel()) {
			throw new XadresExcecao("Não existe um movimwento possivel para esta peça");
		}
	}

	private void validaPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadresExcecao("Não é possivel mover a peça para a posição de destino");
		}
	}
	
	public boolean testePromocao(PosicaoXadres posicaoOrigem, PosicaoXadres posicaoDestino) {
		Posicao origem = posicaoOrigem.paraPosicao();
		Posicao destino = posicaoDestino.paraPosicao();
		validaPosicaoOrigem(origem);
		validaPosicaoDestino(origem, destino);
		PecaXadres peca = (PecaXadres)tabuleiro.peca(origem);
		if(peca instanceof Peao) {
			if(destino.getLinha() == 0 || destino.getLinha() == 7) {
				return true;
			}
		}
		return false;
	}
	
	public PecaXadres substituirPecaPromovida(String tipo) {
		if(promocao == null) {
			throw new IllegalStateException("Não é uma peça promovida");
		}
		if(!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("T") && !tipo.equals("A")) {
			throw new InvalidParameterException("Tipo inválido de promoção");
		}
		Posicao pos = promocao.getPosicaoXadres().paraPosicao();
		Peca p = tabuleiro.removePeca(pos);
		pecasNoTabuleiro.remove(p);
		PecaXadres novaPeca = novaPeca(tipo, promocao.getCor());
		tabuleiro.colocarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);
		
		return null;
	}
	
	private PecaXadres novaPeca(String tipo, Cor cor) {
		if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
		if (tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if (tipo.equals("T")) return new Torre(tabuleiro, cor);
		return new Rainha(tabuleiro, cor);
	}

	private Peca realizarMovimento(Posicao origem, Posicao destino) {
		PecaXadres p = (PecaXadres) tabuleiro.removePeca(origem);
		p.incrementaCotagemDeMovimento();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		// Movimento especial Rock pequeno da torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadres torre = (PecaXadres) tabuleiro.removePeca(origemT);
			tabuleiro.colocarPeca(torre, destinoT);
			torre.incrementaCotagemDeMovimento();

		}
		// Movimento especial Rock grande da torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadres torre = (PecaXadres) tabuleiro.removePeca(origemT);
			tabuleiro.colocarPeca(torre, destinoT);
			torre.incrementaCotagemDeMovimento();

		}
		// Movimento especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if (p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removePeca(posicaoPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}
		return pecaCapturada;
	}

	public void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadres p = (PecaXadres) tabuleiro.removePeca(destino);
		p.decrementaCotagemDeMovimento();
		tabuleiro.colocarPeca(p, origem);
		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}
		// Movimento especial Rock pequeno da torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadres torre = (PecaXadres) tabuleiro.removePeca(destinoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.decrementaCotagemDeMovimento();

		}
		// Movimento especial Rock grande da torre
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadres torre = (PecaXadres) tabuleiro.removePeca(destinoT);
			tabuleiro.colocarPeca(torre, origemT);
			torre.decrementaCotagemDeMovimento();

		}
		// Movimento especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == vuneravelEnPassant) {
				PecaXadres peao = (PecaXadres)tabuleiro.removePeca(destino);
				Posicao posicaoPeao;
				if (p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.colocarPeca(peao,posicaoPeao);
			}
		}
	}

	public void trocaTurn() {
		turn++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	public Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private PecaXadres Rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadres) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (PecaXadres) p;
			}
		}
		throw new IllegalStateException("Não existe o Rei da cor " + cor + " no tabuleiro");
	}

	public boolean testeCheck(Cor cor) {
		Posicao posicaoDoRei = Rei(cor).getPosicaoXadres().paraPosicao();
		List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadres) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecasOponentes) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[posicaoDoRei.getLinha()][posicaoDoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	public boolean testeCheckMate(Cor cor) {
		if (!testeCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadres) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat.length; j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadres) p).getPosicaoXadres().paraPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = realizarMovimento(origem, destino);
						boolean testeCheck = testeCheck(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void colocaNovaPeca(char coluna, int linha, PecaXadres peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadres(coluna, linha).paraPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configuracaoInicial() {
		colocaNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		colocaNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

		colocaNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocaNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocaNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		colocaNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocaNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocaNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
		colocaNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));
	}
}
