package io.epopeia.authorization;

import java.util.Arrays;

public class Main {

	public static final String[] s = { "epopeia", "-", "authorization" };

	public static void main(String[] args) {
		Arrays.stream(s).forEach(System.out::print);
	}
}
