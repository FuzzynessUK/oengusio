package app.oengus.exception;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.NestedServletException;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

// TODO: Add proper json errors once we have the new front end going
//  https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
@ControllerAdvice
public class OengusExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(OengusExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(final NotFoundException exc, final HttpServletRequest req) {
        final String header = req.getHeader("oengus-version");

        if (!"2".equals(header)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(toMap(req, exc));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<?> notFoundException(final LoginException exc, final HttpServletRequest req) {
        final String header = req.getHeader("oengus-version");

        if (!"2".equals(header)) {
            return ((ResponseEntity.BodyBuilder) ResponseEntity.notFound()).body(exc.getMessage());
        }

        return ResponseEntity.badRequest().body(toMap(req, exc));
    }

    @ExceptionHandler(NestedServletException.class)
    public ResponseEntity<?> validationException(final NestedServletException exc, final HttpServletRequest req) {
        final Throwable cause = exc.getRootCause();

        if (cause instanceof ConstraintViolationException ex) {
            final Map<String, Object> stringStringMap = toMap(req, ex);

            stringStringMap.put("errors", ex.getConstraintViolations());

            return ResponseEntity.badRequest().body(stringStringMap);
        }

        LOG.error("Uncaught NestedServletException", exc);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(toMap(req, exc));
    }

    // TODO: find all parts that catch this exception and remove it
    @ExceptionHandler(OengusBusinessException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> oengusBusinessExceptionHandler(final OengusBusinessException e, final HttpServletRequest req) {
        final String header = req.getHeader("oengus-version");

        if (!"2".equals(header)) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.badRequest().body(toMap(req, e));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> requestHandlingNoHandlerFound(final NoHandlerFoundException e) {
        final Map<String, String> mapper = new HashMap<>();

        mapper.put("type", "NotFoundException");
        mapper.put("message", "The requested page was not found");
        mapper.put("method", e.getHttpMethod());
        mapper.put("path", e.getRequestURL());

        return mapper;
    }

    private Map<String, Object> toMap(final HttpServletRequest req, final Exception exception) {
        final Map<String, Object> mapper = new HashMap<>();

        mapper.put("type", exception.getClass().getSimpleName());
        mapper.put("message", exception.getMessage());
        mapper.put("method", req.getMethod());
        mapper.put("path", req.getServletPath());

        return mapper;
    }
}
