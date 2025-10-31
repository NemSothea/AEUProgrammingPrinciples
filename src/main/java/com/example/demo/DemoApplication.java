package com.example.demo;


public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Hello, World!");
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println("Available processors: " + availableProcessors);
	}

}
