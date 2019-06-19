package aplicacao;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import xadres.Cor;
import xadres.PartidaXadres;
import xadres.PecaXadres;
import xadres.PosicaoXadres;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_PRETO = "\u001B[30m";
	public static final String ANSI_VERMELHO = "\u001B[31m";
	public static final String ANSI_VERDE = "\u001B[32m";
	public static final String ANSI_AMARELO = "\u001B[33m";
	public static final String ANSI_AZUL = "\u001B[34m";
	public static final String ANSI_ROXO = "\u001B[35m";
	public static final String ANSI_CIANO = "\u001B[36m";
	public static final String ANSI_BRANCO = "\u001B[37m";

	public static final String ANSI_PRETO_FUNDO = "\u001B[40m";
	public static final String ANSI_VERMELHO_FUNDO = "\u001B[41m";
	public static final String ANSI_VERDE_FUNDO = "\u001B[42m";
	public static final String ANSI_AMARELO_FUNDO = "\u001B[43m";
	public static final String ANSI_AZUL_FUNDO = "\u001B[44m";
	public static final String ANSI_ROXO_FUNDO = "\u001B[45m";
	public static final String ANSI_CIANO_FUNDO = "\u001B[46m";
	public static final String ANSI_BRANCO_FUNDO = "\u001B[47m";

	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	public static void limpaTela() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static PosicaoXadres lerPosicaoXadres(Scanner sc) {
		try{
			String s = sc.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1));
			return new PosicaoXadres(coluna, linha);
		}	
		catch (RuntimeException e) {
			throw new InputMismatchException("Erro lendo Posição de Xadres: Valores válidos de a1 a h8");
			}
	}
	
	public static void imprimePartida(PartidaXadres partidaXadres, List<PecaXadres> capturadas) {
		imprimeTabuleiro(partidaXadres.getPecas());
		System.out.println();
		imprimirPecasCapturadas(capturadas);
		System.out.println();
		System.out.println("Turn: " + partidaXadres.getTurn());
		if(!partidaXadres.getCheckMate()) {
			System.out.println("Esperando o jogador: " + partidaXadres.getJogadorAtual());
			if (partidaXadres.getCheck()) {
				System.out.println("CHECK!");
			}
		}
		else {
			System.out.println("CHECKMATE!");
			System.out.println("Ganhador: " + partidaXadres.getJogadorAtual());
		}
	}
	
	public static void imprimeTabuleiro(PecaXadres[][] pecas) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pecas.length; j++) {
				imprimePeca(pecas[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	
	public static void imprimeTabuleiro(PecaXadres[][] pecas, boolean[][] movimentosPossiveis) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.print(8 - i + " ");
			for (int j = 0; j < pecas.length; j++) {
				imprimePeca(pecas[i][j], movimentosPossiveis[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	private static void imprimePeca(PecaXadres peca, boolean fundo) {
		if(fundo) {
			System.out.print(ANSI_AZUL_FUNDO);
		}
		if (peca == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            if (peca.getCor() == Cor.BRANCO) {
                System.out.print(ANSI_BRANCO + peca + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_AMARELO + peca + ANSI_RESET);
            }
        }
        System.out.print(" ");
	}
	
	public static void imprimirPecasCapturadas(List<PecaXadres> capturadas) {
		List<PecaXadres> branca = capturadas.stream().filter(x -> x.getCor() == Cor.BRANCO).collect(Collectors.toList());
		List<PecaXadres> preta = capturadas.stream().filter(x -> x.getCor() == Cor.PRETO).collect(Collectors.toList());
		System.out.println("Peças capturadas");
		System.out.print("Brancas: ");
		System.out.print(ANSI_BRANCO);
		System.out.println(Arrays.toString(branca.toArray()));
		System.out.println(ANSI_RESET);
		System.out.print("Pretas: ");
		System.out.print(ANSI_AMARELO);
		System.out.println(Arrays.toString(preta.toArray()));
		System.out.println(ANSI_RESET);
	}

}
