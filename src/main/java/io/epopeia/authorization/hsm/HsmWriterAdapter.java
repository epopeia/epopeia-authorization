package io.epopeia.authorization.hsm;

import io.netty.channel.Channel;

public interface HsmWriterAdapter {

	void write(Channel channel);
}
