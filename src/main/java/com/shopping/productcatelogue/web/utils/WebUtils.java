package com.shopping.productcatelogue.web.utils;

import java.util.Locale;
import java.util.function.Supplier;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class WebUtils {
    private final MessageSource messageSource;

    public String getMessage(String code) {
        return getMessage(code, null, code);
    }

    public String getMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    public ResponseStatusException notValid(String code) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessage(code));
    }

    public ResponseStatusException notFound(String code) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessage(code));
    }

    public ResponseStatusException notFound(String code, @Nullable Object[] args) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessage(code, args));
    }

    public Supplier<ResponseStatusException> notValidException(String code) {
        return () -> notValid(code);
    }

    public Supplier<ResponseStatusException> notFoundException(String code) {
        return () -> notFound(code);
    }

    public Supplier<ResponseStatusException> notFoundException(String code, @Nullable Object[] args) {
        return () -> notFound(code, args);
    }
}
