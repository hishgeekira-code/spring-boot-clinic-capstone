package mn.icode.controller;

import mn.icode.model.Schedule;
import mn.icode.service.DoctorService;
import mn.icode.service.ScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "admin/schedules";
    }

    @PostMapping("/save")
    public String saveSchedule(@ModelAttribute("schedule") Schedule schedule) {
        scheduleService.saveSchedule(schedule);
        return "redirect:/admin/schedules";
    }

    @GetMapping("/edit/{id}")
    public String editScheduleForm(@PathVariable Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getScheduleById(id));
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "admin/schedules";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return "redirect:/admin/schedules";
    }
}