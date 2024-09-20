package com.protsdev.ministore.pagePanelFiles;

import java.util.Map;

public record PanelFilesListHeaders(
        String originalName,
        String fileType,
        String fileSize,
        String savedName,
        String module,
        Map<String, String> actions) {

}
