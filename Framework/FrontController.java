package mg.itu.controleur;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mg.itu.framework.*;
import mg.itu.util.Mapping;
import mg.itu.util.*;
import mg.itu.annotation.*;

public class FrontController extends HttpServlet {
    private Map<String, Mapping> urlMappings;
    private boolean isScan = false;

    @Override
    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String chemin = context.getInitParameter("chemin");
            this.setMapping(chemin);
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'initialisation du FrontController", e);
        }
    }
    
    private void setMapping(String chemin)throws Exception{
        urlMappings = new HashMap<>();
        List<String> controllers = scan(chemin); // Utilisation de la méthode scan mise à jour
            for (String controller : controllers) {
                Class<?> clazz = Class.forName(controller);
                Method[] methods = clazz.getDeclaredMethods();
                boolean hasGetMethod = false;
                for (Method method : methods) {
                    // if (method.isAnnotationPresent(GET.class) && 
                    //     method.isAnnotationPresent(RestAPI.class || !method.isAnnotationPresent(GET.class) && 
                    //     method.isAnnotationPresent(RestAPI.class))) {
                        
                    // } else 
                    if (method.isAnnotationPresent(GET.class)) {
                        hasGetMethod = true;
                        GET annotation = method.getAnnotation(GET.class);
                        String url = annotation.value();
                        if (urlMappings.containsKey(url)) {
                            throw new Exception("Duplicate url ["+ url +"] dans "+ clazz.getName() + " et "+ urlMappings.get(url).getClassName());
                        }
                        urlMappings.put(url, new Mapping(clazz.getName(), method.getName(),clazz,method));

                    }
                }    
                
                if (!hasGetMethod) {
                    throw new Exception("La classe " + clazz.getName() + " n'a aucune méthode annotée avec @GET.");
                }
            }
    }
    private List<String> scan(String chemin) throws Exception {
        List<String> liste = new ArrayList<>();
        try {
            String cheminRepertoire = chemin.replace('.', '/');
            URL urPackage = Thread.currentThread().getContextClassLoader().getResource(cheminRepertoire);
            if (urPackage != null) {
                File directory = new File(urPackage.getFile());
                File[] fichiers = directory.listFiles();
                if (fichiers != null) {
                    for (File fichier : fichiers) {
                        if (fichier.isFile() && fichier.getName().endsWith(".class")) {
                            String nomClasse = fichier.getName().substring(0, fichier.getName().length() - 6);
                            String nomCompletClasse = chemin + "." + nomClasse;
                            Class<?> clazz = Class.forName(nomCompletClasse);
                            if (clazz.isAnnotationPresent(Controleur.class)) {
                                System.out.println("is annote");
                                liste.add(nomCompletClasse);
                            }
                        } else if (fichier.isDirectory()) {
                            List<String> li = scan(chemin + "." + fichier.getName());
                            liste.addAll(li);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        this.isScan = true;
        return liste;
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String requestUrl = request.getRequestURI().substring(request.getContextPath().length());
        Mapping mapping = urlMappings.get(requestUrl);
        
        if (mapping != null) {
            Class<?> class1 = Class.forName(mapping.getClassName());
            String methodName = mapping.getMethodName();
            // System.out.println(methodName);
            Object result = mapping.getReponse(request);
            Method method = mapping.getMethod();
            Object instance = class1.getDeclaredConstructor().newInstance();

            if (method.isAnnotationPresent(RestAPI.class)) {
                // Sérialiser en JSON si @RestAPI est présent
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(mapping.retour());
                out.println("Voici le retour en Json "+jsonResponse); // Envoyer la réponse JSON
                if (result instanceof String) {
                    
                    // result = (String) method.invoke(instance);    
                    out.println("<h1>URL: " + requestUrl + "</h1>");
                    out.println("<p>Class: " + mapping.getClassName() + "</p>");
                    out.println("<p>Method: " + mapping.getMethodName() + "</p>");
                    out.println("<p>Resultat: " + jsonResponse + "</p>");
    
                } else if (result instanceof ModelView) {
                    System.out.println("the return is ModelandView");
                    ModelView modelViewResult = (ModelView) mapping.retour();
                    String url = modelViewResult.getUrl();
                    HashMap<String, Object> data = modelViewResult.getMap();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                    request.getRequestDispatcher(url).forward(request, response);
                } else {
                    throw new Exception("le type de retour est non reconue");
                }
            } else {
                if (result instanceof String) {

                    // result = (String) method.invoke(instance);    
                    out.println("<h1>URL: " + requestUrl + "</h1>");
                    out.println("<p>Class: " + mapping.getClassName() + "</p>");
                    out.println("<p>Method: " + mapping.getMethodName() + "</p>");
                    out.println("<p>Resultat: " + mapping.retour() + "</p>");
    
                } else if (result instanceof ModelView) {
                    System.out.println("the return is ModelandView");
                    ModelView modelViewResult = (ModelView) result;
                    String url = modelViewResult.getUrl();
                    HashMap<String, Object> data = modelViewResult.getMap();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                    request.getRequestDispatcher(url).forward(request, response);
                } else {
                    throw new Exception("le type de retour est non reconue");
                }
            }
        } else {
            out.println("<h1>No method associated with URL: " + requestUrl + "</h1>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            processRequest(request, response);    
        } catch (Exception e) {
            // TODO: handle exception
            out.println(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            processRequest(request, response);    
        } catch (Exception e) {
            // TODO: handle exception
            out.println(e);
        }
    }
}
