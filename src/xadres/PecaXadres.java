package xadres;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;

public abstract class PecaXadres extends Peca{
	private Cor cor;
	private int contagemDeMovimento;

	public PecaXadres(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public int getContagemDeMovimento() {
		return contagemDeMovimento;
	}
	
	public void incrementaCotagemDeMovimento() {
		contagemDeMovimento++;
	}
	
	public void decrementaCotagemDeMovimento() {
		contagemDeMovimento--;
	}
	public PosicaoXadres getPosicaoXadres() {
		return PosicaoXadres.dePosicao(posicao);
	}
	
	protected boolean existeUmaPecaAdiverssaria(Posicao posicao) {
		PecaXadres p = (PecaXadres)getTabuleiro().peca(posicao);
		return p != null && p.cor != cor;
	}

}
