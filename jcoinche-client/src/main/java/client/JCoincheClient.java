package client;

/**
 * Created by kaso on 21/11/16.
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import protomsg.CoincheMsg;

public class JCoincheClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new JCoincheClientInitializer());

            // Create connection

            Channel c = null;
            try {
                c = bootstrap.connect(HOST, PORT).sync().channel();
            }
            catch (Exception e)
            {
                System.out.println("There is no server");
                System.exit(84);
            }

            // Get handle to handler so we can send message
            final JCoincheClientHandler handle = c.pipeline().get(JCoincheClientHandler.class);
            handle.handleRequest();
            c.close();

        } finally {
            group.shutdownGracefully();
            System.exit(0);
        }

    }
}
