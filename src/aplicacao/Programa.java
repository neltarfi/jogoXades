package aplicacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadres.PartidaXadres;
import xadres.PecaXadres;
import xadres.PosicaoXadres;
import xadres.XadresExcecao;

public class Programa {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadres partidaXadres = new PartidaXadres();
		while(true) {
			try {
				UI.limpaTela();
				UI.imprimeTabuleiro(partidaXadres.getPecas());
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadres origem = UI.lerPosicaoXadres(sc);
				System.out.println();
				System.out.print("Destino: ");
				PosicaoXadres destino = UI.lerPosicaoXadres(sc);
				PecaXadres pecaCapturada = partidaXadres.realizarMovimentoXadres(origem, destino);
			}
			catch (XadresExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		
	}

}
