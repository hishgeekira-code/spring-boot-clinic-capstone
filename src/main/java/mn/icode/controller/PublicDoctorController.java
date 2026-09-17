package mn.icode.controller;

import mn.icode.model.Doctor;
import mn.icode.service.DoctorService;
import mn.icode.service.ScheduleService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctors")
public class PublicDoctorController {

    private final DoctorService doctorService;
    private final ScheduleService scheduleService;

    public PublicDoctorController(DoctorService doctorService, ScheduleService scheduleService) {
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "doctors/index";
    }

    @GetMapping("/{id}")
    public String doctorDetail(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctor", doctor);
        model.addAttribute("schedules", scheduleService.getSchedulesByDoctorId(id));
        return "doctors/detail";
    }
}