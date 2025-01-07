package org.koreait.message.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.file.constants.FileStatus;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.message.entities.Message;
import org.koreait.message.services.MessageDeleteService;
import org.koreait.message.services.MessageInfoService;
import org.koreait.message.services.MessageSendService;
import org.koreait.message.services.MessageStatusService;
import org.koreait.message.validators.MessageValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private final MessageDeleteService deleteService;
    private final ObjectMapper om;

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
    public String process(@Valid RequestMessage form, Errors errors, Model model, HttpServletRequest request) {
        commonProcess("send", model);

        messageValidator.validate(form, errors);

        if (errors.hasErrors()) {
            // 업로드한 파일 목록 form에 추가
            String gid = form.getGid();
            form.setEditorImages(fileInfoService.getList(gid, "editor", FileStatus.ALL));
            form.setAttachFiles(fileInfoService.getList(gid, "attach", FileStatus.ALL));

            return utils.tpl("message/form");
        }

        Message message = sendService.process(form);
        long totalUnRead = infoService.totalUnRead(form.getEmail());
        Map<String, Object> data = new HashMap<>();
        data.put("item", message);
        data.put("totalUnRead", totalUnRead);

        StringBuffer sb = new StringBuffer();

        try {
            String json = om.writeValueAsString(data);
            sb.append(String.format("if (typeof webSocket != undefined) { webSocket.onopen = () => webSocket.send('%s'); }", json));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sb.append(String.format("location.replace('%s');",request.getContextPath() + "/message/list"));

        model.addAttribute("script", sb.toString());

        return "common/_execute_script";
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
    public String view(@PathVariable("seq") Long seq, Model model, HttpServletRequest request) {
        commonProcess("view", model);

        Message item = infoService.get(seq);
        model.addAttribute("item", item);

        statusService.change(seq); // 열람 상태로 변경

        String referer = Objects.requireNonNullElse(request.getHeader("referer"),"");
        model.addAttribute("mode", referer.contains("mode=send") ? "send":"receive");

        return utils.tpl("message/view");
    }

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, @RequestParam(name="mode", defaultValue = "receive") String mode) {

        deleteService.process(seq, mode);

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
