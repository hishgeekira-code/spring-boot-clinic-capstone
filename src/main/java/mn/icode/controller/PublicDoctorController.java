package mn.icode.controller;

import mn.icode.model.Doctor;
import mn.icode.service.DepartmentService;
import mn.icode.service.DoctorService;
import mn.icode.service.ScheduleService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/doctors")
public class PublicDoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final ScheduleService scheduleService;

    public PublicDoctorController(DoctorService doctorService,
    							  DepartmentService departmentService,
    							  ScheduleService scheduleService) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public String listDoctors(@RequestParam(required = false) String name,
    						  @RequestParam(required = false) Long departmentId,
    						  @RequestParam(required = false) String specialization,
    						  @RequestParam(defaultValue = "0") int page,
    						  @RequestParam(defaultValue = "5") int size,
    						  Model model) {
    	Pageable pageable = PageRequest.of(page, size);
    	Page<Doctor> doctorPage = doctorService.searchDoctorsPaginated(name, departmentId, specialization, pageable);
    	
        model.addAttribute("doctorPage", doctorPage);
        model.addAttribute("doctors", doctorPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", doctorPage.getTotalPages());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("name", name);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("specialization", specialization);
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