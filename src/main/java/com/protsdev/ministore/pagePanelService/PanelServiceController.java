package com.protsdev.ministore.pagePanelService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.protsdev.ministore.enums.MenuConfigurator;
import com.protsdev.ministore.forms.FormConfigurator;
import com.protsdev.ministore.localize.LocalizeService;
import com.protsdev.ministore.pageCommon.ListPagination;
import com.protsdev.ministore.pagePanel.PanelMenuService;

import jakarta.validation.Valid;

/*
Services
 */
@Controller
@RequestMapping("/panel/service")
public class PanelServiceController {
    // configs
    private final MenuConfigurator section = MenuConfigurator.PANEL_SERVICE;
    private final String[] titles;

    private LocalizeService localize;

    @Autowired
    private PanelMenuService panelMenuService;
    @Autowired
    private FormConfigurator formConfigurator;
    @Autowired
    private PanelServiceService service;

    public PanelServiceController(LocalizeService lS) {

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

        // bar services
        model.addAttribute("composeLink", section.getLink() + "/create");

        // set table headers
        var headerFields = PanelServiceListHeaders.class.getDeclaredFields();
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerFields.length; i++) {
            headers.add(headerFields[i].getName());
        }
        model.addAttribute("headers", headers);

        ListPagination pList = service.fetch(page, search,
                section.getLink() + "/%s/edit",
                section.getLink() + "/%s");
        model.addAttribute("list", pList);

        return "panel-list";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("item") PanelServiceFormFields item, Model model) {
        // title & menus
        model.addAttribute("title", titles[1]);
        model.addAttribute("formType", "create");
        model.addAttribute("formAction", section.getLink());
        model.addAttribute("formFields", formConfigurator.getFormFields(PanelServiceFormFields.class));
        return "panel-edit";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("title", titles[2]);

        var entityOp = service.getById(id);
        if (entityOp.isPresent()) {
            model.addAttribute("item", entityOp.get());
        } else {
            return "redirect:" + section.getLink();
        }

        model.addAttribute("formFields", formConfigurator.getFormFields(PanelServiceFormFields.class));
        return "panel-edit";
    }

    @PostMapping
    private String processCreate(final @Valid @ModelAttribute("item") PanelServiceFormFields item,
            final BindingResult result,
            RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", titles[1]);
            model.addAttribute("formType", "create");
            model.addAttribute("formFields", formConfigurator.getFormFields(PanelServiceFormFields.class));
            return "panel-edit";
        }

        service.create(item);
        redirectAttributes.addFlashAttribute("message", localize
                .getMessage("page.panel.edit.message.created"));

        return "redirect:" + section.getLink();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable Long id) {

        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/edit")
    public String processUpdate(final @Valid @ModelAttribute("item") PanelServiceFormFields item,
            BindingResult result,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", titles[2]);
            model.addAttribute("formFields", formConfigurator.getFormFields(PanelServiceFormFields.class));
            return "panel-edit";
        }

        service.update(item, id);

        redirectAttributes.addFlashAttribute("message", localize
                .getMessage("page.panel.edit.message.updated"));

        return "redirect:" + section.getLink();
    }
}
