package me.singularityfor.utility.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hyva on 2015. 11. 15..
 */
@ControllerAdvice(annotations = RestController.class)
public class GlobalRestControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleNotValidException(MethodArgumentNotValidException exception) {
        //TODO 에러 메시지 통합 관리(로컬라이징
        return exception.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleElseException(Exception exception) {
        return exception.getClass().getSimpleName();
    }
}
