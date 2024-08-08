package com.lcs.djbc.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lcs.djbc.api.models.RequestModels;
import com.lcs.djbc.api.models.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class ApiApplication {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static final Logger log = LogManager.getLogger(ApiApplication.class);
    private static final String name = "LCS_DJBC_";

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApiApplication.class, args);
        lds();
    }

        private static void lds() throws IOException {
            String filename = name + sdf.format(new Date());
            File file = new File("D:\\Project\\execute\\" + filename);
            file.renameTo(new File("D:\\Project\\Hasil\\" + filename + ".xlsx"));
            File path = Paths.get("D:\\Project\\Hasil\\" + filename + ".xlsx").toFile();
            if (path.exists()) {
                FileInputStream fis = new FileInputStream(new File("D:\\Project\\Hasil\\" + filename + ".xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    String kode = UUID.randomUUID() + "-" + sdf.format(new Date());
                    Cell cellNOMOR_NPWP = row.getCell(0);
                    Cell cellNAMA_PERUSAHAAN = row.getCell(1);
                    Cell cellNOMOR_INVOICE = row.getCell(2);
                    Cell cellTGL_INVOICE = row.getCell(3);
                    Cell cellJENIS_VALUTA_INVOICE = row.getCell(4);
                    Cell cellNILAI_INVOICE = row.getCell(5);
                    //=================================================
                    String npwp = cellNOMOR_NPWP.getStringCellValue();
                    String nama = cellNAMA_PERUSAHAAN.getStringCellValue();
                    String inv = cellNOMOR_INVOICE.getStringCellValue();
                    String tgl = cellTGL_INVOICE.getStringCellValue();
                    String jenis = cellJENIS_VALUTA_INVOICE.getStringCellValue();
                    String nilai = cellNILAI_INVOICE.getStringCellValue();
                    //===================================================
                    RequestModels requestModels = new RequestModels();
                    requestModels.setIdPerusahaan(npwp);
                    requestModels.setNamaPerusahaan(nama);
                    requestModels.setNomorInvoice(inv);
                    requestModels.setTanggalInvoice(tgl);
                    requestModels.setKodeValuta(jenis);
                    requestModels.setNilaiTransaksi(nilai);
                    //===================================================
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    String url = "https://apis-gw.beacukai.go.id/interchange/SendDataLcs";
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("beacukai-api-key", "ea0207a0-0d60-4f25-b77c-b5de4638f39e");
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("idPerusahaan", requestModels.getIdPerusahaan());
                    log.info(requestModels.getIdPerusahaan());
                    jsonObject.addProperty("namaPerusahaan", requestModels.getNamaPerusahaan());
                    log.info(requestModels.getNamaPerusahaan());
                    jsonObject.addProperty("nomorInvoice", requestModels.getNomorInvoice());
                    jsonObject.addProperty("tanggalInvoice", requestModels.getTanggalInvoice());
                    jsonObject.addProperty("nilaiTransaksi", requestModels.getNilaiTransaksi());
                    jsonObject.addProperty("kodeValuta", requestModels.getKodeValuta());
                    HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
                    Response mo = restTemplate.postForEntity(url, request, Response.class).getBody();
                    //===================================================
                    FileOutputStream fout = new FileOutputStream("D:\\Project\\Sukses\\" + kode + ".txt");
                    String out = new Gson().toJson(mo);
                    byte er[] = out.getBytes();
                    //converting string into byte array
                    fout.write(er);
                    fout.close();
                    //=============================
                    System.out.println("BERHASIL");
                }
            } else {
                System.out.println("file tidak ditemukan");
            }
        }

        public final static int BUF_SIZE = 1024; 


            public static void createorrename(int type, File in, File out) throws Exception {
                FileInputStream fis  = new FileInputStream(in);
                FileOutputStream fos = new FileOutputStream(out);
                try {
                    byte[] buf = new byte[BUF_SIZE];
                    int i = 0;
                    while ((i = fis.read(buf)) != -1) {
                        switch(type){
                            case 0:
                             fos.write(buf, 0, i);
                             break;
                            case 1:
                             fos.renameTo(out);
                             break;
                        }
                    }
                } 
                catch (Exception e) {
                    throw e;
                }
                finally {
                    if (fis != null) fis.close();
                    if (fos != null) fos.close();
                }
            }
    }