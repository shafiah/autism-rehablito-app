package com.autism_rehablito_app.dto.parent;

import jakarta.validation.constraints.NotNull;

public class ParentMessageReadRequest {

    @NotNull(message = "Message ID is required")
    private Long messageId;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}