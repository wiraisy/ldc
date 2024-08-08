package com.lcs.djbc.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lcs.djbc.api.models.RequestModels;
import com.lcs.djbc.api.models.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class ApiApplication {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static final Logger log = LogManager.getLogger(ApiApplication.class);
    private static final String name = "LCS_DJBC_";

    // PATH =================================
    // SOURCE PATH
    private static final String source = "D:\\AKBAR WIRAISY\\workspace2\\Project\\execute\\";
    // BACKUP PATH
    private static final String backup = "D:\\AKBAR WIRAISY\\workspace2\\Project\\Hasil\\BACKUP2\\";
    // READ PATH
    private static final String read = "D:\\AKBAR WIRAISY\\workspace2\\Project\\Hasil\\BACKUP\\";
    // OUTPUT PATH
    private static final String output = "D:\\AKBAR WIRAISY\\workspace2\\Project\\Hasil\\";

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApiApplication.class, args);
        lds();
    }

    private static void lds() throws IOException {
        //connection test
        if(!testkoneksi()){
            System.out.println("Koneksi Bermasalah atau Endpoint Bermasalah");
        }else {
            //define the path source
            String filename = name + sdf.format(new Date());
            File file = new File(source + filename);
            if(file.exists()){
                //back up file path
                file.renameTo(new File(backup + filename + ".xlsx"));
                //copy file from backup to read folder
                copyFile(new File(backup+filename+".xlsx"), new File(read+filename+".xlsx"));
            }
            //file to read after
            File path = Paths.get(read+filename+".xlsx").toFile();
            if (path.exists()) {
                FileInputStream fis = new FileInputStream(path);
                XSSFWorkbook workbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    String kode = UUID.randomUUID() + "-" + sdf.format(new Date());
                    //===================================================
                    //filling the model value
                    RequestModels requestModels = new RequestModels();
                    requestModels.setIdPerusahaan(row.getCell(0).getStringCellValue());
                    requestModels.setNamaPerusahaan(row.getCell(1).getStringCellValue());
                    requestModels.setNomorInvoice(row.getCell(2).getStringCellValue());
                    requestModels.setTanggalInvoice(row.getCell(3).getStringCellValue());
                    requestModels.setKodeValuta(row.getCell(4).getStringCellValue());
                    requestModels.setNilaiTransaksi(row.getCell(5).getStringCellValue());
                    //===================================================
                    //define object mapper
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    String url = "https://apis-gw.beacukai.go.id/interchange/SendDataLcs";
                    RestTemplate restTemplate = new RestTemplate();
                    // define header security
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("beacukai-api-key", "ea0207a0-0d60-4f25-b77c-b5de4638f39e");
                    //define json object
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("idPerusahaan", requestModels.getIdPerusahaan());
                    log.info(requestModels.getIdPerusahaan());
                    jsonObject.addProperty("namaPerusahaan", requestModels.getNamaPerusahaan());
                    log.info(requestModels.getNamaPerusahaan());
                    jsonObject.addProperty("nomorInvoice", requestModels.getNomorInvoice());
                    jsonObject.addProperty("tanggalInvoice", requestModels.getTanggalInvoice());
                    jsonObject.addProperty("nilaiTransaksi", requestModels.getNilaiTransaksi());
                    jsonObject.addProperty("kodeValuta", requestModels.getKodeValuta());
                    //combine json object with hader
                    HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
                    //catch response from post api and mapping to response model
                    Response mo = restTemplate.postForEntity(url, request, Response.class).getBody();
                    //===================================================
                    //devine the output stream
                    FileOutputStream fout = new FileOutputStream( output + kode + ".txt");
                    //convert json from djbc api response to String
                    String out = new Gson().toJson(mo);
                    //converting string into byte array
                    byte er[] = out.getBytes();
                    //write byte to text file
                    fout.write(er);
                    //close buffer stream
                    fout.close();
                    //=============================
                    System.out.println("BERHASIL");
                }
                file.delete();
                path.delete();
            } else {
                System.out.println("file tidak ditemukan");
            }
        }
    }


    //connection test method
    public static Boolean testkoneksi() {
        try {
            URL url = new URL("https://apis-gw.beacukai.go.id/interchange/SendDataLcs");
            URLConnection connection = url.openConnection();
            connection.connect();
            System.out.println("Internet is connected");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public final static int BUF_SIZE = 1024;

    //copy file method
    public static void copyFile(File in, File out) throws IOException {
        try (FileInputStream fis = new FileInputStream(in); FileOutputStream fos = new FileOutputStream(out)) {
            byte[] buf = new byte[BUF_SIZE];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        }
    }

}