
package com.autism_rehablito_app.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminMessageRequest {

    @NotNull(message = "Receiver user id is required")
    private Long receiverUserId;

    /*
     * Required when Admin sends a message to Parent.
     * Identifies the specific Therapist conversation.
     */
    private Long conversationTherapistUserId;

    @NotBlank(message = "Message is required")
    @Size(max = 3000, message = "Message cannot exceed 3000 characters")
    private String message;

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(Long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public Long getConversationTherapistUserId() {
        return conversationTherapistUserId;
    }

    public void setConversationTherapistUserId(
            Long conversationTherapistUserId) {
        this.conversationTherapistUserId = conversationTherapistUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
