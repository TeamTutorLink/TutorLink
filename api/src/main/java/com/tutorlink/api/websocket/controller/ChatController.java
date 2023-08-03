package com.tutorlink.api.websocket.controller;

import com.tutorlink.api.websocket.dto.ChatReq;
import com.tutorlink.api.websocket.dto.ChatRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations mso;

    @MessageMapping("/chat")
    public void chat(@RequestBody ChatReq chatReq) {
        log.info("[{}] {}: {}", chatReq.getLessonId(), chatReq.getUserName(), chatReq.getContent());
        ChatRes chatRes = new ChatRes();
        BeanUtils.copyProperties(chatReq, chatRes);

        mso.convertAndSend("/sub/" + chatReq.getLessonId(), chatRes);
    }
}
