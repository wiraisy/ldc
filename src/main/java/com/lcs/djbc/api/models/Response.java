package com.lcs.djbc.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class Response {
    public String idPerusahaan;
    public int statusResponse;
    public String description;
    public String data;

}
