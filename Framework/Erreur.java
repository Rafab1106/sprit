package mg.itu.erreur;

public class Erreur {
    
    String message;
    String valeur;

    
	public Erreur(String message, String valeur) {
		this.setMessage(message);
		this.setValeur(valeur);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getValeur() {
		return valeur;
	}
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
}
