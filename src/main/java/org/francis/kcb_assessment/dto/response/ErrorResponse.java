package org.francis.kcb_assessment.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    // HTTP status questionCode
    private HttpStatus status;

    private int statusCode;

    //List of constructed messages
    private List<String> messages;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.statusCode = status.value();
        this.messages = Collections.singletonList(message);

    }

    public ErrorResponse(HttpStatus status, List<String> messages) {
        this.status = status;
        this.statusCode = status.value();
        this.messages = messages;
    }
}
