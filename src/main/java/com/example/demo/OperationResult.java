package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.UUID;

public class OperationResult<T> extends ResponseEntity<T> {

    private String resultUUID;
    private String requestUUID;
    private Status status;
    private String advice;

    private T payload;

    public  enum Status{
        SUCCESS(HttpStatus.OK), FAILURE(HttpStatus.INTERNAL_SERVER_ERROR);

        Status(HttpStatus value) {
            this.value = value;
        }
        private HttpStatus value;

        public HttpStatus getValue() {
            return value;
        }

    }




    public OperationResult(@Nullable T body, HttpStatus status) {
        super(body, status);
    }

    public OperationResult(String resultUUID, String requestUUID, Status status, String advice, T payload) {
        this(payload,  status.value);
        this.resultUUID = resultUUID;
        this.requestUUID = requestUUID;
        this.status = status;
        this.advice = advice;
        this.payload = payload;
    }

    public static <R> OperationResult<R> succes(R payload ) {
        return new OperationResult(UUID.randomUUID().toString(), "", Status.SUCCESS, null, payload);
    }

    public static <R> OperationResult<R> error(R payload, String error) {
        return new OperationResult(UUID.randomUUID().toString(), "", Status.FAILURE, error, payload);
    }


    public String getResultUUID() {
        return resultUUID;
    }

    public void setResultUUID(String resultUUID) {
        this.resultUUID = resultUUID;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }



    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
