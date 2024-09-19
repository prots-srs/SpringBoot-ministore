package com.protsdev.ministore.forms;

import java.util.Map;

public record FormField(
        String name,
        String type,
        String label,
        Boolean required,
        Map<?, String> options) {

}
