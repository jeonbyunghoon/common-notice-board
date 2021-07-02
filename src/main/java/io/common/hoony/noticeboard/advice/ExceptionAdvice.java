package io.common.hoony.noticeboard.advice;

import io.common.hoony.noticeboard.error.DkargoException;
import io.common.hoony.noticeboard.error.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(DkargoException.class)
    public ResponseEntity<ErrorDTO> dkargoException(DkargoException e) {
        log.debug("### Exception message: {}", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(new ErrorDTO(e));
    }

    // @vaild 유효성체크에 통과하지 못하면 MethodArgumentNotValidException 이 발생한다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = String.format("[%s] %s", e.getBindingResult().getFieldError().getField(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        log.error("### Bad request message : {}", errorMessage);
        return ErrorDTO.BAD_REQUEST_ERROR().toResponse();
    }

    // 메소드가 잘못되었거나 부적합한 인자를 전달했음을 나타내기 위해 발생한다. (ex : mapper 이름이 다른경우..)
    // (object > Throwable > Exception > RuntimeException > IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> illegalArgumentException(IllegalArgumentException e) {
        log.error("### Bad request message : {}", e.getMessage());
        return ErrorDTO.BAD_REQUEST_ERROR().toResponse();
    }

    // 메소드 인수가 예상 유형이 아닐 때 발생합니다.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("### Bad request message : {}", e.getMessage());
        return ErrorDTO.BAD_REQUEST_ERROR().toResponse();
    }

    // 문자열이 나타내는 숫자와 일치하지 않는 타입의 숫자로 변환시 발생 (ex : String str = "00O7oO"; -> System.out.println(Integer.parseInt(str));
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDTO> numberFormatException(NumberFormatException e) {
        log.error("### Bad request message : {}", e.getMessage());
        return ErrorDTO.BAD_REQUEST_ERROR().toResponse();
    }

    // HttpMessageConverter 에서 발생하며 read 메서드가 실해한 경우 발생한다. (400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("### Bad request message : {}", e.getMessage());
        return ErrorDTO.BAD_REQUEST_ERROR().toResponse();
    }
}
