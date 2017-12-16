package com.zemian.adocblog.web.listener;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception ex) throws Exception {
        ModelAndView ret = new ModelAndView("/error");
        // A cleaner version of the error message
        ret.addObject("errorMessage", ExceptionUtils.getRootCauseMessage(ex));
        // A full stacktrace for debug purpose
        ret.addObject("exceptionStacktrace", ExceptionUtils.getStackTrace(ex));
        return ret;
    }
}
