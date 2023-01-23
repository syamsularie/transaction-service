package com.kezbek.transaction.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionResponse {

    private String subId;
    private String company;
    private String companyCode;
    private String token;
    private String accessToken;
    private String refreshToken;

}
