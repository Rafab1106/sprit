package mg.itu.framework;

import java.util.HashMap;

/**
 * ModelView
 */
public class ModelView {
    String url;
    HashMap<String,Object> map;

    public ModelView(String url) {
        this.url = url;
    }

    public void AddObject(String nameV,Object value)throws Exception{
        HashMap<String,Object> map = new HashMap<>();
        if (!nameV.isEmpty() && value != null) {
            map.put(nameV, value);
            this.setMap(map);
        }
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public HashMap<String,Object> getMap() {
        return map;
    }
    public void setMap(HashMap<String,Object> map) {
        this.map = map;
    }
    
}