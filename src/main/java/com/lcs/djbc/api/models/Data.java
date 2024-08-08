package com.lcs.djbc.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private String idPerusahaan;
    private String namaPerusahaan;
    private String nomorInvoice;
    private String tanggalInvoice;
    private int nilaiTransaksi;
    private String kodeValuta;

}
