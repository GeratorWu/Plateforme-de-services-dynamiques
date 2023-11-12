package appliServeur;

import serveur.ServeurBRi;
import serveur.ServeurProg;

public class BRiLaunch {
	private final static int PORT_AMA = 3000;
	private final static int PORT_PROG = 4000;
	
	public static void main(String[] args) {
		
		System.out.println("Bonjour !");

		new Thread(new ServeurBRi(PORT_AMA)).start();
		new Thread(new ServeurProg(PORT_PROG)).start();
		
		//ftp://localhost:2121/
	}
}
