package appliServeur;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import serveur.ServeurBRi;
import serveur.ServeurProg;
import serveur.ServiceRegistry;
import service.Service;

public class BRiLaunch {
	private final static int PORT_AMA = 3000;
	private final static int PORT_PROG = 4000;
	
	public static void main(String[] args) {
		Scanner clavier = new Scanner(System.in);

		URLClassLoader urlcl = null;
		
		System.out.println("Bonjour !");

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
