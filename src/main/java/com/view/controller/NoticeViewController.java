package com.view.controller;

import com.api.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NoticeViewController {
    private final NoticeService noticeService;

    @GetMapping("/notice")
    public String notice(
    ) throws Exception{
        return "notice/notice";
    }

    @GetMapping("/notice/createNotice")
    public String registerNotice(
    ) throws Exception{
        return "notice/createNotice";
    }

    @GetMapping("/notice/detailNotice/{noticeNo}")
    public String detailNotice(
            @PathVariable int noticeNo,
            Model model
    ){
        model.addAttribute("noticeNo", noticeNo);
        return "notice/detailNotice";
    }

    @GetMapping("/notice/modifyNotice/{noticeNo}")
    public String modifyNotice(
            @PathVariable int noticeNo,
            Model model
    ) throws Exception{
        model.addAttribute("noticeNo", noticeNo);
        return "notice/modifyNotice";
    }
}
