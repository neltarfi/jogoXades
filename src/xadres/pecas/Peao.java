package xadres.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadres.Cor;
import xadres.PartidaXadres;
import xadres.PecaXadres;

public class Peao extends PecaXadres {

	private PartidaXadres partidaXadres;

	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaXadres partidaXadres) {
		super(tabuleiro, cor);
		this.partidaXadres = partidaXadres;
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);
		if (getCor() == Cor.BRANCO) {
			p.definirValores(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().existePosicao(p) && !getTabuleiro().existePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.definirValores(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().existePosicao(p) && !getTabuleiro().existePeca(p) && getTabuleiro().existePosicao(p2)
					&& !getTabuleiro().existePeca(p2) && getContagemDeMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().existePosicao(p) && existeUmaPecaAdiverssaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().existePosicao(p) && existeUmaPecaAdiverssaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			// Movimento especial en passant Branco
			if (posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().existePosicao(esquerda) && existeUmaPecaAdiverssaria(esquerda)
						&& getTabuleiro().peca(esquerda) == partidaXadres.getVuneravelEnPassant()) {
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().existePosicao(direita) && existeUmaPecaAdiverssaria(direita)
						&& getTabuleiro().peca(direita) == partidaXadres.getVuneravelEnPassant()) {
					mat[direita.getLinha() - 1][direita.getColuna()] = true;
				}
			}
		} else {
			p.definirValores(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().existePosicao(p) && !getTabuleiro().existePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.definirValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().existePosicao(p) && !getTabuleiro().existePeca(p) && getTabuleiro().existePosicao(p2)
					&& !getTabuleiro().existePeca(p2) && getContagemDeMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().existePosicao(p) && existeUmaPecaAdiverssaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().existePosicao(p) && existeUmaPecaAdiverssaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			// Movimento especial en passant Preto
			if (posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().existePosicao(esquerda) && existeUmaPecaAdiverssaria(esquerda)
						&& getTabuleiro().peca(esquerda) == partidaXadres.getVuneravelEnPassant()) {
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().existePosicao(direita) && existeUmaPecaAdiverssaria(direita)
						&& getTabuleiro().peca(direita) == partidaXadres.getVuneravelEnPassant()) {
					mat[direita.getLinha() + 1][direita.getColuna()] = true;
				}
			}
		}
		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}

}
