package xadres;

import jogoTabuleiro.Posicao;

public class PosicaoXadres {
		private char coluna;
		private int linha;
		
		public PosicaoXadres(char coluna, int linha) {
			if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
				throw new XadresExcecao("Erro instanciando PosicaoXadres: valores válidos são de 'a1' a 'h8'");
			}
			this.coluna = coluna;
			this.linha = linha;
		}

		public char getColuna() {
			return coluna;
		}

		public int getLinha() {
			return linha;
		}
		
		protected Posicao paraPosicao() {
			return new Posicao(8 - linha, coluna - 'a');
		}
		
		protected static PosicaoXadres dePosicao(Posicao posicao) {
			return new PosicaoXadres((char)('a' + posicao.getColuna()),8 - posicao.getLinha());
		}
		
		@Override
		public String toString() {
			return "" + coluna + linha;
		}

}
