package io.github.gogotea55t.jiriki.domain.exception;

@SuppressWarnings("serial")
public class SongsNotFoundException extends RuntimeException {
  private String message;

  public SongsNotFoundException(String message) {
    this.message = message;
  }
  
  public String getMessage() {
	return this.message;
  }
}
