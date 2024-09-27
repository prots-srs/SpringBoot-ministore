package com.protsdev.ministore.storage;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.protsdev.ministore.dto.ContentFile;

@Controller
public class UploadedContentController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/content/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Optional<ContentFile> file = storageService.loadAsResource(filename);

        if (!file.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // download
        // return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        // "attachment; filename=\"" + file.getFilename() + "\"").body(file);

        // show
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, file.get().contentType())
                .body(file.get().file());
    }

}
