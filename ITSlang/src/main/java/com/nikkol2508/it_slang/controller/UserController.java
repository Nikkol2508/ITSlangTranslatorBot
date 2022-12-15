package com.nikkol2508.it_slang.controller;

import com.nikkol2508.it_slang.dto.FormData;
import com.nikkol2508.it_slang.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final AdminService adminService;

    public UserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String mainPage(@ModelAttribute("formData") FormData formData, Model model) {
        model.addAttribute("formData", formData);
        return "index";
    }

    @PostMapping("/descriptions")
    public String getDescriptions(@ModelAttribute(value = "formData") @RequestBody FormData formData, Model model) {
        List<String> descriptionList = adminService.getDescriptions(formData.getQuery());
        model.addAttribute("descriptionList", descriptionList);
        return "index";
    }
}
