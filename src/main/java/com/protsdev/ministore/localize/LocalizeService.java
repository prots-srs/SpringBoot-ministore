package com.protsdev.ministore.localize;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LocalizeService {

    private final MessageSource messageSource;

    public LocalizeService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getLocalizedMessage(String translationKey) {
        Locale locale = LocaleContextHolder.getLocale();

        String outputMessage = "";
        try {
            outputMessage = messageSource.getMessage(translationKey, null, locale);
        } catch (Exception e) {
            outputMessage = "";
        }
        return outputMessage;
    }

    public List<String> getLanguages() {
        return new ArrayList<>(List.of(SiteLanguages.en.toString(), SiteLanguages.uk.toString()));
    }
}
