package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import walkover.interfaces.RestAPI;

public class HttpWalkOverIntializer extends ChannelInitializer<SocketChannel> {

    private RestAPI ex;
    private HttpWalkOverHandler handler;

    public HttpWalkOverIntializer(RestAPI app, HttpWalkOverHandler handler){
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
