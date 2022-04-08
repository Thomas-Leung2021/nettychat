package com.example.nettychat.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {

    public static void main(String[] args) {
        new ChatServer(8080).run();
    }

    private final int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void run() {
        // accet incoming connections, and pass them to the worker group
        EventLoopGroup bassGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bassGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitializer());

            // listen for incoming connections
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // clean up event loop group
            bassGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
