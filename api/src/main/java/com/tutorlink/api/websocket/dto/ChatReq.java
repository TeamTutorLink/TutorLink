package com.tutorlink.api.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatReq {
    int lessonId;
    String userName;
    String content;
}
