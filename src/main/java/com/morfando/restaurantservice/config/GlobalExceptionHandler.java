package com.morfando.restaurantservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({Exception.class, RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorDTO> handleGeneral(Exception ex) {
		ErrorDTO error = errorDTO("SERVER_ERROR", "Error desconocido", ex, true);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({NoSuchElementException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorDTO> handleNoSuchElement(Exception ex) {
		ErrorDTO error = errorDTO("NOT_FOUND", "Elemento no encontrado", ex, false);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorDTO> handleIllegalArgument(Exception ex) {
		ErrorDTO error = null;
		if (ex instanceof MethodArgumentNotValidException) {
			String message = toMessage((MethodArgumentNotValidException) ex);
			 error = errorDTO("INVALID_ARGUMENT",  message, ex, false);
		} else {
			error = errorDTO("INVALID_ARGUMENT", "Parámetro inválido: " + ex.getMessage(), ex, false);
		}
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({IllegalStateException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorDTO> handleIllegalState(Exception ex) {
		ErrorDTO error = errorDTO("ILLEGAL_STATE", ex.getMessage(), ex, true);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({AuthenticationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorDTO> handleAuthentication(Exception ex) {
		ErrorDTO error = errorDTO("AUTH_ERROR", "Credenciales inválidas", ex, false);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({AccessDeniedException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorDTO> handleAccessDenied(Exception ex) {
		ErrorDTO error = errorDTO("FORBIDDEN", "Autenticación requerida", ex, false);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	private ErrorDTO errorDTO(String code, String message, Throwable t, boolean logException) {
		if (logException) {
			log.error("Error", t);
		} else {
			log.error("Error: {}", t.getMessage());
		}
		return new ErrorDTO(code, message);
	}

	@AllArgsConstructor
	@Getter
	class ErrorDTO {
		private String code;
		private String message;
	}

	private String toMessage(MethodArgumentNotValidException ex) {
		return ex.getBindingResult().getAllErrors().stream()
				.map(objectError -> {
					if (objectError instanceof FieldError) {
						FieldError fe = ((FieldError) objectError);
						return "[" + fe.getField() + "] " + fe.getDefaultMessage();
					}
					return objectError.getDefaultMessage();
				}).collect(Collectors.joining(", "));
	}
}
