package net.devk.kalah.game;

import java.util.Optional;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller advice for converting exceptions to meaningful message with status
 * code
 */
@ControllerAdvice(annotations = RestController.class)
public class GameControllerAdvice {

	private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error");

	@ExceptionHandler(IllegalPlayException.class)
	public ResponseEntity<VndErrors> illegalPlayException(IllegalPlayException exception) {
		return this.error(exception, HttpStatus.BAD_REQUEST, "Illegal Play");
	}

	@ExceptionHandler(GameNotFoundException.class)
	public ResponseEntity<VndErrors> gameNotFoundException(GameNotFoundException exception) {
		return this.error(exception, HttpStatus.NOT_FOUND, "Game Not Found");
	}

	private <E extends Exception> ResponseEntity<VndErrors> error(E error, HttpStatus httpStatus, String logref) {
		String message = Optional.of(error.getMessage()).orElse(error.getClass().getSimpleName());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(this.vndErrorMediaType);
		return new ResponseEntity<>(new VndErrors(logref, message), httpHeaders, httpStatus);

	}

}
