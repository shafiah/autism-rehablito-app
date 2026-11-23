package com.autism_rehablito_app.dto.therapist;

public class TherapistUnreadMessageResponse {

    private Long childId;
    private long unreadCount;

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
}