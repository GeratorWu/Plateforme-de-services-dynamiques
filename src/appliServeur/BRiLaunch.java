package appliServeur;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import examples.Service;
import serveur.ServeurBRi;
import serveur.ServeurProg;
import serveur.ServiceRegistry;

public class BRiLaunch {
	private final static int PORT_AMA = 3000;
	private final static int PORT_PROG = 4000;
	
	public static void main(String[] args) {
		Scanner clavier = new Scanner(System.in);

		URLClassLoader urlcl = null;
		
		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		
		new Thread(new ServeurBRi(PORT_AMA)).start();
		new Thread(new ServeurProg(PORT_PROG)).start();
		
		while (true){
				try {
					String classeName = clavier.next();
					String fileNameURL = "ftp://localhost:2121/";
					urlcl = URLClassLoader.newInstance(new URL[] {new URL(fileNameURL)});
					ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
				} catch (Exception e) {
					System.out.println(e);
				}
			}	
	}
}
