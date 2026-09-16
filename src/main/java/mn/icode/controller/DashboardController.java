package mn.icode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    @ResponseBody
    public String adminDashboard() {
        return "<h1>Admin Dashboard (Access Granted)</h1>";
    }

    @GetMapping("/customer/dashboard")
    @ResponseBody
    public String customerDashboard() {
        return "<h1>Customer Dashboard (Access Granted)</h1>";
    }
}