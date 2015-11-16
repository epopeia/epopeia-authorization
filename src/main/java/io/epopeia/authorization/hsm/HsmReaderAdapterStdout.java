package io.epopeia.authorization.hsm;

import io.netty.channel.ChannelHandlerContext;

public class HsmReaderAdapterStdout implements HsmReaderAdapter {

	@Override
	public void read(ChannelHandlerContext ctx, Object msg) {
		System.out.println(msg);
	}
}
