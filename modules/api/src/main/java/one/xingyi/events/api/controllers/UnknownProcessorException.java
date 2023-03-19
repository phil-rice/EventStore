package one.xingyi.events.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Unknown processor")
public class UnknownProcessorException extends RuntimeException {
    public UnknownProcessorException(String processor) {
        super("Unknown processor " + processor);
    }
}
