package br.com.mls.mltracking.exception;

import io.swagger.client.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by manasses on 10/8/16.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemResourceNotFound extends ApiException {

    private final String message;

    public ItemResourceNotFound(String message, ApiException e) {
        super(e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
