package zesp03.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import zesp03.common.exception.AccessException;
import zesp03.common.exception.NotFoundException;
import zesp03.common.exception.SNMPException;
import zesp03.common.exception.ValidationException;

@ControllerAdvice
public class ExceptionConfig {
    private static final Logger log = LoggerFactory.getLogger(ExceptionConfig.class);

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String handleBadRequest(Exception exc) {
        log.warn("400 Bad Request: {}", exc.getLocalizedMessage());
        return "error-400";
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessException.class)
    public String handleForbidden(Exception exc) {
        log.warn("403 Forbidden: {}", exc.getLocalizedMessage());
        return "error-403";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Exception exc) {
        log.warn("404 Not Found: {}", exc.getLocalizedMessage());
        return "error-404";
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SNMPException.class)
    public String handleInternalServerError(Exception exc) {
        log.warn("500 Internal Server Error: {}", exc.getLocalizedMessage());
        return "error-500";
    }
}