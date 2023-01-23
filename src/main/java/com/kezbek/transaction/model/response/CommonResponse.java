package com.kezbek.transaction.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CommonResponse {
    private String status;
    private String message;

    public CommonResponse() {
        this.status="200";
        this.message="success";
    }
}