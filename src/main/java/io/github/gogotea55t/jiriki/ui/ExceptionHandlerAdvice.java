package io.github.gogotea55t.jiriki.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTDecodeException;

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
}
