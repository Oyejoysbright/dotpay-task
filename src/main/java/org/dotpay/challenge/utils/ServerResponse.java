package org.dotpay.challenge.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class ServerResponse {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(Include.NON_NULL)
    public static class ResponseMessage<T> {
        private Boolean hasError;
        private String message;
        private int statusCode;
        private T data;

        public ResponseMessage(Boolean hasError, String message, int statusCode) {
            this.hasError = hasError;
            this.message = message;
            this.statusCode = statusCode;
        }
        
    }

    public static ResponseEntity<ResponseMessage<Object>> successfulResponse (String message, Object data) {
        ResponseMessage<Object> body = new ResponseMessage<>(false, message, 200, data);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseMessage<Object>> failedResponse (HttpStatus status, String message) {
        ResponseMessage<Object> body = new ResponseMessage<>(true, message, status.value());
        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<ResponseMessage<Object>> failedResponse (Exception e) {
        ResponseMessage<Object> body = new ResponseMessage<>(true, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

