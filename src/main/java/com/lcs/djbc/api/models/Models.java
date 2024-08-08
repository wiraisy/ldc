package com.lcs.djbc.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Models {
  //  @JsonProperty(value = "tester")
    private String idPerusahaan;
    private int statusResponse;
    private String description;
    private Data data;
}
