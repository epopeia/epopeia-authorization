package io.epopeia.authorization.hsm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HsmClientHandler extends ChannelInboundHandlerAdapter {

	private HsmReaderAdapter hsmReaderAdapter;

	public HsmClientHandler(HsmReaderAdapter hsmReaderAdapter) {
		this.hsmReaderAdapter = hsmReaderAdapter;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(hsmReaderAdapter != null) {
			hsmReaderAdapter.read(ctx, msg);
		}
	}
}
