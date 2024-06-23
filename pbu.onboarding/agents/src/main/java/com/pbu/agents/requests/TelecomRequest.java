package com.pbu.agents.requests;

public class TelecomRequest {
    private long id;
    private String telecomName;
    private boolean isDeleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTelecomName() {
        return telecomName;
    }

    public void setTelecomName(String telecomName) {
        this.telecomName = telecomName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
