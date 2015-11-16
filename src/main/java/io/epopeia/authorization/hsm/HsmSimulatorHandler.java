package io.epopeia.authorization.hsm;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class HsmSimulatorHandler extends ChannelInboundHandlerAdapter {

	private static final ChannelGroup channels = new DefaultChannelGroup(
			ImmediateEventExecutor.INSTANCE);

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.write("[SERVER] - " + incoming.remoteAddress()
					+ "has joined\n");
		}
		channels.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.write("[SERVER] - " + incoming.remoteAddress()
					+ "has left\n");
		}
		channels.remove(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			if (channel != incoming) {
				channel.write("[" + incoming.remoteAddress() + "] " + msg
						+ "\n");
			}
		}
	}
}
