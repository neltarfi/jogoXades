package xadres.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadres.Cor;
import xadres.PecaXadres;

public class Cavalo extends PecaXadres {

	public Cavalo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "C";
	}

	public boolean podeMover(Posicao posicao) {
		PecaXadres p = (PecaXadres) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao p = new Posicao(0, 0);


		p.definirValores(posicao.getLinha() - 2, posicao.getColuna() - 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}


		p.definirValores(posicao.getLinha() - 2, posicao.getColuna() + 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}


		p.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 2);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}


		p.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 2);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}


		p.definirValores(posicao.getLinha() + 2, posicao.getColuna() + 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}


		p.definirValores(posicao.getLinha() + 2, posicao.getColuna() - 1);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}


		p.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 2);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 2);
		if (getTabuleiro().existePosicao(p) && podeMover(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}
		return mat;
	}

}