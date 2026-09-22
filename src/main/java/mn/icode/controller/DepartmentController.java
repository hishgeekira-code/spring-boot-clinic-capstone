package mn.icode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mn.icode.model.Department;
import mn.icode.service.DepartmentService;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        if (!model.containsAttribute("department")) {
            model.addAttribute("department", new Department());
        }
        return "admin/departments";
    }

    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute("department") Department department,
                                 RedirectAttributes redirectAttributes) {
        try {
            departmentService.saveDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage", "Department saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save department: " + e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @GetMapping("/edit/{id}")
    public String editDepartmentForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("department", departmentService.getDepartmentById(id));
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "admin/departments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Department not found with ID: " + id);
            return "redirect:/admin/departments";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete department because it is assigned to one or more doctors.");
        }
        return "redirect:/admin/departments";
    }
}