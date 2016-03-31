package ru.kadyrov.electron.commerce.controllers;

import ru.kadyrov.electron.commerce.exception.ShopServiceException;
import ru.kadyrov.electron.commerce.services.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public abstract class ApiController {

    @Inject
    protected ShopService shopService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ShopServiceException.class)
    @ResponseBody
    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    public static class ErrorInfo {
        public final String url;
        public final String ex;

        public ErrorInfo(String url, Exception ex) {
            this.url = url;
            this.ex = ex.getLocalizedMessage();
        }
    }

}
