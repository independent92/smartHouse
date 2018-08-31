package ru.smart.smartHouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.smart.smartHouse.entity.Account;
import ru.smart.smartHouse.service.AccountService;
import ru.smart.smartHouse.service.SecurityService;

@Controller
@RequestMapping(value = "")
public class WelcomeController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/login")
    public String login() {
        return "welcome/login";
    }

    @RequestMapping(value = "/registration")
    public String registration() {
        return "welcome/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String saveAccount(@ModelAttribute("user") Account account) {
        accountService.save(account);
        securityService.autoLogin(account.getName(), account.getPassword());
        return "redirect:/login";
    }

    @Secured("usero")
    @RequestMapping(value = "/secured")
    public String securedMethod() {
        return  "welcome/secured";
    }

    @RequestMapping(value = "/homepage")
    public ModelAndView homepage() {
        ModelAndView view = new ModelAndView();
        view.addObject("accountName", securityService.findLoggedInUsername());
        view.setViewName("welcome/homepage");

        return view;
    }
}
