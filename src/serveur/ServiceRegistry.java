package serveur;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import service.Service;

public class ServiceRegistry {
	
	private static List<Class<?extends Service>> servicesClasses;
	private static List<Class<?extends Service>> servicesStop;

	static {
		servicesClasses = new ArrayList<Class<? extends Service>>();
		servicesStop =new ArrayList<Class<? extends Service>>();
	}

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
	        throw new ValidationException("La classe de service existe déjà");
	    }
	}
	
	public static void removeService(int i){	    
	    servicesClasses.remove(i-1);
	}
	
	public static void startService(int i){	    
		Class<? extends Service> servicetmp = servicesStop.get(i-1);
	    servicesStop.remove(i-1);
	    servicesClasses.add(servicetmp);
	}
	
	public static void stopService(int i){	    
		Class<? extends Service> servicetmp = servicesClasses.get(i-1);
	    servicesClasses.remove(i-1);
	    servicesStop.add(servicetmp);
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
	
	// une méthode de validation renvoie void et lève une exception si non validation
	// surtout pas de retour boolean !
	private static void validation(Class<? extends Service> classe) throws ValidationException {
		// cette partie pourrait être déléguée à un objet spécialisé
		// le constructeur avec Socket
		Constructor<? extends Service> c = null;
		try { 
			c = classe.getConstructor(java.net.Socket.class); 
		} catch (NoSuchMethodException e) {
			// transformation du type de l'exception quand l'erreur est détectée par ce biais
			throw new ValidationException("Il faut un constructeur avec Socket");
		}
		int modifiers = c.getModifiers();
		if (!Modifier.isPublic(modifiers)) 
			throw new ValidationException("Le constructeur (Socket) doit être public");
		if (c.getExceptionTypes().length != 0)
			throw new ValidationException("Le constructeur (Socket) ne doit pas lever d'exception");
		// etc... avec tous les tests nécessaires
		
	
	}
	
	public static Class<? extends Service> getServiceClass(int numService) {
		return servicesClasses.get(numService - 1);
	}
	
// liste les activités présentes
	public static String toStringue() {
		String result = "Activités présentes :##";
		int i = 1;
		// foreach n'est qu'un raccourci d'écriture 
		// donc il faut prendre le verrou explicitement sur la collection
		synchronized (servicesClasses) { 
			for (Class<? extends Service> s : servicesClasses) {
				try {
					Method toStringue = s.getMethod("toStringue");
					String string = (String) toStringue.invoke(s);
					result = result + i + " " + string+"##";
					i++;
				} catch (Exception e) {
					e.printStackTrace(); // normalement déjà testé par validation()
				}
			}
		}
		return result;
	}
	
	public static String toStringueStop() {
		String result = "Activités arrêtées présentes :##";
		int i = 1;
		synchronized (servicesClasses) { 
			for (Class<? extends Service> s : servicesStop) {
				try {
					Method toStringue = s.getMethod("toStringue");
					String string = (String) toStringue.invoke(s);
					result = result + i + " " + string+"##";
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
