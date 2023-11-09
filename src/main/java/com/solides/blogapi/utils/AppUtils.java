package com.solides.blogapi.utils;

import com.solides.blogapi.exception.BlogApiException;
import org.springframework.http.HttpStatus;

public class AppUtils {

    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "O tamanho do numero não pode ser menor que zero.");
        }

        if (size < 0) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "O tamanho do numero não pode ser menor que zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "O tamanho a pagina dever ser maior que " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
