package com.zemian.adocblog.web.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base controller that support web view.
 *
 * @Author Zemian Deng 2017
 */
public abstract class AbstractController {

    public static Map<String, Object> toMap(Object ... pairs) {
        Map<String, Object> ret = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            ret.put(pairs[i].toString(), pairs[i + 1]);
        }
        return ret;
    }

    protected ModelAndView getView(String viewName, Map<String, Object> attrs) {
        ModelAndView ret = new ModelAndView(viewName);
        ret.addAllObjects(attrs);
        return ret;

    }

    protected ModelAndView getView(String viewName, Object ... attrPairs) {
        return getView(viewName, toMap(attrPairs));
    }

    protected ModelAndView getErrorView(String viewName, BindingResult bindingResult, Object ... attrPairs) {
        Map<String, Object> attrs = toMap(attrPairs);
        List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
        attrs.put("errors", errors);
        return getView(viewName, attrs);
    }
}
