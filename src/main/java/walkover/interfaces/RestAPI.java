package walkover.interfaces;

import server.HttpWalkOverHandler;

public interface RestAPI {
    public void get(HttpWalkOverHandler handler);
    public void put();
    public void delete();
    public void update();
    public void post();
}
