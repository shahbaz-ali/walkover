import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WalkOver implements RestAPI {
    private App registeredClass;
    private HttpSnoopServerIntializer intializer;
    private HttpSnoopServerHandler handler;

    public static WalkOver instance = null;

    private WalkOver(App registeredClass) {
        this.registeredClass = registeredClass;
        this.handler = new HttpSnoopServerHandler(this);
        this.intializer = new HttpSnoopServerIntializer(this,handler);
    }

    public static WalkOver getInstance(App registeredClass) {
        if (instance==null){
            instance = new WalkOver(registeredClass);
        }
        return instance;
    }

    public void get(HttpSnoopServerHandler handler) {

       App clazz = new App();
        for (Method method : clazz.getClass().getMethods()) {
            if(method.getAnnotation(RegisterRoute.class)!=null) {
                //System.out.print(method.getAnnotation(RegisterRoute.class));
                if (method.getAnnotation(RegisterRoute.class).route().equals(handler.request.uri())){
                    try {
                        method.invoke(clazz,null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
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

    public void listen(int port){
        try {
            HttpSnoopServer server = new HttpSnoopServer(intializer,this);
            server.main();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void write(String msg){
        handler.writeResponse(msg);
    }
}
