package org.koreait.message.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.file.constants.FileStatus;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.message.entities.Message;
import org.koreait.message.services.MessageInfoService;
import org.koreait.message.services.MessageSendService;
import org.koreait.message.services.MessageStatusService;
import org.koreait.message.validators.MessageValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@ApplyErrorPage
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final Utils utils;
    private final MessageValidator messageValidator;
    private final FileInfoService fileInfoService;
    private final MessageSendService sendService;
    private final MessageInfoService infoService;
    private final MessageStatusService statusService;

    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("message/style");
    }

    /**
     * 쪽지 작성 양식
     *
     * @return
     */
    @GetMapping
    public String form(@ModelAttribute RequestMessage form, Model model) {
        commonProcess("send", model);

        form.setGid(UUID.randomUUID().toString());

        return utils.tpl("message/form");
    }

    /**
     * 쪽지 작성
     *
     * @return
     */
    @PostMapping
    public String process(@Valid RequestMessage form, Errors errors, Model model) {
        commonProcess("send", model);

        messageValidator.validate(form, errors);

        if (errors.hasErrors()) {
            // 업로드한 파일 목록 form에 추가
            String gid = form.getGid();
            form.setEditorImages(fileInfoService.getList(gid, "editor", FileStatus.ALL));
            form.setAttachFiles(fileInfoService.getList(gid, "attach", FileStatus.ALL));

            return utils.tpl("message/form");
        }

        sendService.process(form);

        return "redirect:/message/list";
    }

    /**
     * 보낸거나 받은 쪽지 목록
     *
     * @return
     */
    @GetMapping("/list")
    public String list(@ModelAttribute MessageSearch search, Model model) {
        commonProcess("list", model);
        String mode = search.getMode();
        search.setMode(StringUtils.hasText(mode) ? mode : "receive");

        ListData<Message> data = infoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("message/list");
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model) {
        commonProcess("view", model);

        Message item = infoService.get(seq);
        model.addAttribute("item", item);

        statusService.change(seq); // 열람 상태로 변경

        return utils.tpl("message/view");
    }

    @DeleteMapping
    public String delete(@RequestParam(name="seq", required = false) List<String> seq) {

        return "redirect:/message/list";
    }

    /**
     * 컨트롤러 공통 처리
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";
        String pageTitle = "";
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if (mode.equals("send")) { // 쪽지 보내기
            pageTitle = utils.getMessage("쪽지_보내기");
            addCommonScript.add("fileManager");
            addCommonScript.add("ckeditor5/ckeditor");
            addScript.add("message/send");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }
}
