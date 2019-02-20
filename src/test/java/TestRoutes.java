import walkover.annotations.RegisterRoute;
import walkover.main.WalkOver;

public class TestRoutes {
    public TestRoutes(){

    }


    @RegisterRoute(method = "GET",route = "/shahbaz")
    public void testroutesapi(){
        WalkOver.getInstance().write("{\"success\":true}");
    }
}
