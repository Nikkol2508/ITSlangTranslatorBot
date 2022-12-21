package com.nikkol2508.it_slang.controller;

import com.nikkol2508.it_slang.dto.FormData;
import com.nikkol2508.it_slang.dto.NotFoundResult;
import com.nikkol2508.it_slang.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String index(@ModelAttribute("formData") FormData formData, Model model) {

        ArrayList<NotFoundResult> queryList = (ArrayList<NotFoundResult>) adminService.getNotFoundResultList()
                .stream()
                .map(e -> new NotFoundResult(e.getNotFoundQuery(), e.getCountQuery()))
                .collect(Collectors.toList());

        model.addAttribute("queryList", queryList);
        model.addAttribute("totalCount", queryList.size());
        model.addAttribute("formData", formData);

        return "admin";
    }

    @PostMapping("/descriptions")
    public String addQuery(@ModelAttribute(value = "formData") @RequestBody FormData formData) {
        adminService.saveToSlangTranslator(formData);
        adminService.deleteFromNotFound(formData);
        return "redirect:/admin/";
    }

    @PostMapping("/queries")
    public String deleteAllQueries(@ModelAttribute(value = "formData") @RequestBody FormData formData) {
        adminService.deleteAllFromNotFound();
        return "redirect:/admin/";
    }
}
