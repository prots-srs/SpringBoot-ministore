package com.protsdev.ministore.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class UploadedContentController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/content/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        // download
        // return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        // "attachment; filename=\"" + file.getFilename() + "\"").body(file);

        // show
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(file);
    }

}
