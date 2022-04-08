package com.example.nettychat.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel arg0) throws Exception {
        // pipeline describes how we want to organize our communication
        ChannelPipeline pipeline = arg0.pipeline();
        // tell netty we expect frames of at most 8192 in size, each delimited by \n
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // decode bytes into strings and encode it to send to the server
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        // define a class which will handle the endcodings and decodings strings from the server
        pipeline.addLast("handler", new ChatClientHandler());
    }

}
