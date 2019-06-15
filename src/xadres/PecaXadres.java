package xadres;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Tabuleiro;

public abstract class PecaXadres extends Peca{
	private Cor cor;

	public PecaXadres(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	

}
