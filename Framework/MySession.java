package mg.itu.util;
import jakarta.servlet.http.*;

/**
 * MySession
 */
public class MySession {

    private HttpSession session;
    public MySession(HttpSession session) {
        this.session = session;
    }

    public Object get(String key) {
        return session.getAttribute(key);
    }

    public void add(String key, Object objet) {
        session.setAttribute(key, objet);
    }

    public void delete(String key) {
        session.removeAttribute(key);
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }
}