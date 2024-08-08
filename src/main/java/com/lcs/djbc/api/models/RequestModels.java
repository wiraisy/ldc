package com.lcs.djbc.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestModels {
    private String idPerusahaan;
    private String namaPerusahaan;
    private String nomorInvoice;
    private String tanggalInvoice;
    private String nilaiTransaksi;
    private String kodeValuta;
    private String LCS_NEGARA_MITRA;



}
