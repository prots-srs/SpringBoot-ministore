package com.protsdev.ministore.pageSeo;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

import com.protsdev.ministore.domen.MenuConfigurator;
import com.protsdev.ministore.domen.PanelMenuService;
import com.protsdev.ministore.dto.FormField;
import com.protsdev.ministore.dto.PaginationList;
import com.protsdev.ministore.localize.LocalizeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
SEOs
 */
@Controller
@RequestMapping("/panel/seo")
public class PanelSeoController {

    // configs
    private final String mappingLink = MenuConfigurator.PANEL_SEO.getLink();
    private final String mappingType = MenuConfigurator.PANEL_SEO.toString();
    private final String formClassPath = "com.protsdev.ministore.pageSeo.FormSeo";
    private final int TEXT_SIZE = 70;
    private final String[] titles;

    private LocalizeService lS;
    @Autowired
    private SeoService seoService;

    @Autowired
    private PanelMenuService panelMenuService;

    public PanelSeoController(LocalizeService lS) {

        this.lS = lS;

        this.titles = new String[] {
                lS.getMessage("page.panel.menu." + mappingType),
                lS.getMessage("page.panel.edit.title.compose") + ": "
                        + lS.getMessage("page.panel.menu." + mappingType),
                lS.getMessage("page.panel.edit.title.update") + ": "
                        + lS.getMessage("page.panel.menu." + mappingType)
        };
    }

    @ModelAttribute
    public void panelAttribute(Model model) {
        model.addAttribute("mappingLink", mappingLink);

        model.addAttribute("menu", panelMenuService.getLeftMenu(mappingLink));

        List<FormField> fields = new LinkedList<>();
        try {
            Class<?> cls = Class.forName(formClassPath);
            Field[] flds = cls.getDeclaredFields();
            for (Field f : flds) {
                String type = "text";
                Boolean required = false;
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
                    if (f.getAnnotation(NotBlank.class) != null) {
                        required = true;
                    }
                }

                fields.add(new FormField(f.getName(), type,
                        lS.getMessage("page.panel.list.headers." + f.getName()), required));

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        model.addAttribute("formFields", fields);
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        // title & menus
        model.addAttribute("title", titles[0]);

        // bar services
        model.addAttribute("composeLink", mappingLink + "/create");

        PaginationList pList = seoService.fetch(page, search, mappingLink + "/%s/edit", mappingLink + "/%s");
        model.addAttribute("list", pList);

        return "panel-list";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("item") FormSeo item, Model model) {
        // title & menus
        model.addAttribute("title", titles[1]);
        model.addAttribute("formType", "create");
        model.addAttribute("formAction", mappingLink);
        return "panel-edit";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("title", titles[2]);

        var entityOp = seoService.getById(id);
        if (entityOp.isPresent()) {
            model.addAttribute("item", entityOp.get());
            model.addAttribute("id", id);
        } else {
            return "redirect:" + mappingLink;
        }
        return "panel-edit";
    }

    @PostMapping
    private String processCreate(final @Valid @ModelAttribute("item") FormSeo item, final BindingResult result,
            RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", titles[1]);
            return "panel-edit";
        }

        seoService.create(item);
        redirectAttributes.addFlashAttribute("message", lS
                .getMessage("page.panel.edit.message.created"));

        return "redirect:" + mappingLink;
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable Long id) {

        if (seoService.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/edit")
    public String processUpdate(final @Valid @ModelAttribute("item") FormSeo item,
            BindingResult result,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title", titles[2]);
            return "panel-edit";
        }

        seoService.update(id, item);

        redirectAttributes.addFlashAttribute("message", lS
                .getMessage("page.panel.edit.message.updated"));

        return "redirect:" + mappingLink;
    }
}
