package server;

import common.WorkerTab;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kaso on 21/11/16.
 */

public class JCoincheServer {

    public static void main(String[] args) throws InterruptedException {
        final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
        List<Game> game = Collections.synchronizedList(new ArrayList<Game>());
        final Lobby lobby = new Lobby(game);
        final WorkerTab tabWorker = new WorkerTab();

        // Create event loop groups. One for incoming connections handling and
        // second for handling actual event by workers
        EventLoopGroup serverGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            System.out.println("Lancement du server");
            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.group(serverGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new JCoincheServerChannelInitializer(lobby, tabWorker));

            // Bind to port
            bootStrap.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            serverGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.exit(0);
        }
    }
}
