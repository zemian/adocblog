package com.zemian.adocblog.web.controller.admin;

import com.zemian.adocblog.service.AsciidocService;
import com.zemian.adocblog.service.ContentService;
import com.zemian.adocblog.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Base class to support Doc related processing.
 */
public abstract class AbstractDocController {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocController.class);

    @Value("${app.web.defaultPagingSize}")
    protected int defaultPagingSize;

    @Autowired
    protected DocService docService;

    @Autowired
    protected ContentService contentService;

    @Autowired
    protected AsciidocService asciidocService;

}
