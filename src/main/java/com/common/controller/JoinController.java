package com.common.controller;

import com.api.user.entity.AccountEntity;
import com.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
public class JoinController {
    private final UserService userService;

    //회원가입
    @PostMapping("/api/join")
    public ModelAndView join(
            AccountEntity accountEntity,
            RedirectAttributes redirectAttributes
    ) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            userService.join(accountEntity);
            redirectAttributes.addFlashAttribute("message", "회원가입 성공");
            modelAndView.setViewName("redirect:/login?success");
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            modelAndView.setViewName("redirect:/join?error");
        }

        return modelAndView;
    }
}
