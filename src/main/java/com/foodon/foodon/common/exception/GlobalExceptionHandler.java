package com.foodon.foodon.common.exception;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.stream.Collectors;

import com.foodon.foodon.common.dto.Response;
import com.foodon.foodon.common.util.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "request = {}, {} \n class = {} \n code = {} \n message = {}";

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Response<Void>> handleGlobalException(
            GlobalException exception,
            HttpServletRequest request
    ){
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(
                LOG_FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
        exception.printStackTrace();

        return ResponseUtil.failure(exception, errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleServerException(
            Exception exception,
            HttpServletRequest request
    ){
        log.warn(
                LOG_FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
        exception.printStackTrace();

        return ResponseUtil.failure(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidationException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        String errorMessages = extractErrorMessages(exception);

        log.warn(
            LOG_FORMAT,
            request.getMethod(),
            request.getRequestURI(),
            exception.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value(),
            errorMessages
        );

        return ResponseUtil.failure(exception,errorMessages);
    }

    private String extractErrorMessages(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.collectingAndThen(
                Collectors.joining(", "),
                msg -> msg.isEmpty() ? "Validation failed" : msg
            ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<Void>> handleTypeMismatchException(
        MethodArgumentTypeMismatchException exception,
        HttpServletRequest request
    ) {
        Class<?> requiredType = exception.getRequiredType();

        if (requiredType == YearMonth.class || requiredType == LocalDate.class) {
            String errorMessage = "잘못된 날짜 형식입니다.";

            log.warn(
                LOG_FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                errorMessage
            );
            exception.printStackTrace();

            return ResponseUtil.failure(exception, errorMessage);
        }

        throw exception;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<Void>> handleMissingParams(
            MissingServletRequestParameterException exception,
            HttpServletRequest request
    ) {
        String missingParam = exception.getParameterName();
        String errorMessage = "요청 파라미터가 누락되었습니다 : " + missingParam;

        log.warn(
            LOG_FORMAT,
            request.getMethod(),
            request.getRequestURI(),
            exception.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value(),
            errorMessage
        );

        return ResponseUtil.failure(exception, errorMessage);
    }

}

