package com.qpros.helpers.yopmail;

import lombok.Data;

@Data
public class EmailModel {
    private String account;
    private String url;
    private String subject;
    private String sender;
}
