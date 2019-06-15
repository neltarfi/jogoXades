package xadres.pecas;

import jogoTabuleiro.Tabuleiro;
import xadres.Cor;
import xadres.PecaXadres;

public class Rei extends PecaXadres {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		return mat;
	}
	

}
