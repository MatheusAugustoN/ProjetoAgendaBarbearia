package barber.agenda.exception;


import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource; //

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ResponseError responseError(String message, HttpStatus statusCode) {
        ResponseError error = new ResponseError();
        error.setStatus("error");
        error.setError(message);
        error.setStatusCode(statusCode.value());
        error.setTimestamp(new Date());
        return error;
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleGeneral(Exception e, WebRequest request) {
        // Busca a mensagem no arquivo messages.properties ou usa o padrão
        String message = messageSource.getMessage("error.server", new Object[]{e.getMessage()}, null);
        ResponseError error = responseError(message, HttpStatus.INTERNAL_SERVER_ERROR);
        return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
        // Usando seu método privado para manter o padrão
        ResponseError error = responseError(e.getMessage(), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(e, error, headers(), HttpStatus.BAD_REQUEST, request);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Pega o primeiro erro de validação encontrado
        String mensagemErro = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ResponseError error = responseError(mensagemErro, HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, error, headers(), HttpStatus.BAD_REQUEST, request);
    }
}

