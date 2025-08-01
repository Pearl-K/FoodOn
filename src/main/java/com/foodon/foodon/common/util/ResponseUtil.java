package com.foodon.foodon.common.util;

import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.exception.ErrorCode;
import com.foodon.foodon.common.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Component
public class ResponseUtil {

    public static <T> ResponseEntity<Response<T>> success(String message, T data) {
        return ResponseEntity.ok(Response.<T>builder().code("00000").message(message).data(data).build());
    }

    public static <T> ResponseEntity<Response<T>> success(T data) {
        return ResponseEntity.ok(
                Response.<T>builder()
                        .code("00000")
                        .message("정상적으로 처리되었습니다.")
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<Response<T>> success() {
        return ResponseEntity.ok(
                Response.<T>builder()
                        .code("00000")
                        .message("정상적으로 처리되었습니다.")
                        .build());
    }

    public static <T> ResponseEntity<Response<T>> failure(
            GlobalException e,
            ErrorCode errorCode
    ) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(Response.<T>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static <T> ResponseEntity<Response<T>> failure(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.<T>builder()
                        .code("50000")
                        .message("서버 에러가 발생하였습니다.")
                        .build());
    }

    public static <T> ResponseEntity<Response<T>> created() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<T>builder().code("00000")
                        .build());
    }

    public static <T> ResponseEntity<Response<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<T>builder()
                        .code("00000")
                        .message("정상적으로 처리되었습니다.")
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<Response<T>> failure(
        MethodArgumentNotValidException e,
        String message
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.<T>builder()
                .code("70000")
                .message(message)
                .build());
    }

    public static <T> ResponseEntity<Response<T>> failure(
        MethodArgumentTypeMismatchException e,
        String message
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.<T>builder()
                .code("80000")
                .message(message)
                .build());
    }

    public static <T> ResponseEntity<Response<T>> failure(
            MissingServletRequestParameterException e,
            String message
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.<T>builder()
                        .code("04000")
                        .message(message)
                        .build());
    }
}
