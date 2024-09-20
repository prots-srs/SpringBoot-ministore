package com.protsdev.ministore.pagePanelFiles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.protsdev.ministore.enums.MenuConfigurator;
import com.protsdev.ministore.localize.LocalizeService;
import com.protsdev.ministore.pageCommon.ListPagination;
import com.protsdev.ministore.pagePanel.PanelMenuService;

@Controller
@RequestMapping("/panel/files")
public class PanelFilesController {

    // configs
    private final MenuConfigurator section = MenuConfigurator.PANEL_FILES;
    private final String[] titles;

    private LocalizeService localize;

    @Autowired
    private PanelMenuService panelMenuService;

    @Autowired
    private PanelFilesService service;

    public PanelFilesController(LocalizeService lS) {

        localize = lS;

        titles = new String[] {
                localize.getMessage("page.panel.menu." + section.toString()),
                localize.getMessage("page.panel.edit.title.compose") + ": "
                        + localize.getMessage("page.panel.menu." + section.toString()),
                localize.getMessage("page.panel.edit.title.update") + ": "
                        + localize.getMessage("page.panel.menu." + section.toString())
        };
    }

    @ModelAttribute
    public void panelAttribute(Model model) {
        model.addAttribute("mappingLink", section.getLink());

        model.addAttribute("menu", panelMenuService.getLeftMenu(section.getLink()));
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        // title & menus
        model.addAttribute("title", titles[0]);

        // set table headers
        var headerFields = PanelFilesListHeaders.class.getDeclaredFields();
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerFields.length; i++) {
            headers.add(headerFields[i].getName());
        }
        model.addAttribute("headers", headers);

        ListPagination pList = service.fetch(page, search, null,
                section.getLink() + "/%s");
        model.addAttribute("list", pList);

        return "panel-list";
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable Long id) {

        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
