package xadres;

import jogoTabuleiro.Peca;
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
	

	public boolean[][] movimentosPossiveis(PosicaoXadres posicaoOrigem){
		Posicao posicao = posicaoOrigem.paraPosicao();
		validaPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	

	public PecaXadres realizarMovimentoXadres(PosicaoXadres posicaoOrigem, PosicaoXadres posicaoDestino) {
		Posicao origem = posicaoOrigem.paraPosicao();
		Posicao destino = posicaoDestino.paraPosicao();
		validaPosicaoOrigem(origem);
		validaPosicaoDestino(origem, destino);
		Peca pecaCapturada = realizarMovimento(origem, destino);
		return (PecaXadres)pecaCapturada;
	}
	
	private void validaPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.existePeca(posicao)) {
			throw new XadresExcecao("Não existe Peça na posição de origem");
		}
		if(!tabuleiro.peca(posicao).existeUmMovimentoPossivel()) {
			throw new XadresExcecao("Não existe um movimwento possivel para esta peça");
		}
	}
	
	private void validaPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadresExcecao("Não é possivel mover a peça para a posição de destino");
		}
	}
	
	private Peca realizarMovimento(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removePeca(origem);
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		return pecaCapturada;
	}
	
	private void colocaNovaPeca(char coluna, int linha, PecaXadres peca) {
		tabuleiro.colocarPeca(peca,new PosicaoXadres(coluna, linha).paraPosicao());
	}
	
	private void configuracaoInicial() {
		colocaNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));

		colocaNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
	}
}
