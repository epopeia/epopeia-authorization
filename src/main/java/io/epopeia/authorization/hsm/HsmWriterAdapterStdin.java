package io.epopeia.authorization.hsm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.channel.Channel;

public class HsmWriterAdapterStdin implements HsmWriterAdapter {

	private BufferedReader in;

	public HsmWriterAdapterStdin() {
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public void write(Channel channel) {
		try {
			channel.writeAndFlush(in.readLine() + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
