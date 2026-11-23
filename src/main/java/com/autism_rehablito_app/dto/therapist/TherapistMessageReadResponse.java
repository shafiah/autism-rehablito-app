package com.autism_rehablito_app.dto.therapist;

public class TherapistMessageReadResponse {

    private Long messageId;
    private Boolean read;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}