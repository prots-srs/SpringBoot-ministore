package com.protsprog.ministore.models;

public record Credentials(
        String email,
        String phone) {
    public String getEmailLink() {
        return "<a href=\"mailto:" + email + "\">" + email + "</a>";
    }

    public String getPhoneLink() {
        return "<a href=\"tel:" + phone + "\">" + phone + "</a>";
    }
}
