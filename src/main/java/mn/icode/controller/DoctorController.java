package mn.icode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mn.icode.model.Doctor;
import mn.icode.service.DepartmentService;
import mn.icode.service.DoctorService;

@Controller
@RequestMapping("/admin/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;

    public DoctorController(DoctorService doctorService, DepartmentService departmentService) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new Doctor());
        }
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "admin/doctors";
    }

    @PostMapping("/save")
    public String saveDoctor(@ModelAttribute("doctor") Doctor doctor, RedirectAttributes redirectAttributes) {
        try {
            doctorService.saveDoctor(doctor);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save doctor: " + e.getMessage());
        }
        return "redirect:/admin/doctors";
    }

    @GetMapping("/edit/{id}")
    public String editDoctorForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("doctor", doctorService.getDoctorById(id));
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "admin/doctors";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Doctor not found with ID: " + id);
            return "redirect:/admin/doctors";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteDoctor(id);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete doctor because they have existing schedules or appointments. Set Active status to No instead.");
        }
        return "redirect:/admin/doctors";
    }
}