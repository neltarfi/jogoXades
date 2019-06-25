package xadres;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadres.pecas.Peao;
import xadres.pecas.Rei;
import xadres.pecas.Torre;

public class PartidaXadres {
	
	private int turn;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
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
		if (testeCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadresExcecao("Você não pode se colocar em check");
		}
		check = (testeCheck(oponente(jogadorAtual)))? true : false;
		if (testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			trocaTurn();
		}
		return (PecaXadres)pecaCapturada;
	}
	
	private void validaPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.existePeca(posicao)) {
			throw new XadresExcecao("Não existe Peça na posição de origem");
		}
		if(jogadorAtual != ((PecaXadres)tabuleiro.peca(posicao)).getCor()) {
			throw new XadresExcecao("Essa peça não é sua");
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
		PecaXadres p = (PecaXadres)tabuleiro.removePeca(origem);
		p.incrementaCotagemDeMovimento();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocarPeca(p, destino);
		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		return pecaCapturada;
	}
	
	public void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadres p = (PecaXadres)tabuleiro.removePeca(destino);
		p.decrementaCotagemDeMovimento();
		tabuleiro.colocarPeca(p, origem);
		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}
	}
	
	public void trocaTurn() {
		turn++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	public Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO)? Cor.PRETO : Cor.BRANCO;
	}
	
	private PecaXadres Rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadres)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : list) {
			if(p instanceof Rei) {
				return (PecaXadres)p;
			}
		}
		throw new IllegalStateException("Não existe o Rei da cor " + cor + " no tabuleiro");
	}
	
	public boolean testeCheck(Cor cor) {
		Posicao posicaoDoRei = Rei(cor).getPosicaoXadres().paraPosicao();
		List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadres)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for(Peca p : pecasOponentes) {
			boolean[][] mat = p.movimentosPossiveis();
			if(mat[posicaoDoRei.getLinha()][posicaoDoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean testeCheckMate(Cor cor) {
		if (!testeCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadres)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for(int i=0;i<mat.length;i++) {
				for(int j=0;j<mat.length;j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadres)p).getPosicaoXadres().paraPosicao();
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
		tabuleiro.colocarPeca(peca,new PosicaoXadres(coluna, linha).paraPosicao());
		pecasNoTabuleiro.add(peca);
	}
	
	private void configuracaoInicial() {
		colocaNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));

		colocaNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO));
		colocaNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));
	}
}
