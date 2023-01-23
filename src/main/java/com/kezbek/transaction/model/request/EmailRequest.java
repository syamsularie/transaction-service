package com.kezbek.transaction.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest {
    private String destinations[];
    private String from;
    private String cc;

    private String bcc;
    private String content;
    private String Subject;
}
