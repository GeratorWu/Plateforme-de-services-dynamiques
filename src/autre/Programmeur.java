package autre;

import java.util.HashMap;

public class Programmeur {
	private String login;
	private String mdp;
	private String ftp;
	private boolean certifie;
	private final static HashMap<String, Programmeur> listeProg = new HashMap<>();
	
	public Programmeur(String login, String mdp) {
		this.login = login;
		this.mdp = mdp;
		this.ftp = "";
		this.certifie = false;
		
		if(!listeProg.containsKey(login)) {
			listeProg.put(login, this);
		}
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public String getMdp() {
		return this.mdp;
	}
	
	public String getFtp() {
		return this.ftp;
	}
	
	public boolean getCertifie() {
		return this.certifie;
	}

	public static Programmeur getProg(String login) {
		return listeProg.get(login);
	}
	
	public void setFtp(String ftp) {
		this.ftp = ftp;
		this.certifie = true;
	}
	
	public boolean verifProg(String mdp) {
		return (this.mdp.equals(mdp));
	}
	
}
