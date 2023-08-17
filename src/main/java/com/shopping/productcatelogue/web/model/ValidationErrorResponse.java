package com.shopping.productcatelogue.web.model;

import java.util.List;

public class ValidationErrorResponse {
    private int status;
    private String error;
    private List<FieldError> fieldErrors;

    public ValidationErrorResponse(int status, String error, List<FieldError> fieldErrors) {
        this.status = status;
        this.error = error;
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;

        public FieldError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }
    }
}