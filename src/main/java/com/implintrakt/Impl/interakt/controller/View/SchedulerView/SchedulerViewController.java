package com.implintrakt.Impl.interakt.controller.View.SchedulerView;

import com.implintrakt.Impl.interakt.repository.ContactDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SchedulerViewController {

    @Autowired
    private ContactDetailRepository contactDetailRepository;

    @GetMapping()
    public ModelAndView schedulerView() {
        ModelAndView modelAndView = new ModelAndView();
        List<String> status = contactDetailRepository.getStatus();
        modelAndView.addObject("status", status);
        modelAndView.setViewName("schedulerView/scheduler");
        return modelAndView;
    }
}
