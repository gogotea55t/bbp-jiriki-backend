package io.github.gogotea55t.jiriki.domain;

import java.util.ArrayList;
import java.util.List;

import io.github.gogotea55t.jiriki.domain.entity.Scores;
import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.entity.Users;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.ScoreValue;
import io.github.gogotea55t.jiriki.domain.vo.song.Contributor;
import io.github.gogotea55t.jiriki.domain.vo.song.Instrument;
import io.github.gogotea55t.jiriki.domain.vo.song.SongId;
import io.github.gogotea55t.jiriki.domain.vo.song.SongName;
import io.github.gogotea55t.jiriki.domain.vo.user.UserId;
import io.github.gogotea55t.jiriki.domain.vo.user.UserName;
import lombok.Getter;

@Getter
public class SampleDatum {
  private List<Users> users;
  private List<Songs> songs;
  private List<Scores> scores;

  public SampleDatum() {
    Users user1 = new Users();
    user1.setUserId(new UserId("u001"));
    user1.setUserName(new UserName("妖怪1"));

    Users user2 = new Users();
    user2.setUserId(new UserId("u002"));
    user2.setUserName(new UserName("妖怪2"));

    Songs mitemite = new Songs();
    mitemite.setSongId(new SongId("001"));
    mitemite.setJirikiRank(JirikiRank.getJirikiRankFromRankName("地力Ｓ＋"));
    mitemite.setInstrument(new Instrument("チェンバロ"));
    mitemite.setSongName(new SongName("みてみて☆こっちっち"));
    mitemite.setContributor(new Contributor("エメラル"));

    Songs miraclePaint = new Songs();
    miraclePaint.setSongId(new SongId("002"));
    miraclePaint.setJirikiRank(JirikiRank.getJirikiRankFromRankName("地力Ａ＋"));
    miraclePaint.setSongName(new SongName("ミラクルペイント"));
    miraclePaint.setContributor(new Contributor("タタナミ"));
    miraclePaint.setInstrument(new Instrument("ピアノ①"));
    
    Songs gensokyo = new Songs();
    gensokyo.setSongId(new SongId("003"));
    gensokyo.setJirikiRank(JirikiRank.JIRIKI_B_PLUS);
    gensokyo.setSongName(new SongName("千年幻想郷 〜 History of the Moon"));
    gensokyo.setInstrument(new Instrument("E・ギター"));
    gensokyo.setContributor(new Contributor("DXいしょく＠ばけばけ"));

    Scores score1 = new Scores();
    score1.setUsers(user1);
    score1.setSongs(mitemite);
    score1.setScore(new ScoreValue(99));
    score1.setScoreId(1);

    Scores score2 = new Scores();
    score2.setUsers(user2);
    score2.setSongs(mitemite);
    score2.setScore(new ScoreValue(94));
    score2.setScoreId(2);

    Scores score3 = new Scores();
    score3.setUsers(user1);
    score3.setSongs(miraclePaint);
    score3.setScore(new ScoreValue(99));
    score3.setScoreId(3);
    
    Scores score4 = new Scores();
    score4.setUsers(user2);
    score4.setSongs(gensokyo);
    score4.setScore(new ScoreValue(83));
    score4.setScoreId(4);
    
    List<Users> users = new ArrayList<Users>();
    users.add(user1);
    users.add(user2);

    List<Songs> songs = new ArrayList<Songs>();
    songs.add(mitemite);
    songs.add(miraclePaint);
    songs.add(gensokyo);

    List<Scores> scores = new ArrayList<Scores>();
    scores.add(score1);
    scores.add(score2);
    scores.add(score3);
    scores.add(score4);

    this.users = users;
    this.songs = songs;
    this.scores = scores;
  }

}
