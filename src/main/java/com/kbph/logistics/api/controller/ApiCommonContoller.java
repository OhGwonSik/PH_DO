package com.kbph.logistics.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.api.constant.Constants;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.COMMON_PREFIX)
public class ApiCommonContoller {
    @GetMapping("/healthz")
    public String healthz() {
        return "ok";
    }
}
