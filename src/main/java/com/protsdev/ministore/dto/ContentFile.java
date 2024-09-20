package com.protsdev.ministore.dto;

import org.springframework.core.io.Resource;

public record ContentFile(Resource file, String contentType) {

}
