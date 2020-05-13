package io.github.gogotea55t.jiriki.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.services.sheets.v4.model.ValueRange;

import io.github.gogotea55t.jiriki.AuthService;
import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@EnableScheduling
@Service
public class JirikiService {
  private UserRepository userRepository;

  private SongRepository songRepository;

  private ScoresRepository scoreRepository;

  private GoogleSheetsService sheetsService;

  private AuthService authService;

  @Autowired
  public JirikiService(
      GoogleSheetsService sheetsService,
      UserRepository userRepository,
      SongRepository songRepository,
      ScoresRepository scoreRepository,
      AuthService authService) {
    this.sheetsService = sheetsService;
    this.userRepository = userRepository;
    this.songRepository = songRepository;
    this.scoreRepository = scoreRepository;
    this.authService = authService;
  }

  @Scheduled(cron = "0 0 4 * * *")
  @Transactional
  public void doGet() {
    try {
      //      userRepository.deleteAll();
      //      scoreRepository.deleteAll();
      //      songRepository.deleteAll();

      List<ValueRange> respList = sheetsService.getValuesFromSpreadSheet();

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

      for (Users user : users.values()) {
        // ユーザーが登録済みかどうか調べる
        Optional<Users> userFetched = userRepository.findById(user.getUserId());
        if (userFetched.isPresent()) {
          // 登録済みの場合、登録内容が変わっていないか調べる
          if (user.equals(userFetched.get())) {
            // 変わっていなければ何もしない
          } else {
            // 変わっていた場合は更新をかける
            userFetched.get().setUserName(user.getUserName());
            userRepository.update(userFetched.get());
          }
        } else {
          // そもそも登録がない場合は新規登録
          userRepository.save(user);
        }
      }

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
      for (Songs s : songs.values()) {
        Optional<Songs> songFetched = songRepository.findById(s.getSongId());
        if (songFetched.isPresent()) {
          if (s.equals(songFetched.get())) {

          } else {
            songFetched.get().setJirikiRank(s.getJirikiRank());
            songFetched.get().setSongName(s.getSongName());
            songFetched.get().setInstrument(s.getInstrument());
            songFetched.get().setContributor(s.getContributor());
            songRepository.update(songFetched.get());
          }
        } else {
          songRepository.save(s);
        }
      }

      // **************************
      // 成績情報の取得
      // **************************

      List<List<Object>> scoreRows = respList.get(2).getValues();

      List<Scores> scores = new ArrayList<>();

      Pattern p = Pattern.compile("^\\d{2}$");

      for (int i = 2; i < scoreRows.size(); i++) {
        List<Object> scoreRow = scoreRows.get(i);

        // 完全に空白になっている行はスキップする（もっとやりようがある気がする）
        if (scoreRow.size() == 0
            || scoreRow.get(0) == null
            || scoreRow.get(0).toString().equals("")) {
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
              score.setScore(new ScoreValue(Integer.parseInt(scoreCol)));

              Optional<Scores> scoreFetched =
                  scoreRepository.findByUsers_UserIdAndSongs_SongId(
                      scoreRows.get(0).get(j).toString(), thisSong.getSongId());

              if (scoreFetched.isPresent()) {
                if (scoreFetched.get().getScore().isEqualTo(score.getScore())) {
                  // do nothing
                } else {
                  scoreFetched.get().setScore(score.getScore());
                  scoreRepository.update(scoreFetched.get());
                }
              } else {
                scores.add(score);
              }
            }
          } catch (Exception e) {
            System.out.println(e);
            System.out.println(scoreRow);
            continue;
          }
        }
      }
      if (!scores.isEmpty()) {
        // saveAllは一件もないときエラーになる
        scoreRepository.saveAll(scores);
      }
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  public String getUserSubjectFromToken() {
    return authService.getUserSubjectFromToken();
  }
}
