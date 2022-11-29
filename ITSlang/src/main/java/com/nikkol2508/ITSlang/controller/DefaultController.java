package com.nikkol2508.ITSlang.controller;

import com.nikkol2508.ITSlang.dto.FormData;
import com.nikkol2508.ITSlang.dto.NotFoundResult;
import com.nikkol2508.ITSlang.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
public class DefaultController {

    private final AdminService adminService;

    public DefaultController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/")
    public String index(@ModelAttribute("formData") FormData formData, Model model) {

                         ArrayList<NotFoundResult> queryList = (ArrayList<NotFoundResult>) adminService.getNotFoundResultList()
                .stream()
                .map(e -> new NotFoundResult(e.getNotFoundQuery(), e.getCountQuery()))
                .collect(Collectors.toList());

        model.addAttribute("queryList", queryList);
        model.addAttribute("totalCount", queryList.size());
        model.addAttribute("formData", formData);

        return "index";
    }
}
