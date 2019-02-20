package walkover.main;

import server.HttpWalkOverServer;
import server.HttpWalkOverHandler;
import server.HttpWalkOverIntializer;
import walkover.annotations.RegisterRoute;
import walkover.interfaces.RestAPI;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class WalkOver implements RestAPI {

    private HttpWalkOverIntializer intializer;
    private HttpWalkOverHandler handler;
    private ArrayList<Object> mRegisteredRouterClases = new ArrayList<Object>();

    public static WalkOver instance = null;

    private WalkOver() {
        this.handler = new HttpWalkOverHandler(this);
        this.intializer = new HttpWalkOverIntializer(this, handler);
    }

    public static WalkOver getInstance() {
        if (instance == null) {
            instance = new WalkOver();
        }
        return instance;
    }

    public void get(HttpWalkOverHandler handler) {

        if (!mRegisteredRouterClases.isEmpty()) {

            for (Object clazz : mRegisteredRouterClases) {

                for (Method method : clazz.getClass().getMethods()) {

                    if (method.isAnnotationPresent(RegisterRoute.class)) {

                        if (method.getAnnotation(RegisterRoute.class).method().equals("GET") && method.getAnnotation(RegisterRoute.class).route().equals(handler.request.uri())) {
                            try {
                                method.invoke(clazz, null);
                                break;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            handler.send404();

        } else {
            System.out.println("No Routes Present..");
        }
    }

    public void put() {

    }

    public void delete() {

    }

    public void update() {

    }

    public void post() {

    }

    public void register(Class obj) {
        try {
            Class<?> clazz = Class.forName(obj.getCanonicalName());
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();
            for (Object objects : mRegisteredRouterClases) {
                if (obj.getCanonicalName().equals(objects.getClass().getCanonicalName())) {
                    System.out.println("Object Already Presnt" + obj.getCanonicalName());
                }
            }
            mRegisteredRouterClases.add(obj.newInstance());
        } catch (Exception e) {
            System.out.println("Exception In Register");
            e.printStackTrace();
        }
    }

    public void listen(int port) {
        try {
            HttpWalkOverServer server = new HttpWalkOverServer(intializer, this);
            server.ini();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String msg) {
        handler.writeResponse(msg);
    }
}
