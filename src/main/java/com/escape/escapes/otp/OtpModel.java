package com.escape.escapes.otp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.escape.escapes.admin.model.Admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "otpCode", "expirationTime", "userInfo"})
public class OtpModel {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("otpCode")
    private String otpCode;

    @JsonProperty("expirationTime")
    private Long expirationTime;

    @JsonProperty("userInfo")
    private Admin user;

}
