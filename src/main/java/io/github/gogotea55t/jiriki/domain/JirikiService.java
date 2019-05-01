package io.github.gogotea55t.jiriki.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchGet;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;

@Service
public class JirikiService {
  private GoogleSpreadSheetConfig sheetConfig;

  private UserRepository userRepository;

  private SongRepository songRepository;

  private ScoresRepository scoreRepository;

  /** Directory to store user credentials for this application. */
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(
          System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

  /** Global instance of the {@link FileDataStoreFactory}. */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  private static String APPLICATION_NAME = "jiriki";

  private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static HttpTransport HTTP_TRANSPORT;

  private static final List<String> SCOPES =
      Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  @Autowired
  public JirikiService(
      GoogleSpreadSheetConfig sheetConfig,
      UserRepository userRepository,
      SongRepository songRepository,
      ScoresRepository scoreRepository) {
    this.sheetConfig = sheetConfig;
    this.userRepository = userRepository;
    this.songRepository = songRepository;
    this.scoreRepository = scoreRepository;
  }

  public static Credential authorize() throws IOException {
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

  public static Sheets getSheetsService() throws IOException {
    Credential credential = authorize();
    return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  //	@Transactional
  public void doGet() {
    try {
      userRepository.deleteAll();
      scoreRepository.deleteAll();
      songRepository.deleteAll();

      Sheets service = JirikiService.getSheetsService();

      String spreadSheetId = sheetConfig.getId();
      String spreadSheetName = sheetConfig.getName();

      BatchGet request = service.spreadsheets().values().batchGet(spreadSheetId);

      List<String> ranges = new ArrayList<>();

      // ユーザー定義
      ranges.add(spreadSheetName + "!L3:4");

      // 楽曲パート情報
      ranges.add(spreadSheetName + "!A5:K");

      // 得点
      ranges.add(spreadSheetName + "!K3:1495");

      request.setRanges(ranges);

      BatchGetValuesResponse response = request.execute();

      List<ValueRange> respList = response.getValueRanges();

      // **************************
      // アカウント情報の取得
      // **************************
      List<List<Object>> userRows = respList.get(0).getValues();

      List<Object> userId = userRows.get(0);
      List<Object> userName = userRows.get(1);

      Map<String, Users> users = new HashMap<String, Users>();

      for (int i = 0; i < userId.size(); i++) {
        if (userId.get(i) == null || userName.get(i) == null) {
          continue;
        }
        Users user = new Users();

        String userIdStr = userId.get(i).toString();

        user.setUserId(userIdStr);
        user.setUserName(userName.get(i).toString());
        users.put(userIdStr, user);
      }

      userRepository.saveAll(users.values());

      // **************************
      // 楽曲情報の取得
      // **************************
      List<List<Object>> songRows = respList.get(1).getValues();

      Map<String, Songs> songs = new HashMap<String, Songs>();

      for (List<Object> songInfo : songRows) {
        if (songInfo.size() != 11
            || songInfo.get(1) == null
            || songInfo.get(1).toString().equals("")) {
          continue;
        }

        JirikiRank jirikiRank = JirikiRank.getJirikiRankFromRankName(songInfo.get(0).toString());

        Songs song =
            Songs.of(
                songInfo.get(10).toString(),
                jirikiRank,
                songInfo.get(1).toString(),
                songInfo.get(2).toString(),
                songInfo.get(3).toString());
        songs.put(songInfo.get(10).toString(), song);
      }

      songRepository.saveAll(songs.values());

      // **************************
      // 成績情報の取得
      // **************************

      List<List<Object>> scoreRows = respList.get(2).getValues();

      List<Scores> scores = new ArrayList<>();

      Pattern p = Pattern.compile("^\\d{2}$");

      // 空白行の数
      int k = 0;

      for (int i = 2; i < scoreRows.size(); i++) {
        List<Object> scoreRow = scoreRows.get(i);

        // 完全に空白になっている行はスキップする（もっとやりようがある気がする）
        if (scoreRow.size() == 0
            || scoreRow.get(0) == null
            || scoreRow.get(0).toString().equals("")) {
          k++;
          continue;
        }

        Songs thisSong = songs.get(scoreRow.get(0));

        // 最後から二番目の列はユーザーに紐づかないソート用の列であるため除外
        for (int j = 1; j < scoreRow.size() - 2; j++) {
          String scoreCol = scoreRow.get(j).toString();
          try {

            // 条件は「空欄じゃない」かつ「0~100までの数字」
            if (scoreCol != null && ("100".equals(scoreCol) || p.matcher(scoreCol).find())) {
              Scores score = new Scores();

              score.setSongs(thisSong);
              score.setUsers(users.get(scoreRows.get(0).get(j).toString()));
              score.setScore(Integer.parseInt(scoreCol));

              scores.add(score);
            }
          } catch (Exception e) {
            System.out.println(scoreRow);
            continue;
          }
        }
      }

      scoreRepository.saveAll(scores);

    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  /**
   * 全プレイヤーの情報を取得し、コントローラーに返却する。
   *
   * @return プレイヤー情報の配列
   */
  public List<UserResponse> getAllPlayer() {
    List<UserResponse> players = new ArrayList<>();
    List<Users> users = userRepository.findAll(Sort.by(Order.asc("userId")));
    for (Users user : users) {
      players.add(UserResponse.of(user));
    }
    return players;
  }

  /**
   * プレイヤーを名前で検索し、該当するユーザーの情報を返す。 ユーザーは一人を想定しているが、同名のプレイヤーがいた場合を想定して念のため配列にしている
   *
   * @param name
   * @return
   */
  public List<UserResponse> getPlayerByName(String name) {
    List<UserResponse> players = new ArrayList<>();
    List<Users> users = userRepository.findByUserNameLike(name);
    for (Users user : users) {
      players.add(UserResponse.of(user));
    }

    return players;
  }

  public UserResponse getPlayerById(String id) {
    Optional<Users> response = userRepository.findById(id);
    if (response.isPresent()) {
      return UserResponse.of(response.get());
    } else {
      return null;
    }
  }

  public SongsResponse getSongBySongId(String songId) {
    Optional<Songs> response = songRepository.findById(songId);
    if (response.isPresent()) {
      return SongsResponse.of(response.get());
    } else {
      return null;
    }
  }

  public List<SongsResponse> getSongBySongName(String songName, Pageable page) {
    List<SongsResponse> songs = new ArrayList<>();
    List<Songs> songsResponse = songRepository.findBySongNameContaining(songName, page);
    songsResponse.forEach(
        s -> {
          songs.add(SongsResponse.of(s));
        });
    return songs;
  }

  public List<SongsResponse> getSongByContributor(String contributor, Pageable page) {
    List<SongsResponse> songs = new ArrayList<>();
    List<Songs> songsResponse = songRepository.findByContributorContaining(contributor, page);
    songsResponse.forEach(
        s -> {
          songs.add(SongsResponse.of(s));
        });
    return songs;
  }

  public List<SongsResponse> getSongByInstrument(String instrument, Pageable page) {
    List<SongsResponse> songs = new ArrayList<>();
    List<Songs> songsResponse = songRepository.findByInstrumentContaining(instrument, page);
    songsResponse.forEach(
        s -> {
          songs.add(SongsResponse.of(s));
        });
    return songs;
  }

  public List<Score4SongResponse> getScoresBySongId(String songId) {
    Optional<Songs> song = songRepository.findById(songId);
    if (song.isPresent()) {
      List<Score4SongResponse> response = new ArrayList<>();
      song.get()
          .getScores()
          .stream()
          .forEach(
              (sc) -> {
                response.add(Score4SongResponse.of(sc));
              });
      return response;
    } else {
      return null;
    }
  }

  public List<Score4UserResponse> getScoresByUserId(String userId) {
    Optional<Users> user = userRepository.findById(userId);
    if (user.isPresent()) {
      List<Score4UserResponse> response = new ArrayList<>();
      user.get()
          .getScores()
          .stream()
          .forEach(
              (sc) -> {
                response.add(Score4UserResponse.of(sc));
              });
      return response;
    } else {
      return null;
    }
  }

  public List<Score4UserResponse> getScoresByUserIdWithEmpty(String userId, Pageable page) {
    if (userRepository.existsById(userId)) {
      return songRepository.findSongsByUserIdWithEmpty(userId, page);
    } else {
      return null;
    }
  }

  public List<Score4UserResponse> getScoresByUserIdAndSongNameWithEmpty(
      String userId, String songName, Pageable page) {
    if (userRepository.existsById(userId)) {
      return songRepository.findSongsByUserIdAndSongNameWithEmpty(userId, songName, page);
    } else {
      return null;
    }
  }

  public List<Score4UserResponse> getScoresByUserIdAndContributorWithEmpty(
      String userId, String contributor, Pageable page) {
    if (userRepository.existsById(userId)) {
      return songRepository.findSongsByUserIdAndContributorWithEmpty(userId, contributor, page);
    } else {
      return null;
    }
  }

  public List<Score4UserResponse> getScoresByUserIdAndInstrumentWithEmpty(
      String userId, String instrument, Pageable page) {
    if (userRepository.existsById(userId)) {
      return songRepository.findSongsByUserIdAndInstrumentWithEmpty(userId, instrument, page);
    } else {
      return null;
    }
  }

  public List<Score4UserResponse> getScoresByUserIdAndJirikiRankWithEmpty(
      String userId, JirikiRank jiriki, Pageable page) {
    if (userRepository.existsById(userId)) {
      return songRepository.findSongsByUserIdAndJirikiRankWithEmpty(userId, jiriki, page);
    } else {
      return null;
    }
  }

  public List<SongsResponse> getAllSongs(Pageable pageable) {
    Page<Songs> songs = songRepository.findAll(pageable);
    List<SongsResponse> songsResponse = new ArrayList<>();
    songs
        .stream()
        .forEach(
            (s) -> {
              songsResponse.add(SongsResponse.of(s));
            });
    return songsResponse;
  }
}
