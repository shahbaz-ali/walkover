package walkover.interfaces;

import server.HttpWalkOverHandler;

public interface RestAPI {
    public void get(HttpWalkOverHandler handler);
    public void put(HttpWalkOverHandler handler);
    public void delete(HttpWalkOverHandler handler);
    public void update(HttpWalkOverHandler handler);
    public void post(HttpWalkOverHandler handler);
}
