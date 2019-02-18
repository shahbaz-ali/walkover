import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpSnoopServerIntializer extends ChannelInitializer<SocketChannel> {

    private RestAPI ex;
    private HttpSnoopServerHandler handler;

    public HttpSnoopServerIntializer(RestAPI app, HttpSnoopServerHandler handler){
        this.ex = app;
        this.handler = handler;
    }
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(handler);
    }
}
