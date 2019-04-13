package io.github.gogotea55t.jiriki.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 例外ハンドリング用クラス
 *
 * @author gogotea55t
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
  @ExceptionHandler(value = NullPointerException.class)
  public ResponseEntity<?> userNotFound() {
    return ResponseEntity.badRequest().build();
  }
}
