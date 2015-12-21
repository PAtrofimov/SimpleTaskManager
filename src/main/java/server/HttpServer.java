package server;

import handlers.DeleteTaskHandler;
import handlers.DeleteUserHandler;
import handlers.HandlerInterface;
import handlers.InsertTaskHandler;
import handlers.InsertUserHandler;
import handlers.LoginHandler;
import handlers.SelectAllUsersHandler;
import handlers.SelectTasksHandler;
import handlers.SelectUserHandler;
import handlers.UpdateTaskHandler;
import handlers.UpdateUserHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.util.HashMap;
import java.util.Map;

public final class HttpServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
    private static Map<String, HandlerInterface> mapServerHandlers;

    public static Map<String, HandlerInterface> getMapServerHandlers() {
        return mapServerHandlers;
    }

    public static void setMapServerHandlers(Map<String, HandlerInterface> mapServerHandlers) {
        HttpServer.mapServerHandlers = mapServerHandlers;
    }
    
    public static Map<String, HandlerInterface> initMapServerHandlers() {
        Map<String, HandlerInterface> handlers = new HashMap<String, HandlerInterface>();
        handlers.put("managertask.handlers.UpdateTaskHandler", new UpdateTaskHandler());
        handlers.put("managertask.handlers.InsertTaskHandler", new InsertTaskHandler());
        handlers.put("managertask.handlers.SelectTasksHandler", new SelectTasksHandler());
        handlers.put("managertask.handlers.DeleteTaskHandler", new DeleteTaskHandler());
        handlers.put("managertask.handlers.LoginHandler", new LoginHandler());
        handlers.put("managertask.handlers.InsertUserHandler", new InsertUserHandler());
        handlers.put("managertask.handlers.UpdateUserHandler", new UpdateUserHandler());
        handlers.put("managertask.handlers.SelectUserHandler", new SelectUserHandler());
        handlers.put("managertask.handlers.DeleteUserHandler", new DeleteUserHandler());
        handlers.put("managertask.handlers.SelectAllUsersHandler", new SelectAllUsersHandler());
        return handlers;
    }
           
    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
                .sslProvider(SslProvider.JDK).build();
        } else {
            sslCtx = null;
        }
        HttpServer.setMapServerHandlers(initMapServerHandlers());
        
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new HttpServerInitializer(sslCtx));

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("Open your web browser and navigate to " +
                    (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
