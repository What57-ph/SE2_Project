package com.example.SE2_Project.Controller;

import com.example.SE2_Project.Dto.AuthenticationRequest;
import com.example.SE2_Project.Security.SecurityUtils;
import com.example.SE2_Project.Service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "")
public class LoginController {

    @Autowired
    private LoginService loginService;


//
//    @PostMapping("/process-login")
//    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
//        try {
//            // Gọi service login và xử lý chuyển hướng
//            String redirectUrl = loginService.loginAndRedirect(request);
//
//            // Thực hiện chuyển hướng trực tiếp
//            response.sendRedirect(redirectUrl);
//            SecurityUtils.printCurrentUserInfo();
//            return ResponseEntity.ok("Redirecting to: " + redirectUrl);
//        } catch (IllegalArgumentException e) {
//            // Nếu thông tin không chính xác, trả về lỗi
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during redirect: " + e.getMessage());
//        }
//    }






}
