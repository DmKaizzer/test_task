package test.task.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ExceptionData> handleException(Exception e) {
		ExceptionData data = new ExceptionData();
		data.setInfo(e.getMessage());
		data.setData(new Date());
		log.error("Throw exception cause by: " + e.getMessage());
		return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
	}
}