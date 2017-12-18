package com.zemian.adocblog.data;

import com.zemian.adocblog.SpringTestBase;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ContextConfiguration(classes = DataConfig.class)
public class DataConfigTest extends SpringTestBase {
    @Autowired
    private JdbcTemplate jdbc;

    @Test
    public void testConn() {
        jdbc.execute((ConnectionCallback<Object>) conn -> {
            assertThat(conn, notNullValue());
            return null;
        });

        Integer ret = jdbc.queryForObject("SELECT 1 + 1", Integer.class);
        assertThat(ret, is(2));
    }
}
