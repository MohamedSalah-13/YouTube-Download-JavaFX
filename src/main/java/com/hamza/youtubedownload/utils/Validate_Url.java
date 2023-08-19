package com.hamza.youtubedownload.utils;

import org.apache.commons.validator.routines.UrlValidator;

public class Validate_Url {
    public boolean isValidURL(String url) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }
}
