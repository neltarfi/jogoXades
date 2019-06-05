package Xades;

import jogoTabuleiro.Tabuleiro;

public class PartidaXadres {
	private Tabuleiro tabuleiro;
	
	public PartidaXadres() {
		tabuleiro = new Tabuleiro(8, 8);
	}
	
	public PecaXadres[][] getPecas() {
		PecaXadres[][] mat = new PecaXadres[tabuleiro.getLinha()][tabuleiro.getColuna()];
		for (int i=0;i<tabuleiro.getLinha();i++) {
			for(int j=0;j<tabuleiro.getColuna();j++) {
				mat[i][j] = (PecaXadres)tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
}
