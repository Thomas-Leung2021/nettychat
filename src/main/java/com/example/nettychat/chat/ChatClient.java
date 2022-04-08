package com.example.nettychat.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {

    public static void main(String[] args) {
        new ChatClient("localhost", 8080).run();
    }

    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // Use bootstrap to setup a channel using EventLoopGroup
            // the channel is handled by the ChatClientInitializer that we created
            Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());

            // Tell bootstrap to create a connection to the server, when connected we
            // will ask for the channel object to get interaction with the server
            Channel channel = bootstrap.connect(host, port).sync().channel();

            // Capture user's input from the consult
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            // takes user's input and writes it to the server
            while (true) {
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            // shut down event loop group
            group.shutdownGracefully();
        }
    }
}
