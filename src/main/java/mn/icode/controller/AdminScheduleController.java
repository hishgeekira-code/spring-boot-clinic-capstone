package mn.icode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mn.icode.model.Schedule;
import mn.icode.service.DoctorService;
import mn.icode.service.ScheduleService;

@Controller
@RequestMapping("/admin/schedules")
public class AdminScheduleController {

    private final ScheduleService scheduleService;
    private final DoctorService doctorService;

    public AdminScheduleController(ScheduleService scheduleService, DoctorService doctorService) {
        this.scheduleService = scheduleService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        if (!model.containsAttribute("schedule")) {
            model.addAttribute("schedule", new Schedule());
        }
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "admin/schedules";
    }

    @PostMapping("/save")
    public String saveSchedule(@ModelAttribute("schedule") Schedule schedule, RedirectAttributes redirectAttributes) {
        try {
            if (schedule.getStartTime() != null && schedule.getEndTime() != null
                    && schedule.getStartTime().isAfter(schedule.getEndTime())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Start time must be before end time.");
                return "redirect:/admin/schedules";
            }

            scheduleService.saveSchedule(schedule);
            redirectAttributes.addFlashAttribute("successMessage", "Schedule saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save schedule: " + e.getMessage());
        }
        return "redirect:/admin/schedules";
    }

    @GetMapping("/edit/{id}")
    public String editScheduleForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("schedule", scheduleService.getScheduleById(id));
            model.addAttribute("schedules", scheduleService.getAllSchedules());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            return "admin/schedules";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Schedule not found with ID: " + id);
            return "redirect:/admin/schedules";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            scheduleService.deleteSchedule(id);
            redirectAttributes.addFlashAttribute("successMessage", "Schedule deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete schedule because it is linked to an existing appointment.");
        }
        return "redirect:/admin/schedules";
    }
}