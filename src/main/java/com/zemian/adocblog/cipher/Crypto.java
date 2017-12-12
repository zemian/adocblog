package com.zemian.adocblog.cipher;

import com.zemian.adocblog.AppException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * A simple service to encrypt/decrypt data using "javax.crypto.Cipher" with a configurable algorithm. It requires
 * minimum of an algorithm (transformation) in format of "algorithm/mode/padding" string and a secret Key. Some mode
 * such as "CBC" also requires an extra IV parameter. Both key and IV parameters can be randomize by user as input.
 * Note also that depending on which algorithm used, you must provide correct number of bytes as Key and IV parameters.
 *
 * For complete list of algorithm you can use, see the reference links below.
 *
 * Example:
 *   // We will use String to Byte as example for key and IV parameter here, but you can use any random byte values
 *   // as input. You should keep these secret from your application since they act as the "key" to encryp/decrypt
 *   // sensitive data. One way would be to store it externally from the application and read it in at runtime.
 *
 *   Crypto crypto = new Crypto("AES/CBC/PKCS5Padding", toBytes("1234567890123456"), toBytes("1234567890123456"));
 *   String secret = crypto.encrypt("TEST");
 *   String testData = crypto.decrypt(secret);
 *
 * References:
 * * https://docs.oracle.com/javase/8/docs/api/javax/crypto/Cipher.html
 * * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public class Crypto {
    public static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String DES_ALGORITHM = "DESede/CBC/PKCS5Padding";
    public static final String AES_ECB_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String DES_ECB_ALGORITHM = "DESede/ECB/PKCS5Padding";

    private String algorithm;
    private SecretKey secretKey;
    private IvParameterSpec ivParam;

    public Crypto(String algorithm, byte[] keyBytes, byte[] ivBytes) {
        String algoName = algorithm;
        int sepIdx = algorithm.indexOf("/");
        if (sepIdx > 0) {
            algoName = algorithm.substring(0, sepIdx);
        }
        IvParameterSpec ivParam = (ivBytes == null) ? null : new IvParameterSpec(ivBytes);

        this.algorithm = algorithm;
        this.secretKey = new SecretKeySpec(keyBytes, algoName);
        this.ivParam = ivParam;
    }
    public Crypto(String algorithm, byte[] keyBytes) {
        this(algorithm, keyBytes, null);
    }

    public Crypto(String algorithm, SecretKey secretKey, IvParameterSpec ivParam) {
        this.algorithm = algorithm;
        this.secretKey = secretKey;
        this.ivParam = ivParam;
    }
    public Crypto(String algorithm, SecretKey secretKey) {
        this(algorithm, secretKey, null);
    }

    public String encrypt(String plain) {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(toBytes(plain));
            ByteArrayOutputStream ouptput = new ByteArrayOutputStream();
            Cipher cipher = Cipher.getInstance(algorithm);
            if (ivParam != null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }
            int blockSize = cipher.getBlockSize();
            byte[] buff = new byte[blockSize];
            int len;
            while ((len = input.read(buff, 0, blockSize)) != -1) {
                ouptput.write(cipher.update(buff, 0, len));
            }
            ouptput.write(cipher.doFinal());
            String result = Base64.getEncoder().encodeToString(ouptput.toByteArray());
            return result;
        } catch (Exception e) {
            throw new AppException("Failed to encrypt data.", e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(toBytes(encrypted));
            ByteArrayInputStream input = new ByteArrayInputStream(encryptedBytes);
            ByteArrayOutputStream ouptput = new ByteArrayOutputStream();
            Cipher cipher = Cipher.getInstance(algorithm);
            if (ivParam != null) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            }
            int blockSize = cipher.getBlockSize();
            byte[] buff = new byte[blockSize];
            int len;
            while ((len = input.read(buff, 0, blockSize)) != -1) {
                ouptput.write(cipher.update(buff, 0, len));
            }
            ouptput.write(cipher.doFinal());
            String result = new String(ouptput.toByteArray(), StandardCharsets.UTF_8);
            return result;
        } catch (Exception e) {
            throw new AppException("Failed to encrypt data.", e);
        }
    }

    // == Support Utilities

    /**
     * Randomly generate a string id.
     */
    public static final String ALPAH_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    public static final String ALPAH_NUM_SYM = ALPAH_NUM + "!@#$%^&*()";
    public static String randString(int len) {
        return randString(len, ALPAH_NUM_SYM);
    }
    public static String randString(int len, String source) {
        Random rand = new Random();
        int poolSize = ALPAH_NUM_SYM.length();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = ALPAH_NUM_SYM.charAt(rand.nextInt(poolSize));
            result.append(c);
        }
        return result.toString();
    }

    public static byte[] toBytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }
}
