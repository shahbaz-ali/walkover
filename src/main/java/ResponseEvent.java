import io.netty.channel.ChannelHandlerContext;

public class ResponseEvent {
    private ChannelHandlerContext context;
    private Object message;

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
