package com.protsprog.ministore.admin;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.protsprog.ministore.models.IconClass;
import com.protsprog.ministore.models.SEOs;
import com.protsprog.ministore.models.ServiceItem;
import com.protsprog.ministore.repositories.ServiceRepository;

/*
 * page params
 * - seos
 * - composeLink
 * - editLink
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ServiceRepository serviceRepo;

    @GetMapping
    public String dashboard(Model model) {

        // get current user
        // SecurityContext context = SecurityContextHolder.getContext();
        // Authentication authentication = context.getAuthentication();
        // String username = authentication.getName();
        // Object principal = authentication.getPrincipal();
        // Collection<? extends GrantedAuthority> authorities =
        // authentication.getAuthorities();

        // System.out.println("username: " + username);
        // System.out.println("principal: " + principal);
        // for (GrantedAuthority grantedAuthority : authorities) {
        // System.out.println("grantedAuthority: " + grantedAuthority);
        // }

        model.addAttribute("seos", new SEOs("Dashboard", "", ""));
        model.addAttribute("menu", AdminMenuHolder.getMenu("/admin"));

        return "admin-dashboard";
    }

    @GetMapping("/services")
    public String serviceList(Model model) {

        model.addAttribute("seos", new SEOs("Services", "", ""));
        model.addAttribute("menu", AdminMenuHolder.getMenu("/admin/services"));
        model.addAttribute("composeLink", "/admin/services/create");

        ArrayList<ServiceItem> serviceList = this.serviceRepo.findAllByOrderBySortAsc();
        model.addAttribute("services", serviceList);

        return "admin-service-list";
    }

    @GetMapping("/services/create")
    public String serviceCreate(Model model) {// Map<String, Object> model
        ServiceItem item = new ServiceItem();

        model.addAttribute("item", item);

        model.addAttribute("seos", new SEOs("Compose service item", "", ""));
        model.addAttribute("menu", AdminMenuHolder.getMenu("/admin/services"));

        ArrayList<IconClass> iconClasses = ServiceItem.getListIconClasses();
        model.addAttribute("allIconClasses", iconClasses);

        return "admin-service-edit";
    }

    @PostMapping("/services")
    private String createItem(ServiceItem item, BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in creating the service.");
            return "admin-service-edit";
        }

        this.serviceRepo.save(item);
        redirectAttributes.addFlashAttribute("message", "New Service Created");

        // return "redirect:/admin/services/" + item.getId();
        return "redirect:/admin/services";
    }

    @DeleteMapping("/services/{id}")
    private ResponseEntity<Void> deleteCashCard(@PathVariable Long id) {

        if (this.serviceRepo.existsById(id)) {
            this.serviceRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/services/{id}/edit")
    public String updateItem(@PathVariable Long id, Model model) {
        model.addAttribute("seos", new SEOs("Compose service item", "", ""));
        model.addAttribute("menu", AdminMenuHolder.getMenu("/admin/services"));

        ArrayList<IconClass> iconClasses = ServiceItem.getListIconClasses();
        model.addAttribute("allIconClasses", iconClasses);

        ServiceItem item;

        if (this.serviceRepo.existsById(id)) {
            item = this.serviceRepo.findById(id).get();
            model.addAttribute("id", id);
        } else {
            item = new ServiceItem();
        }

        model.addAttribute("item", item);
        return "admin-service-edit";
    }

    @PostMapping("/services/{id}/edit")
    public String processUpdateServiceForm(ServiceItem item, BindingResult result, @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "There was an error in updating the service.");
            return "admin-service-edit";
        }

        item.setId(id);
        this.serviceRepo.save(item);

        redirectAttributes.addFlashAttribute("message", "Service Values Updated");

        // return "redirect:/admin/services/{id}";
        return "redirect:/admin/services";

    }

}
