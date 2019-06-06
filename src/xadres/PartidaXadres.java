package xadres;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadres.pecas.Rei;
import xadres.pecas.Torre;

public class PartidaXadres {
	private Tabuleiro tabuleiro;
	
	public PartidaXadres() {
		tabuleiro = new Tabuleiro(8, 8);
		configuracaoInicial();
	}
	
	public PecaXadres[][] getPecas() {
		PecaXadres[][] mat = new PecaXadres[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i=0;i<tabuleiro.getLinhas();i++) {
			for(int j=0;j<tabuleiro.getColunas();j++) {
				mat[i][j] = (PecaXadres)tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	public void configuracaoInicial() {
		tabuleiro.colocarPeca(new Rei(tabuleiro, Cor.BRANCO), new Posicao(2, 1));
		tabuleiro.colocarPeca(new Torre(tabuleiro,Cor.PRETO), new Posicao(0, 4));
		tabuleiro.colocarPeca(new Rei(tabuleiro, Cor.PRETO), new Posicao(7, 4));
	}
}
