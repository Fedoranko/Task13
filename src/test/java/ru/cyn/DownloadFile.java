package ru.cyn;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.core.util.JacksonFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.*;
import ru.cyn.model.JsonParse;
import ru.cyn.model.JsonTest;
import ru.cyn.model.ParseMyJson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.name;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class DownloadFile {
    ClassLoader cl = DownloadFile.class.getClassLoader();

    //    @BeforeAll
    public static void beforeAll() {
        open("https://multikulich.cynteka.ru/core");
        String loginEmail = "yaman@cynteka.ru";
        String password = "Dec9219463833";
        $("[placeholder='Логин']").val(loginEmail);
        $("[id='password-field']").val(password);
        $(".btn.btn-primary.btn-lg").click();
        $(".user-data > h3").shouldHave(text("Снабженец Мавис"));
    }

    @Disabled
    @Test
    void downloadFile() throws IOException {
        open("https://multikulich.cynteka.ru/core/deliveries");
        File file = $("#deliveries-table > tbody > tr:nth-child(1) > td.files > a").download();
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = inputStream.readAllBytes();
            String textContent = new String(buffer, StandardCharsets.UTF_8);
            assertThat(textContent).contains("Удаление delivery_request");
        }
    }

    @Disabled
    @Test
    void uploadFile() {
        open("https://multikulich.cynteka.ru/core/orders/new/light?projectId=333");
        sleep(5000);
        $("input[type='file']").uploadFromClasspath("photo23.jpg");
        $(".name.ng-binding").shouldHave(text("photo23.jpg"));
    }

    @Disabled
    @Test
    void pdfParseTest() throws IOException {
        try (InputStream resourceInputStream = cl.getResourceAsStream("9. Ситистрой.pdf")) {
            PDF content = new PDF(resourceInputStream);
            assertThat(content.text).contains("Сантехкомплект");
        }
    }

    @Disabled
    @DisplayName("Excel")
    @Test
    void excelParseTest() throws IOException {
        try (InputStream resourceInputStream = cl.getResourceAsStream("7. Счет с картинками.xls")) {
            XLS content = new XLS(resourceInputStream);
            assertThat(content.excel.getSheetAt(0).getRow(22).getCell(1).getStringCellValue()).contains("Покупатель");
        }
    }

    @Disabled
    @DisplayName("ZIP")
    @Test
    void zipParseTest() throws IOException {
        try (InputStream resourceInputStream = cl.getResourceAsStream("104.35307792.zip");
             ZipInputStream zipInputStream = new ZipInputStream(resourceInputStream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                assertThat(entry.getName()).contains("104.35307792.xls");
            }
        }
    }

    @Disabled
    @DisplayName("JSON")
    @Test
    void jsonParseTest() throws IOException {
        Gson gson = new Gson();
        try (InputStream resourceInputStream = cl.getResourceAsStream("json.json");
             InputStreamReader reader = new InputStreamReader(resourceInputStream)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            assertThat(jsonObject.get("name").getAsString()).isEqualTo("Brendon Klocko Sr.");
        }
    }

    @DisplayName("JSON")
    @Test
    void jsonParseTest2() throws IOException {
        Gson gson = new Gson();
        try (InputStream resourceInputStream = cl.getResourceAsStream("json.json");
             InputStreamReader reader = new InputStreamReader(resourceInputStream)) {
            JsonTest jsonTest = gson.fromJson(reader, JsonTest.class);
            assertThat(jsonTest.email.home_number).isEqualTo("36326");
        }
    }

    @Disabled
    @Test
    void unzipAllFormats() throws Exception {
        Gson gson = new Gson();
        try (InputStream stream = cl.getResourceAsStream("ForTest.zip");
             ZipInputStream zipInputStream = new ZipInputStream(stream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xlsx")) {
                    XLS content = new XLS(zipInputStream);
                    assertThat(content.excel.getSheetAt(0).getRow(1).getCell(0).getStringCellValue()).isEqualTo("Маяк");
                } else if (entry.getName().endsWith(".pdf")) {
                    PDF content = new PDF(zipInputStream);
                    assertThat(content.text).contains("Кнауф");
                } else if (entry.getName().endsWith(".csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zipInputStream));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(1)[9]).isEqualTo("Filter By Creator Order on Pay Payments Page");
                } else if (entry.getName().endsWith(".json")) {
                    JsonParse content = gson.fromJson(new InputStreamReader(zipInputStream), JsonParse.class);
                    assertThat(content.members[2].name).isEqualTo("Eternal Flame");
                }
            }
        }
    }

    @Disabled
    @Test
    void parseMyJson() throws Exception {
        Gson gson = new Gson();
        try (InputStream stream = cl.getResourceAsStream("myJson.json");
             InputStreamReader reader = new InputStreamReader(stream)) {
            ParseMyJson content = gson.fromJson(reader, ParseMyJson.class);
            assertThat(content.myWork[0].address.second).isEqualTo("sampsonevskyi");
        }
    }

    @Test
    void parseMyJson2() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream stream = cl.getResourceAsStream("myJson.json");
             InputStreamReader reader = new InputStreamReader(stream)) {
            ParseMyJson content = objectMapper.readValue(reader, ParseMyJson.class);
            assertThat(content.myWork[0].address.second).isEqualTo("sampsonevskyi");
        }
    }
}
