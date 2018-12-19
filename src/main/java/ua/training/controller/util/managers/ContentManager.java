package ua.training.controller.util.managers;

import ua.training.controller.util.LocaleUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

public class ContentManager {
    public String getContent(String key, Locale locale) {
        return ResourceBundle.getBundle("content", locale).getString(key);
    }

    public void setLocalizedMessage(HttpServletRequest request, String attributeKey, String messageKey) {
        request.setAttribute(attributeKey, getContent(messageKey,  LocaleUtil.getLocale(request)));
    }
}