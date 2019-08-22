package com.webflux.config;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.webflux.ChannelRepository;
import com.webflux.handler.SimpleChatChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Configuration
@EnableConfigurationProperties(NettyProperties.class)
public class NettyConfiguration {

	private final NettyProperties nettyProperties;

	public NettyConfiguration(NettyProperties nettyProperties) {
		this.nettyProperties = nettyProperties;
	}

	@Bean(name = "nettyBootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup(), workerGroup());
		b.channel(NioServerSocketChannel.class);
		b.childHandler(somethingChannelInitializer);

		b.option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog());
		return b;
	}

	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(nettyProperties.getBossCount());
	}

	@Bean(destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(nettyProperties.getWorkerCount());
	}

	@Bean
	public InetSocketAddress tcpSocketAddress() {
		return new InetSocketAddress(nettyProperties.getTcpPort());
	}

	@Bean
	public ChannelRepository channelRepository() {
		return new ChannelRepository();
	}

	@Autowired
	private SimpleChatChannelInitializer somethingChannelInitializer;

}
