# walkover
WalkOver is a library to create simple and light weight web services in Android and Java

# WalkOver in 3 steps
Define a component:

    public class App {
    
        public App(){ 
          //Keep empty constructor defined
        }
    /* Additional fields if needed */ 
    }
Prepare Routes : Declare and annotate your route method, with the HTTP method and route uri

    @RegisterRoute(method="GET", route="/api/display")  
    public void httpGetDisplay() {
      /* Do something */
      
      ...
      //when you are done
      //return the reponse using
      WalkOver.getInstance().write("{\"success\":true,\"message\": \"Your First Api\"}");
    };

Register and unregister your routes. For example on Android, activities and fragments should usually register according to their life cycle:

    @Override
    public void onStart() {
      super.onStart();
      WalkOver.getInstance().register(MyRoute.class);
        //OR
        //you can also pass 'this' as argument
     
      WalkOver.getInstance().register(this);
    }

    @Override
    public void onStop() {
      super.onStop();
      EventBus.getDefault().unregister(this);
    }
Read the full getting started guide.

Add WalkOver to your project


Via Gradle:

    implementation 'io.walkover:walkover:1.0.0'

Via Maven:

    <dependency>
        <groupId>io.walkover</groupId>
        <artifactId>walkover</artifactId>
        <version>1.0.0</version>
    </dependency>
    
Or download the latest JAR from Maven Central.
