package aplicacao;

import java.util.Scanner;

import xadres.PartidaXadres;
import xadres.PecaXadres;
import xadres.PosicaoXadres;

public class Programa {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadres partidaXadres = new PartidaXadres();
		while(true) {
			UI.imprimeTabuleiro(partidaXadres.getPecas());
			System.out.println();
			System.out.print("Origem: ");
			PosicaoXadres origem = UI.lerPosicaoXadres(sc);
			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadres destino = UI.lerPosicaoXadres(sc);
			PecaXadres pecaCapturada = partidaXadres.realizarMovimentoXadres(origem, destino);
			
		}
		
	}

}
