package aplicacao;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadres.PartidaXadres;
import xadres.PecaXadres;
import xadres.PosicaoXadres;
import xadres.XadresExcecao;

public class Programa {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadres partidaXadres = new PartidaXadres();
		String tipoPromocao;
		List<PecaXadres> capturadas = new ArrayList<>();
		while(!partidaXadres.getCheckMate()) {
			try {
				UI.limpaTela();
				UI.imprimePartida(partidaXadres, capturadas);
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadres origem = UI.lerPosicaoXadres(sc);
				
				boolean[][] movimentosPossiveis = partidaXadres.movimentosPossiveis(origem);
				UI.limpaTela();
				UI.imprimeTabuleiro(partidaXadres.getPecas(), movimentosPossiveis);
				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadres destino = UI.lerPosicaoXadres(sc);
				if (partidaXadres.testePromocao(origem, destino)) {
					System.out.print("Entre com a peça para promoção (A/B/C/T): ");
					tipoPromocao = sc.nextLine();
					UI.testeTipo(tipoPromocao);
				}
				else {
					tipoPromocao = null;
				}
				PecaXadres pecaCapturada = partidaXadres.realizarMovimentoXadres(origem, destino, tipoPromocao);
				if (pecaCapturada != null) {
					capturadas.add(pecaCapturada);
				}
			}
			catch (XadresExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (IllegalStateException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InvalidParameterException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.limpaTela();
		UI.imprimePartida(partidaXadres, capturadas);
		
	}

}
