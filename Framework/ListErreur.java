package mg.itu.erreur;

import java.util.HashMap;

public class ListErreur {
    HashMap<String,Erreur> lists;

	public Erreur getLists(String name) {
        Erreur e = this.lists.get(name);
        if (e.getMessage()!=null) {
            return e;
        } 
        return null;
	}

	public void addLists(String name,Erreur e) {
        HashMap<String, Erreur> lists = new HashMap<>();
        lists.put(name, e);
		this.lists = lists;
	}

	public void setLists(HashMap<String, Erreur> lists) {
		this.lists = lists;
	}
}
