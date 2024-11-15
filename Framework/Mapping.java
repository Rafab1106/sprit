package mg.itu.util;

import jakarta.servlet.http.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.annotation.Param;
import mg.itu.annotation.Required;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;

public class Mapping {
    private String className;
    private String methodName;
    private Class classe;
    private Method methode;

    public Mapping(String className, String methodName, Class classe, Method method) {
        this.className = className;
        this.methodName = methodName;
        this.classe = classe;
        this.methode = method;
    }
    public Mapping(){
        
    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }
    public Method getMethodByName(Class<?> cls, String methodName) throws Exception {
        // Obtenir toutes les méthodes de la classe
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            // Vérifier si le nom de la méthode correspond au nom donné
            if (method.getName().equals(methodName)) {
                return method;

            }
        }
        Exception e = new Exception("Tsisy methode");
        throw e;
        // Retourner null si aucune méthode correspondante n'est trouvée

    }
    public String retour() {
        String response = "";
        try {
            Object instance = this.classe.getDeclaredConstructor().newInstance();
            response = this.methode.invoke(instance).toString();
        } catch (Exception e) {
            response = "Tsy mety";
        }
        return response;
    }
    public ArrayList<String> getDeclareParameters(HttpServletRequest request) throws Exception {
        Enumeration<String> parameterNames = request.getParameterNames();
        ArrayList<String> response = new ArrayList<String>();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            response.add(name);
        }
        return response;
    }
    public boolean isPrimitiveOrString(Class<?> paramType) {
        return paramType.isPrimitive() || paramType.equals(String.class);
    }
    public static Object setToObject(Class<?> clazz, String value) throws Exception {
        if (value == null) {
            if (clazz == int.class || clazz == Integer.class) {
                return 0; // Valeur par défaut pour int
            }
            // Gestion d'autres types par défaut ici si nécessaire
        }
        try {
            if (clazz == String.class) {
                return value;
            } else if (clazz == int.class || clazz == Integer.class) {
                return Integer.parseInt(value);
            } else if (clazz == boolean.class || clazz == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (clazz == double.class || clazz == Double.class) {
                return Double.parseDouble(value);
            } else if (clazz == long.class || clazz == Long.class) {
                return Long.parseLong(value);
            } else if (clazz == float.class || clazz == Float.class) {
                return Float.parseFloat(value);
            } else if (clazz == short.class || clazz == Short.class) {
                return Short.parseShort(value);
            } else if (clazz == byte.class || clazz == Byte.class) {
                return Byte.parseByte(value);
            }
            // Ajouter d'autres types si nécessaire

            // Si le type n'est pas géré, lever une exception
            throw new IllegalArgumentException("Cannot convert String to " + clazz.getName());
        } catch (Exception e) {

            Object averina = clazz.getConstructor().newInstance();
            return averina;
        }
        
    }
    public void validerAttribute(Object object) throws Exception{
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Required.class)) {
                if (field.get(object) == null) {
                    throw new Exception("la valeur de "+field.getName()+ " ne doit pas etre null");
                }
            } else if (field.isAnnotationPresent(Min.class)) {
                Min min = field.getAnnotation(Min.class);
                if (field.get(object) < min.value()) {
                    throw new Exception("la valeur de "+field.getName()+ " ne doit pas etre en dessous de "+min.value());
                }
            }
        }
    }
    public Object getReponse(HttpServletRequest request) throws Exception {
        Parameter[] parameters = methode.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(Param.class)) {
                if (isPrimitiveOrString(parameters[i].getType())) {
                    args[i] = request.getParameter(parameters[i].getName());
                    Param param = parameters[i].getAnnotation(Param.class);
                    String paramName = param.name();
                    String paramValue = request.getParameter(paramName);
                    args[i] = setToObject(parameters[i].getType(), paramValue);
                } else if (parameters[i].getType() == MySession.class) {
                    HttpSession httpSession = request.getSession();
                    MySession mySession = new MySession(httpSession);
                    args[i] = mySession;
                } else {
                    ArrayList<String> listeParametre = getDeclareParameters(request);
                    String nomParametre = parameters[i].getName();
                    Param param = parameters[i].getAnnotation(Param.class);
                    nomParametre = param.name();
                    Class cl = parameters[i].getType();
                    // Employer e=new Employer();
                    Object object = cl.getConstructor().newInstance();
                    validerAttribute(object);// validation des attributs des objets
                    for (String a : listeParametre) {
                        String[] sep = a.split("\\.");
                        if (sep.length > 1) {
                            if (sep[0].equalsIgnoreCase(nomParametre)) {
                                String maj = sep[1].substring(0, 1).toUpperCase() + sep[1].substring(1);
                                Method m = this.getMethodByName(cl, "set" + maj);
                                Parameter[] par = m.getParameters();
                                m.invoke(object, setToObject(par[0].getType(), request.getParameter(a)));
                            }
                        }
                    }
                    args[i] = object;
                }    
            } else {
                throw new Exception("ETU002413 , il y a une parametre non annote");
            }
            
        }
        return methode.invoke(classe.getDeclaredConstructor().newInstance(), args);
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public Class getClasse() {
        return classe;
    }
    public void setClasse(Class classe) {
        this.classe = classe;
    }
    public Method getMethode() {
        return methode;
    }
    public void setMethode(Method methode) {
        this.methode = methode;
    }
}