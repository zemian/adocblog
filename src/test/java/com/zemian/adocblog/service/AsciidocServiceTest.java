package com.zemian.adocblog.service;

import com.zemian.adocblog.BaseSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ContextConfiguration(classes = ServiceConfig.class)
public class AsciidocServiceTest extends BaseSpringTest {

    @Autowired
    private AsciidocService asciidocService;

    @Test
    public void toHtml() {
        String ct = "Writing AsciiDoc is _easy_!";
        assertThat(asciidocService.toHtml(ct),
                is("<div class=\"paragraph\">\n<p>Writing AsciiDoc is <em>easy</em>!</p>\n</div>"));
    }
}
