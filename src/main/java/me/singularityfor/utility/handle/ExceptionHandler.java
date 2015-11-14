package me.singularityfor.utility.handle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by hyva on 2015. 11. 15..
 */
public abstract class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleException(MethodArgumentNotValidException exception) {
        //TODO 에러 메시지 통합 관리
        return exception.getMessage();
    }
}
