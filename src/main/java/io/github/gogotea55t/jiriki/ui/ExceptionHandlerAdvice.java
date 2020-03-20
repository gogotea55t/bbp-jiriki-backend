package io.github.gogotea55t.jiriki.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTDecodeException;

import io.github.gogotea55t.jiriki.domain.ErrorResponse;

/**
 * 例外ハンドリング用クラス
 *
 * @author gogotea55t
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(value = JWTDecodeException.class)
  public ResponseEntity<?> aaa() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> invalidRequest(Throwable error) {
    ErrorResponse body = new ErrorResponse();
    body.setMessage(error.getMessage());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> validationFailed(Throwable error) {
    ErrorResponse body = new ErrorResponse();
    body.setMessage(error.getMessage());
    return ResponseEntity.badRequest().body(body);
  }
}
