package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProg {

	private final static int PORT_PROG = 4000;
	private final static String HOST = "localhost"; 

	public static void main(String[] args) {
		Socket s = null;		
		try {
			s = new Socket(HOST, PORT_PROG);
	
			BufferedReader sin = new BufferedReader (new InputStreamReader(s.getInputStream ( )));
			PrintWriter sout = new PrintWriter (s.getOutputStream ( ), true);
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));			
		
			System.out.println("Connect� au serveur " + s.getInetAddress() + ":"+ s.getPort());
			
			String line;
			boolean continuer = true;

			while (continuer) {
			    line = sin.readLine();
			    if (line != null) {
			        System.out.println(line.replaceAll("##", "\n"));
			        sout.println(clavier.readLine());
			    } else {
			        continuer = false;
			    }
			}
			
		}
		catch (IOException e) { System.err.println("Fin de la connexion"); }
		// Refermer dans tous les cas la socket
		try { if (s != null) s.close(); } 
		catch (IOException e2) { ; }		
	}

}
