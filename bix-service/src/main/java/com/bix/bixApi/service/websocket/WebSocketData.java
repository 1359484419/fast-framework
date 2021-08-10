package com.bix.bixApi.service.websocket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebSocketData {
    private String message;
    private Object data;
}