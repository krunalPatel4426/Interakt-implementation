package com.implintrakt.Impl.interakt.controller.View.ContactView;


import com.implintrakt.Impl.interakt.dto.AddContactDetailDto.AddContactDetailDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/contact")
public class ContactViewController {
    @GetMapping("/add")
    public ModelAndView addContact(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ContactView/addContact");
        return modelAndView;
    }
}
