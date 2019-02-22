package server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import walkover.interfaces.RestAPI;

import java.util.Set;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.BAD_REQUEST;

@ChannelHandler.Sharable
public class HttpWalkOverHandler extends SimpleChannelInboundHandler<Object> {
    public HttpRequest request;
    private final StringBuilder buf = new StringBuilder();
    private RestAPI app;
    public ChannelHandlerContext context;
    public Object message;


    public HttpWalkOverHandler(RestAPI app){
        this.app = app;
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;
            if (HttpHeaderUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            this.context = ctx;
            this.message = msg;
            if (request.method().toString().equals("GET")){
                this.app.get(this);
            }else if(request.method().toString().equals("POST")){
                this.app.post(this);
            }else if(request.method().toString().equals("PUT")){
                this.app.put(this);
            }else if(request.method().toString().equals("UPDATE")){
                this.app.update(this);
            }else if (request.method().toString().equals("DELETE")){
                this.app.delete(this);
            }else {
                FullHttpResponse error = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                error.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-");
                ctx.write(error);
            }
        }

    }

    private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
        DecoderResult result = o.decoderResult();
        if (result.isSuccess()) {
            return;
        }

        buf.append(".. WITH DECODER FAILURE: ");
        buf.append(result.cause());
        buf.append("\r\n");
    }


    public void send404(){
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, NOT_FOUND,
                Unpooled.copiedBuffer("404 NOT FOUND", CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text; charset=UTF-");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w.org/Protocols/HTTP/./draft-ietf-http-v-spec-.html#Connection
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // Encode the cookie.
        String cookieString = request.headers().getAndConvert(HttpHeaderNames.COOKIE);
        if (cookieString != null) {
            Set<Cookie> cookies = ServerCookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                for (Cookie cookie : cookies) {
                    response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode(cookie));
                }
            }
        } else {
            // Browser sent no cookie.  Add some.
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode("key", "value"));
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode("key", "value"));
        }

        // Write the response.
        context.write(response);
    }

    public boolean writeResponse(String m) {
        // Decide whether to close the connection or not.
       boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, ((HttpObject)message).decoderResult().isSuccess() ? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(m, CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w.org/Protocols/HTTP/./draft-ietf-http-v-spec-.html#Connection
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // Encode the cookie.
        String cookieString = request.headers().getAndConvert(HttpHeaderNames.COOKIE);
        if (cookieString != null) {
            Set<Cookie> cookies = ServerCookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                for (Cookie cookie : cookies) {
                    response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode(cookie));
                }
            }
        } else {
            // Browser sent no cookie.  Add some.
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode("key", "value"));
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode("key", "value"));
        }

        // Write the response.
        context.write(response);

        return keepAlive;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
