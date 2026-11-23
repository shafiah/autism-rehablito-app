package com.autism_rehablito_app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "communication_messages")
public class CommunicationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Child context for the conversation.
     * Every communication belongs to a specific child.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    /*
     * User who sent the message.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User sender;

    /*
     * User who receives the message.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiver;
    
    /*
     * Identifies the specific Therapist conversation.
     * Parent + selected Therapist + Admin share this conversation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_therapist_user_id")
    private User conversationTherapist;

    @Column(nullable = false, length = 3000)
    private String message;

    /*
     * false = unread
     * true  = read
     */
    @Column(name = "is_read", nullable = false)
    private Boolean read = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        if (read == null) {
            read = false;
        }

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
    public User getConversationTherapist() {
        return conversationTherapist;
    }

    public void setConversationTherapist(User conversationTherapist) {
        this.conversationTherapist = conversationTherapist;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}