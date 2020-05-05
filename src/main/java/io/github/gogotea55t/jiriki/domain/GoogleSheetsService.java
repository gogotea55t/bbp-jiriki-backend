package io.github.gogotea55t.jiriki.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchGet;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class GoogleSheetsService {
  /** Global instance of the {@link FileDataStoreFactory}. */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  private static String APPLICATION_NAME = "jiriki";

  private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static HttpTransport HTTP_TRANSPORT;

  private static final List<String> SCOPES =
      Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
  /** Directory to store user credentials for this application. */
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(
          System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  private GoogleSpreadSheetConfig sheetConfig;

  @Autowired
  public GoogleSheetsService(GoogleSpreadSheetConfig config) {
    this.sheetConfig = config;
  }

  public Credential authorize() throws IOException {
    InputStream in = JirikiService.class.getResourceAsStream("/client_secret.json");
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  public Sheets getSheetsService() throws IOException {
    Credential credential = authorize();
    return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  public List<ValueRange> getValuesFromSpreadSheet() throws IOException {
    Sheets service = getSheetsService();

    String spreadSheetId = sheetConfig.getId();
    String spreadSheetName = sheetConfig.getName();

    BatchGet request = service.spreadsheets().values().batchGet(spreadSheetId);

    List<String> ranges = new ArrayList<>();

    // ユーザー定義
    ranges.add(spreadSheetName + "!L3:4");

    // 楽曲パート情報
    ranges.add(spreadSheetName + "!A5:K");

    // 得点
    ranges.add(spreadSheetName + "!K3:2100");

    request.setRanges(ranges);

    BatchGetValuesResponse response = request.execute();

    return response.getValueRanges();
  }
}
