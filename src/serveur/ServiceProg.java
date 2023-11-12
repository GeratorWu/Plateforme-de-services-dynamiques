package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import autre.Programmeur;
import service.Service;

public class ServiceProg implements Runnable{
	
	private Socket client;
	URLClassLoader urlcl = null;
	String ftp;
	
	ServiceProg(Socket socket) {
		client = socket;
	}

	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println("Bonjour, quel est votre login ?");
			String login = in.readLine();
			out.println("Quel est le mot de passe ?");
			String mdp = in.readLine();
			
			Programmeur utilisateur = Programmeur.getProg(login);
			
			if(utilisateur == null) {
				Programmeur newUtilisateur = new Programmeur(login, mdp);
				out.println("Votre compte a �t� cr�� avec succ�s, quel est l'adresse de votre serveur FTP ?");
				ftp = in.readLine();
				newUtilisateur.setFtp(ftp);
			}
			else if (utilisateur.verifProg(mdp)){
				ftp = utilisateur.getFtp();
			}
			else {
				out.println("Mot de passe incorrect");
				client.close();
			}
			
			
			out.println(ServiceRegistry.toStringue()+"##Que voulez vous faire ? ##1. Fournir un nouveau service ##2. Mettre � jour un service ##3. D�clarer un changement d�adresse de son serveur ftp ##4. Arr�ter un service ##5. D�marrer un service ##6. D�sinstaller un service");
			int choix = Integer.parseInt(in.readLine());
			
			String fileNameURL = ftp;
			switch(choix) {
				case 1:
					out.println("Quel est le nom du service � installer ?");
					try {
						String classeName = in.readLine();
						urlcl = URLClassLoader.newInstance(new URL[] {new URL(fileNameURL)});
						ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
					} catch (Exception e) {
						System.out.println(e);
					}
					break;
				case 2:
					out.println("Quel est le nom du service que voulez vous mettre � jour ?");
					try {
						String classeName = in.readLine();
						urlcl = URLClassLoader.newInstance(new URL[] {new URL(fileNameURL)});
						ServiceRegistry.replaceService(urlcl.loadClass(classeName).asSubclass(Service.class));
					} catch (Exception e) {
						System.out.println(e);
					}
					break;
				case 3:
					out.println("Voici votre adresse ftp actuelle :" + ftp + ". Quel est la nouvelle adresse ftp ?");
					utilisateur.setFtp(in.readLine());
					break;
				case 4:
					out.println("Quel est le num�ro du service que vous voulez arr�ter ?");
					int stop = Integer.parseInt(in.readLine());
					ServiceRegistry.stopService(stop);
					break;
				case 5:
					out.println(ServiceRegistry.toStringueStop() + "Quel est le num�ro du service que vous voulez d�marrer ?");
					int start = Integer.parseInt(in.readLine());
					ServiceRegistry.startService(start);
					break;
				case 6:
					out.println("Quel est le num�ro du service que vous voulez d�sinstaller ?");
					int desinstaller = Integer.parseInt(in.readLine());
					ServiceRegistry.removeService(desinstaller);
					break;
				default:
					break;
			}
			
			}
		catch (IOException | IllegalArgumentException | SecurityException e) {
			//Fin du service
		}

		try {client.close();} catch (IOException e2) {}
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	// lancement du service
	public void start() {
		(new Thread(this)).start();		
	}
}
