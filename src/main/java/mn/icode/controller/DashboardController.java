package mn.icode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mn.icode.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // /admin болон /admin/dashboard хоёр хаягийн алинаар нь ч орсон ажиллана
    @GetMapping({"/admin", "/admin/dashboard"})
    public String adminDashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStatistics());
        return "admin/dashboard";
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard() {
        return "redirect:/my-appointments";
    }
}