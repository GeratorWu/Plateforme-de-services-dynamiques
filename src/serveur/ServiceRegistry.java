package serveur;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import service.Service;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partag�e en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = new ArrayList<Class<? extends Service>>();
	}
	private static List<Class<?extends Service>> servicesClasses;


	public static void addService(Class<? extends Service> class1) throws ValidationException {
	    validation(class1);
	    
	    boolean existe = false;
	    for (Class<? extends Service> existingClass : servicesClasses) {
	        if (existingClass.getName().equals(class1.getName())) {
	            existe = true;
	            break;
	        }
	    }
	    
	    if (!existe) {
	        servicesClasses.add(class1);
	    } else {
	        throw new ValidationException("La classe de service existe d�j�");
	    }
	}

	
	public static void replaceService(Class<? extends Service> service) throws ValidationException{
		validation(service);

	    for (Class<? extends Service> existingClass : servicesClasses) {
	        if (existingClass.getName().equals(service.getName())) {
	        	servicesClasses.remove(existingClass);
				servicesClasses.add(service);
	        }else {
				throw new ValidationException("La classe de service n'est pas dans la liste");
	        }
	    }
	}
	
	// une m�thode de validation renvoie void et l�ve une exception si non validation
	// surtout pas de retour boolean !
	private static void validation(Class<? extends Service> classe) throws ValidationException {
		// cette partie pourrait �tre d�l�gu�e � un objet sp�cialis�
		// le constructeur avec Socket
		Constructor<? extends Service> c = null;
		try { 
			c = classe.getConstructor(java.net.Socket.class); 
		} catch (NoSuchMethodException e) {
			// transformation du type de l'exception quand l'erreur est d�tect�e par ce biais
			throw new ValidationException("Il faut un constructeur avec Socket");
		}
		int modifiers = c.getModifiers();
		if (!Modifier.isPublic(modifiers)) 
			throw new ValidationException("Le constructeur (Socket) doit �tre public");
		if (c.getExceptionTypes().length != 0)
			throw new ValidationException("Le constructeur (Socket) ne doit pas lever d'exception");
		// etc... avec tous les tests n�cessaires
		
	
	}
	
// renvoie la classe de service (numService -1)	
	public static Class<? extends Service> getServiceClass(int numService) {
		return servicesClasses.get(numService - 1);
	}
	
// liste les activit�s pr�sentes
	public static String toStringue() {
		String result = "Activit�s pr�sentes :##";
		int i = 1;
		// foreach n'est qu'un raccourci d'�criture 
		// donc il faut prendre le verrou explicitement sur la collection
		synchronized (servicesClasses) { 
			for (Class<? extends Service> s : servicesClasses) {
				try {
					Method toStringue = s.getMethod("toStringue");
					String string = (String) toStringue.invoke(s);
					result = result + i + " " + string+"##";
					i++;
				} catch (Exception e) {
					e.printStackTrace(); // normalement d�j� test� par validation()
				}
			}
		}
		return result;
	}

}
