package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ServiceProg implements Runnable{
	private Socket client;
	
	ServiceProg(Socket socket) {
		client = socket;
	}

	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println(ServiceRegistry.toStringue()+"##Que vouslez vous faire ? ##1. Installer un service ##2. Mettre à jour un service");
			int choix = Integer.parseInt(in.readLine());
			
			switch(choix) {
				case 1:
					out.println("Quel est le nom du service à installer ?");
			}
			
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
