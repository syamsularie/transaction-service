package com.kezbek.transaction.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerRequest {
    @NotBlank
    private String partnerCode;
    @NotBlank
    private String name;
    private String description;
    private String category;
}
