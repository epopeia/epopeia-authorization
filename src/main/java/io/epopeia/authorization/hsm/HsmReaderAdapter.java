package io.epopeia.authorization.hsm;

import io.netty.channel.ChannelHandlerContext;

public interface HsmReaderAdapter {

	void read(ChannelHandlerContext ctx, Object msg);
}
