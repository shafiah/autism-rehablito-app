
package com.autism_rehablito_app.dto.therapist;

import java.time.LocalDateTime;

public class TherapistNoteResponse {

    private Long noteId;
    private Long childId;
    private String childName;
    private Long therapistId;
    private String therapistName;
    private Long sessionId;
    private String note;
    private LocalDateTime createdAt;

    public TherapistNoteResponse(
            Long noteId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            Long sessionId,
            String note,
            LocalDateTime createdAt) {

        this.noteId = noteId;
        this.childId = childId;
        this.childName = childName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.sessionId = sessionId;
        this.note = note;
        this.createdAt = createdAt;
    }

    public Long getNoteId() {
        return noteId;
    }

    public Long getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public Long getTherapistId() {
        return therapistId;
    }

    public String getTherapistName() {
        return therapistName;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
