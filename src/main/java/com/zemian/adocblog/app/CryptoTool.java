package com.zemian.adocblog.app;

import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.cipher.Crypto;
import com.zemian.adocblog.cipher.CryptoConfig;
import com.zemian.adocblog.support.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

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
		@Bean
		public CryptoTool cryptoTool() {
			return new CryptoTool();
		}
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext(CryptoTool.Config.class);
		CryptoTool main = spring.getBean(CryptoTool.class);
		main.run(args);
	}


	@Autowired
	private Environment env;

	@Autowired
	private Crypto crypto;

	public void run(String[] args) {
		if (args.length < 1) {
			System.out.println("ERROR: You need to provide a plain text argument.");
			return;
		}

		String appEnv = env.getProperty("app.env");
		System.out.println("Encrypting with appEnv=" + appEnv);

		String input = args[0];
		System.out.println("Encryption for: " + input);
		System.out.println(crypto.encrypt(input));

	}

}
