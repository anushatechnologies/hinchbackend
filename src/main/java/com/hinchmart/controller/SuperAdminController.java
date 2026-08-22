package com.hinchmart.controller;

import com.hinchmart.dto.response.ApiResponse;
import com.hinchmart.dto.response.UserDto;
import com.hinchmart.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Super Admin Operations",
        description = "Endpoints available only to SUPER_ADMIN"
)
public class SuperAdminController {

    private final UserService userService;

    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/users/{userId}/make-admin")
    @Operation(
            summary = "Promote User to Admin",
            description = "Promotes a specific user to ADMIN. Only SUPER_ADMIN can perform this operation."
    )
    public ResponseEntity<ApiResponse<UserDto>> makeUserAdmin(
            @PathVariable Long userId) {

        UserDto updatedUser = userService.makeUserAdmin(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User promoted to ADMIN successfully",
                        updatedUser
                )
        );
    }

    @GetMapping("/super-admins/emails")
    @Operation(
            summary = "Get Super Admin Emails",
            description = "Returns the email addresses of all SUPER_ADMIN users."
    )
    public ResponseEntity<ApiResponse<List<String>>> getSuperAdminEmails() {

        List<String> emails = userService.getSuperAdminEmails();

        return ResponseEntity.ok(
                ApiResponse.success(emails)
        );
    }
}