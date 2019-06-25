package xadres.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadres.Cor;
import xadres.PecaXadres;

public class Peao extends PecaXadres {

	public Peao(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
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
		} else {
			p.definirValores(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().existePosicao(p) && !getTabuleiro().existePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.definirValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
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
		}
		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}

}
