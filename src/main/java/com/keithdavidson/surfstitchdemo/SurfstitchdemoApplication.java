package com.keithdavidson.surfstitchdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SurfstitchdemoApplication {
	private static final Logger log = LoggerFactory.getLogger(SurfstitchdemoApplication.class);

	public static void main(String args[]) {
		try {
			SpringApplication.run(SurfstitchdemoApplication.class, args);
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
