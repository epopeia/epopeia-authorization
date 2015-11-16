package io.epopeia.authorization;

import java.util.Arrays;

//@SpringBootApplication
public class Main {

	public static final String[] s = { "epopeia", "-", "authorization" };

	public static void main(String[] args) {
		//SpringApplication.run(Application.class, args);
		Arrays.stream(s).forEach(System.out::print);
	}
}
