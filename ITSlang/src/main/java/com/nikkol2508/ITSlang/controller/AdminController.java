package com.nikkol2508.ITSlang.controller;

import com.nikkol2508.ITSlang.dto.FormData;
import com.nikkol2508.ITSlang.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/descriptions")
    public String addQuery(@ModelAttribute(value = "formData") @RequestBody FormData formData) {
        adminService.saveToSlangTranslator(formData);
        adminService.deleteFromNotFound(formData);
        return "redirect:/";
    }

    @PostMapping("/queries")
    public String deleteAllQueries() {
        adminService.deleteAllFromNotFound();
        return "redirect:/";
    }
}
