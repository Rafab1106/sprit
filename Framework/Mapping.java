package mg.itu.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.annotation.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;

public class Mapping {
<<<<<<< HEAD
    private String className;
    private String methodName;
    private Class classe;
    private Method methode;

    public Mapping(String className, String methodName, Class classe, Method method) {
        this.className = className;
        this.methodName = methodName;
        this.classe = classe;
        this.methode = method;
=======
    String className;
    String methodName;
    Parameter[] parameters;

    public Mapping(String className, String methodName, Parameter[] parameters) {
        setClassName(className);
        setParameters(parameters);
        setMethodName(methodName);
    }

    public Mapping(String className, String methodName) {
        setClassName(className);
        setMethodName(methodName);
    }

    public Mapping() {

    }

    public Object getResponse(HttpServletRequest request) throws Exception {
        Class<?> class1 = Class.forName(this.getClassName());
        Object instance = class1.getConstructor().newInstance();
        Method method = class1.getMethod(methodName, getParameterTypes());
        
        Object[] params = new Object[method.getParameterCount()];
        Enumeration<String> values = request.getParameterNames();

        while (values.hasMoreElements()) {
            String name = values.nextElement();

            for (int i = 0; i < parameters.length; i++) {
                if(parameters[i].getName().equals(name)) {
                    params[i] = request.getParameter(name);
                }
            }

            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(Param.class)) {
                    String val = parameters[i].getAnnotation(Param.class).value();
                    if(val.equals(name)) {
                        params[i] = request.getParameter(name);
                    }
                }
            }
        }

        return method.invoke(instance, params);
    }

    private Class<?>[] getParameterTypes() {
        Class<?>[] types = new Class[parameters.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = parameters[i].getType();
        }
        return types;
>>>>>>> cfa3b8943a9e7c23e37e267a8d1de8ccf4e3b04f
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
<<<<<<< HEAD
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
    public Object getReponse(HttpServletRequest request) throws Exception {
        Parameter[] parameters = methode.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {

            if (isPrimitiveOrString(parameters[i].getType())) {
                args[i] = request.getParameter(parameters[i].getName());
                if (parameters[i].isAnnotationPresent(Param.class)) {
                    Param param = parameters[i].getAnnotation(Param.class);
                    String paramName = param.value();
                    String paramValue = request.getParameter(paramName);

                    // For simplicity, assume all parameters are of type String
                    args[i] = setToObject(parameters[i].getType(), paramValue);
                }
            } else {
                ArrayList<String> listeParametre = getDeclareParameters(request);
                String nomParametre = parameters[i].getName();
                if (parameters[i].isAnnotationPresent(Param.class)) {
                    Param param = parameters[i].getAnnotation(Param.class);
                    nomParametre = param.value();

                }
                Class cl = parameters[i].getType();
                // Employer e=new Employer();
                Object object = cl.getConstructor().newInstance();
                Object p[] = new Object[1];

                for (String a : listeParametre) {
                    String[] sep = a.split("\\.");
                    if (sep.length > 1) {
                        if (sep[0].equalsIgnoreCase(nomParametre)) {
                            String maj = sep[1].substring(0, 1).toUpperCase() + sep[1].substring(1);
                            Method m = getMethodByName(cl, "set" + maj);
                            Parameter[] par = m.getParameters();
                            m.invoke(object, setToObject(par[0].getType(), request.getParameter(a)));
                        }
                    }
                }
                args[i] = object;
            }
        }
        return methode.invoke(classe.getDeclaredConstructor().newInstance(), args);

    }
}
=======

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }    
}
>>>>>>> cfa3b8943a9e7c23e37e267a8d1de8ccf4e3b04f
