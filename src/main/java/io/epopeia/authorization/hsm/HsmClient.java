package io.epopeia.authorization.hsm;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HsmClient {

	private final String host;
	private final int port;
	private HsmWriterAdapter hsmWriterAdapter;
	private HsmReaderAdapter hsmReaderAdapter;

	public static void main(String[] args) {
		HsmClient hsmClient = new HsmClient("localhost", 8000);
		hsmClient.setHsmReaderAdapter(new HsmReaderAdapterStdout());
		hsmClient.setHsmWriterAdapter(new HsmWriterAdapterStdin());
		hsmClient.run();
	}

	public HsmReaderAdapter getHsmReaderAdapter() {
		return hsmReaderAdapter;
	}

	public void setHsmReaderAdapter(HsmReaderAdapter hsmReaderAdapter) {
		this.hsmReaderAdapter = hsmReaderAdapter;
	}

	public HsmWriterAdapter getHsmWriterAdapter() {
		return hsmWriterAdapter;
	}

	public void setHsmWriterAdapter(HsmWriterAdapter hsmWriterAdapter) {
		this.hsmWriterAdapter = hsmWriterAdapter;
	}

	public HsmClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		/**
		 * The old 3.x channel factory was replaced for this implementation that
		 * not register a channel with a IO thread anymore to let users decide
		 * where to register different channels implementations in one or more
		 * EventLoopGroup.
		 */
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(
							new HsmClientInitializer(new HsmClientHandler(
									hsmReaderAdapter)));

			Channel channel = bootstrap.connect(host, port)
					.syncUninterruptibly().channel();

			while (true) {
				if (hsmWriterAdapter != null) {
					hsmWriterAdapter.write(channel);
				}
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}
