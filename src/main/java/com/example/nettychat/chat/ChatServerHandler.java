package com.example.nettychat.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " joined\n");
        }
        channels.add(ctx.channel());
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " joined");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has left!\n");
        }
        channels.remove(ctx.channel());
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " has left!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // channel will let us know who sends the object
        Channel incoming = ctx.channel();
        // send message to all clients, except the incoming channel
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "] " + msg + "\n");
            }
        }
        incoming.disconnect(); // server disconnect client connection after receiving one message from client
        // channels.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n",
        // ChannelMatcher);
        System.out.println("[" + incoming.remoteAddress() + "] " + msg + "\n");
    }

}
