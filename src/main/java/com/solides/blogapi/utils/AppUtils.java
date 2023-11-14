package com.solides.blogapi.utils;

import com.solides.blogapi.exception.BlogApiException;
import org.springframework.http.HttpStatus;

import static com.solides.blogapi.utils.AppConstants.DEFAULT_PAGE_NUMBER;

public class AppUtils {

    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "O tamanho do numero não pode ser menor que " + DEFAULT_PAGE_NUMBER);
        }

        if (size < 0) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "O tamanho do numero não pode ser menor que " + DEFAULT_PAGE_NUMBER);
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "O tamanho a pagina dever ser maior que " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
