package com.protsdev.ministore.pagePanelSeo;

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
SEOs
 */
@Controller
@RequestMapping("/panel/seo")
public class PanelSeoController {

    // configs
    private final MenuConfigurator section = MenuConfigurator.PANEL_SEO;
    private final String[] titles;

    private LocalizeService localize;
    @Autowired
    private PanelMenuService panelMenuService;

    @Autowired
    private FormConfigurator formConfigurator;

    @Autowired
    private PanelSeoService service;

    public PanelSeoController(LocalizeService lS) {

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
        var headerFields = PanelSeoListHeaders.class.getDeclaredFields();
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < headerFields.length; i++) {
            headers.add(headerFields[i].getName());
        }
        model.addAttribute("headers", headers);

        // table data
        ListPagination pList = service.fetch(page, search, section.getLink() + "/%s/edit",
                section.getLink() + "/%s");
        model.addAttribute("list", pList);

        return "panel-list";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("item") PanelSeoFormFields item, Model model) {
        // title & menus
        model.addAttribute("title", titles[1]);
        model.addAttribute("formType", "create");
        model.addAttribute("formAction", section.getLink());
        model.addAttribute("formFields", formConfigurator.getFormFields(PanelSeoFormFields.class));
        return "panel-edit";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("title", titles[2]);

        var itemOp = service.getById(id);
        if (itemOp.isPresent()) {
            model.addAttribute("item", itemOp.get());
        } else {
            return "redirect:" + section.getLink();
        }

        model.addAttribute("formFields", formConfigurator.getFormFields(PanelSeoFormFields.class));
        return "panel-edit";
    }

    @PostMapping
    private String processCreate(final @Valid @ModelAttribute("item") PanelSeoFormFields item,
            final BindingResult result,
            RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", titles[1]);
            model.addAttribute("formFields", formConfigurator.getFormFields(PanelSeoFormFields.class));
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
    public String processUpdate(final @Valid @ModelAttribute("item") PanelSeoFormFields item,
            BindingResult result,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", titles[2]);
            model.addAttribute("formFields", formConfigurator.getFormFields(PanelSeoFormFields.class));
            return "panel-edit";
        }

        service.update(item, id);

        redirectAttributes.addFlashAttribute("message", localize
                .getMessage("page.panel.edit.message.updated"));

        return "redirect:" + section.getLink();
    }
}
