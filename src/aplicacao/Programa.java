package aplicacao;

import xadres.PartidaXadres;

public class Programa {

	public static void main(String[] args) {
		PartidaXadres partidaXadres = new PartidaXadres();
		UI.imprimiTabuleiro(partidaXadres.getPecas());
		
	}

}
