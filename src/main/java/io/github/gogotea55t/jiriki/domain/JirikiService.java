package io.github.gogotea55t.jiriki.domain;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchGet;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.TwitterUsers;
import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.repository.ScoresRepository;
import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.TwitterUsersRepository;
import io.github.gogotea55t.jiriki.domain.repository.UserRepository;
import io.github.gogotea55t.jiriki.domain.request.TwitterUsersRequest;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;

@EnableScheduling
@Service
public class JirikiService {
  private GoogleSpreadSheetConfig sheetConfig;

  private UserRepository userRepository;

  private SongRepository songRepository;

  private ScoresRepository scoreRepository;

  private GoogleSheetsService sheetsService;

  private TwitterUsersRepository twitterUsersRepository;

  @Autowired
  public JirikiService(
      GoogleSpreadSheetConfig sheetConfig,
      GoogleSheetsService sheetsService,
      UserRepository userRepository,
      SongRepository songRepository,
      ScoresRepository scoreRepository,
      TwitterUsersRepository twitterUsersRepository) {
    this.sheetConfig = sheetConfig;
    this.sheetsService = sheetsService;
    this.userRepository = userRepository;
    this.songRepository = songRepository;
    this.scoreRepository = scoreRepository;
    this.twitterUsersRepository = twitterUsersRepository;
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
                if (scoreFetched.get().getScore() == score.getScore()) {
                  // do nothing
                } else {
                  scoreFetched.get().setScore(score.getScore());
                }
              } else {
                scores.add(score);
              }
            }
          } catch (Exception e) {
            System.out.println(e.getMessage());
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

  public UserResponse findPlayerByTwitterId(String twitterId) {
    Optional<TwitterUsers> response = twitterUsersRepository.findById(twitterId);
    if (response.isPresent()) {
      return UserResponse.of(response.get().getUsers());
    } else {
      return null;
    }
  }

  @Transactional
  public UserResponse addNewLinkBetweenUserAndTwitterUser(TwitterUsersRequest request) {
    Optional<Users> user = userRepository.findById(request.getUserId());
    if (user.isPresent()) {
      Optional<TwitterUsers> tw = twitterUsersRepository.findById(request.getTwitterUserId());
      if (tw.isPresent()) {
        twitterUsersRepository.delete(tw.get());
      }
      twitterUsersRepository.save(new TwitterUsers(request.getTwitterUserId(), user.get()));
      return UserResponse.of(user.get());
    } else {
      throw new NullPointerException("User Not Found.");
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

  public List<SongsResponse> getSongByJiriki(JirikiRank jiriki, Pageable page) {
    List<SongsResponse> songs = new ArrayList<>();

    List<Songs> songsResponse = songRepository.findByJirikiRank(jiriki, page);
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
  
  public List<Score4UserResponse> getAverageScores(Pageable page) {
	return songRepository.findSongsWithAverage(page);
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

  public String getUserSubjectFromToken() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
    DecodedJWT decodedJwt = JWT.decode(details.getTokenValue());
    return decodedJwt.getSubject();
  }
}
