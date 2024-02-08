package com.example.onlinecoursesplatform.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    private static final String LOGIN_PATH = "/login";
    private static final String ROOT_PATH = "/";
    private static final String ADMIN_DASHBOARD_PATH = "/admin-dashboard";
    private static final String USER_DASHBOARD_PATH = "/user-dashboard";
    private static final String ABOUT_US_PATH = "/aboutUs";

    @GetMapping(LOGIN_PATH)
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:" + ROOT_PATH;
    }

    @GetMapping(ROOT_PATH)
    public String defaultDashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("Administrator"))) {
            return "redirect:" + ADMIN_DASHBOARD_PATH;
        } else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("SimpleUser"))) {
            return "redirect:" + USER_DASHBOARD_PATH;
        } else {
            return "redirect:" + ROOT_PATH;
        }
    }

    @GetMapping(ADMIN_DASHBOARD_PATH)
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping(USER_DASHBOARD_PATH)
    public String userDashboard() {
        return "user-dashboard";
      }

    @GetMapping(ABOUT_US_PATH)
    public String aboutUsPage() {
        return "aboutUs";
    }

}


