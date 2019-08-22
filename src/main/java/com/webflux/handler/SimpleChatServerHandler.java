/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webflux.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


import com.webflux.ChannelRepository;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * event handler to process receiving messages
 *
 * @author Jibeom Jung
 */
@Component
@ChannelHandler.Sharable
public class SimpleChatServerHandler extends ChannelInboundHandlerAdapter {
    private final static Logger log = LoggerFactory.getLogger(SimpleChatServerHandler.class);


    private final ChannelRepository channelRepository;

    public SimpleChatServerHandler(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");

        ctx.fireChannelActive();

        String channelKey = ctx.channel().remoteAddress().toString();
        channelRepository.put(channelKey, ctx.channel());

        ctx.writeAndFlush("Your channel key is " + channelKey + "\r\n");
        log.debug("Channel : " + ctx.channel().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String stringMessage = (String) msg;
        String[] splitMessage = stringMessage.split("::");

        if (splitMessage.length != 2) {
            ctx.channel().writeAndFlush(stringMessage + "\n\r");
            return;
        }

        if (channelRepository.get(splitMessage[0]) != null) {
            channelRepository.get(splitMessage[0]).writeAndFlush(splitMessage[1] + "\n\r");
        }
        log.debug("Message : " + (String) msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
        Assert.notNull(ctx, "[Assertion failed] - ChannelHandlerContext is required; it must not be null");

        String channelKey = ctx.channel().remoteAddress().toString();
        this.channelRepository.remove(channelKey);

    }
}
