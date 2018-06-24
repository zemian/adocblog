package com.zemian.adocblog.app;

import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.cipher.Crypto;
import com.zemian.adocblog.cipher.CryptoConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Encrypt a given plain text and print out it out.
 *
 * You may change to use different cipher key and salt by using different env. Example:
 *
 *   java -Dadocblog.env=qa com.zemian.adocblog.app.CryptoTool
 */
public class CryptoTool {
	@Configuration
	@Import({CryptoConfig.class, CommonConfig.class})
	public static class Config {
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("ERROR: You need to provide a plain text argument.");
			return;
		}

		AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext(CryptoTool.Config.class);
		Crypto crypto = spring.getBean(Crypto.class);
		System.out.println(crypto.encrypt(args[0]));
	}
}
