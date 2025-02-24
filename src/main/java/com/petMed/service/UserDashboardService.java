package com.petMed.service;

import com.petMed.model.enums.Role;
import com.petMed.security.CurrentUser;
import org.springframework.stereotype.Service;

@Service
public class UserDashboardService {

    public String getDashboardView(CurrentUser currentUser) {
        if (currentUser.getRole() == Role.VET) {
            return "vet-owner-home";
        } else if (currentUser.getRole() == Role.ADMIN) {
            return "admin-home";
        }
        return "index";
    }
}
