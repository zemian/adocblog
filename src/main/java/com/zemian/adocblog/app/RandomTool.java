package com.zemian.adocblog.app;

import com.zemian.adocblog.cipher.Crypto;

/**
 * Generate 5 random string, suitable for used as secret password or salt key.
 *
 * You may change the lenght of the random string by first argument.
 */
public class RandomTool {
	public static void main(String[] args) {
		int len = 16;
		if (args.length >= 1) {
			len = Integer.parseInt(args[0]);
		}
		for (int i = 0; i < 5; i++) {
			String randStr = Crypto.randString(len);
			System.out.println(randStr);
		}
	}

}
