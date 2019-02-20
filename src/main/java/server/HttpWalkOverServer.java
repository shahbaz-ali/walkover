package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import walkover.interfaces.RestAPI;

public final class HttpWalkOverServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int port = Integer.parseInt(System.getProperty("port",SSL?"8443":"8080"));
    private RestAPI app;
    private HttpWalkOverIntializer intializer;
    public HttpWalkOverServer(HttpWalkOverIntializer intializer, RestAPI app){
        this.app = app;
        this.intializer = intializer;
    }

    public  void ini() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(intializer);
            Channel ch = b.bind(port).sync().channel();
            System.err.println("Open your web browser and navigate to " +
                    (SSL? "https" : "http") + "://127.0.0.1:" + port + '/');

            ch.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
