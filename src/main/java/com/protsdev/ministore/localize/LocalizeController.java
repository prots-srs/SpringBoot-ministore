package com.protsdev.ministore.localize;

import java.text.DateFormat;
import java.text.NumberFormat;
/*
 * TO READ
 * https://simplelocalize.io/blog/posts/java-internationalization/
 * https://simplelocalize.io/blog/posts/spring-boot-simple-internationalization/
 * https://docs.oracle.com/javase/8/docs/api/java/text/NumberFormat.html
 * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/text/DateFormat.html
 * https://docs.oracle.com/javase/tutorial/datetime/overview/index.html
 */
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.val;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LocalizeController {

    @Autowired
    private LocalizeService localizeService;

    @GetMapping("/localize")
    public String getMethodName(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("language", locale.getLanguage());
        model.addAttribute("languageName", localizeService.getMessage("lang." + locale.getLanguage()));
        model.addAttribute("selectLanguages", localizeService.getLanguages());

        var number = NumberFormat.getNumberInstance(locale).format(1234567.89);
        model.addAttribute("number", number);

        var price = NumberFormat.getCurrencyInstance(locale).format(123.89);
        model.addAttribute("price", price);

        Date dateNow = new Date();
        var date = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(dateNow);
        model.addAttribute("date", date);

        var time = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale).format(dateNow);
        model.addAttribute("time", time);

        return "localize";
    }

    @ResponseBody
    @PostMapping("/localize")
    public Map<String, String> postMethodName(@RequestParam String language) {
        Map<String, String> answer = new HashMap<>();

        try {
            val curL = SiteLanguages.valueOf(language);
            answer.put("code", "2");
            answer.put("language", curL.toString());
        } catch (Exception e) {
            answer.put("code", "1");
        }

        return answer;
    }

}
