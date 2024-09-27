package com.protsdev.ministore.forms;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.protsdev.ministore.enums.IconConfigurator;
import com.protsdev.ministore.enums.ProductTypes;
import com.protsdev.ministore.localize.LocalizeService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Service
public class FormConfigurator {

    private final int TEXT_SIZE = 70;

    @Autowired
    private LocalizeService localize;

    public List<FormField> getFormFields(Class<?> formConfigClass) {
        List<FormField> fields = new LinkedList<>();

        Field[] flds = formConfigClass.getDeclaredFields();

        for (Field f : flds) {

            String type = "text";
            Boolean required = false;
            Map<?, String> options = null;

            if (f.getType().equals(String.class)) {
                type = "text";

                if (f.getAnnotation(Size.class) != null) {
                    Integer size = f.getAnnotation(Size.class).max();
                    if (size != null) {
                        if (size > TEXT_SIZE) {
                            type = "textarea";
                        }
                    }
                }
            } else if (f.getType().equals(Boolean.class)) {
                type = "checkbox";
            } else if (f.getType().equals(IconConfigurator.class)) {
                type = "select";

                Map<IconConfigurator, String> optionsA = new LinkedHashMap<>();
                for (IconConfigurator ico : IconConfigurator.values()) {
                    optionsA.put(ico,
                            localize.getMessage("enum.icons." + ico.getType()));
                }
                options = optionsA;
            } else if (f.getType().equals(ProductTypes.class)) {
                type = "select";

                Map<ProductTypes, String> optionsA = new LinkedHashMap<>();
                for (ProductTypes prodtype : ProductTypes.values()) {
                    optionsA.put(prodtype,
                            localize.getMessage("enum.product." + prodtype));
                }
                options = optionsA;
            } else if (f.getName().equals("fileSaved")) {
                type = "hidden";
                // } else if (f.getType().equals(FormSavedFile.class)) {
                // type = "fileSaved";
            } else if (f.getType().equals(MultipartFile.class)) {
                type = "file";
            }

            if (f.getAnnotation(NotBlank.class) != null
                    || f.getAnnotation(NotEmpty.class) != null
                    || f.getAnnotation(NotNull.class) != null) {
                required = true;
            }

            fields.add(new FormField(f.getName(), type, localize.getMessage("page.panel.list.headers." + f.getName()),
                    required, options));

        }

        return fields;
    }
}
