package zesp03.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import zesp03.exception.AccessException;
import zesp03.exception.NotFoundException;
import zesp03.exception.SNMPException;
import zesp03.exception.ValidationException;

@ControllerAdvice
public class ExceptionConfig {
    private static final Logger log = LoggerFactory.getLogger(ExceptionConfig.class);

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String handleBadRequest(Exception exc) {
        log.warn("exception thrown by controller", exc);
        return "error-400";
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessException.class)
    public String handleForbidden(Exception exc) {
        log.warn("exception thrown by controller", exc);
        return "error-403";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Exception exc) {
        log.warn("exception thrown by controller", exc);
        return "error-404";
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SNMPException.class)
    public String handleInternalServerError(Exception exc) {
        log.warn("exception thrown by controller", exc);
        return "error-500";
    }
}