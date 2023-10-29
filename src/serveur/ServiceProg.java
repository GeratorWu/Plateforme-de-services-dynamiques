package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import autre.Programmeur;

public class ServiceProg implements Runnable{
	
	private Socket client;
	
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
				out.println("Votre compte a été créé avec succès, quel est l'adresse de votre serveur FTP ?");
				String ftp = in.readLine();
				newUtilisateur.setFtp(ftp);
			}
			else {
				String ftp = utilisateur.getFtp();
			}
			
			
			out.println(ServiceRegistry.toStringue()+"##Que voulez vous faire ? ##1. Fournir un nouveau service ##2. Mettre à jour un service ##3. Déclarer un changement d’adresse de son serveur ftp");
			int choix = Integer.parseInt(in.readLine());
			
			/*switch(choix) {
				case 1:
					out.println("Quel est le nom du service à installer ?");
					break;
				case 2:
					out.println("Quel service voulez vous mettre à jour ? ?");
					break;
				case 3:
					out.println("Quel est la nouvelle adresse ftp ?");
					break;
				default:
					break;
			}*/
			
			Class<? extends Service> classe = ServiceRegistry.getServiceClass(choix);
			Service service = classe.getConstructor(java.net.Socket.class).newInstance(this.client);
			service.run();
			// instancier le service numéro "choix" en lui passant la socket "client"
			// invoquer run() pour cette instance ou la lancer dans un thread à part 
				
			}
		catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
