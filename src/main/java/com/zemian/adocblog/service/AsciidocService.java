package com.zemian.adocblog.service;

import com.zemian.adocblog.AppException;
import com.zemian.adocblog.data.domain.Content;
import org.asciidoctor.Asciidoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
public class AsciidocService {
    @Autowired
    private Asciidoctor adoc;

    private Map<String, Object> options = new HashMap<>();

    public String toHtml(String contentText) {
        return adoc.convert(contentText, options);
    }

    public void render(String contentText, Writer outputWriter) {
        try {
            adoc.render(new StringReader(contentText), outputWriter, options);
        } catch (IOException e) {
            throw new AppException("Failed to toHtml content text into asciidoc format.", e);
        }
    }
}
