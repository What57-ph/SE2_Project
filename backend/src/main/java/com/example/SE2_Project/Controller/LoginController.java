package com.example.SE2_Project.Controller;

import com.example.SE2_Project.Dto.AuthenticationRequest;
import com.example.SE2_Project.Service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(value = "")
public class LoginController {

    @Autowired
    private LoginService loginService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        try {
            // Gọi service login và xử lý chuyển hướng
            String redirectUrl = loginService.loginAndRedirect(request);

            // Thực hiện chuyển hướng trực tiếp
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Redirecting to: " + redirectUrl);
        } catch (IllegalArgumentException e) {
            // Nếu thông tin không chính xác, trả về lỗi
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during redirect: " + e.getMessage());
        }
    }

    @GetMapping(value = "/process-after-login")
    public String processAfterLoginController(){
        return loginService.processAfterLogin();
    }

    @GetMapping("/report")
    public String reportPage() {
        return "report/report";
    }
    @GetMapping("/homepage")
    public String homepage() {
        return "expenses/homepage";
    }

}
