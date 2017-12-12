package com.zemian.adocblog.cipher;

import com.zemian.adocblog.BaseSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ContextConfiguration(classes = CryptoConfig.class)
public class CryptoTest extends BaseSpringTest {
    @Autowired
    private Crypto crypto;

    @Test
    public void generateIDs() {
        assertThat(Crypto.randString(16).length(), is(16));
        assertThat(Crypto.randString(16).length(), is(16));
        assertThat(Crypto.randString(16).length(), is(16));
    }

    @Test
    public void encrypt() {
        String secret = crypto.encrypt("test");
        assertThat(crypto.decrypt(secret), is("test"));
    }
}
