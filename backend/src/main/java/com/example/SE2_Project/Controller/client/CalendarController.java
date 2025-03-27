package com.example.SE2_Project.Controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {
    @GetMapping("/calendar")
    public String getCalendarPage(){
        return "client/calendar/calendar";
    }

}
