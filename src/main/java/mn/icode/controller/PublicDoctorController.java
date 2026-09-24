package mn.icode.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import mn.icode.model.Doctor;
import mn.icode.service.DepartmentService;
import mn.icode.service.DoctorService;
import mn.icode.service.ScheduleService;

@Controller
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

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated()
                             && !authentication.getName().equals("anonymousUser");

        boolean isAdmin = isLoggedIn && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isAdmin", isAdmin);

        return "home"; // redirect биш, templates/home.html-ийг дуудна
    }

    @GetMapping("/doctors")
    public String listDoctors(@RequestParam(required = false) String name,
                              @RequestParam(required = false) Long departmentId,
                              @RequestParam(required = false) String specialization,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Authentication authentication,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Doctor> doctorPage = doctorService.searchDoctorsPaginated(name, departmentId, specialization, pageable);

        // Хэрэглэгч нэвтэрсэн эсэхийг шалгах:
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated()
                             && !authentication.getName().equals("anonymousUser");

        // Админ мөн эсэхийг шалгах:
        boolean isAdmin = isLoggedIn && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isAdmin", isAdmin);

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

    @GetMapping("/doctors/{id}")
    public String doctorDetail(@PathVariable Long id, 
                               Authentication authentication, 
                               Model model) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            if (!doctor.isActive()) {
                return "redirect:/doctors";
            }
            model.addAttribute("doctor", doctor);
            model.addAttribute("schedules", scheduleService.getSchedulesByDoctorId(id));

            // Нэвтэрсэн төлөвийг шалгах хэсэг:
            boolean isLoggedIn = authentication != null && authentication.isAuthenticated()
                                 && !authentication.getName().equals("anonymousUser");

            boolean isAdmin = isLoggedIn && authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            model.addAttribute("isLoggedIn", isLoggedIn);
            model.addAttribute("isAdmin", isAdmin);

            return "doctors/detail";
        } catch (Exception e) {
            return "redirect:/doctors";
        }
    }
}