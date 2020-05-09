package io.github.gogotea55t.jiriki.domain.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import io.github.gogotea55t.jiriki.domain.entity.WeeklyChallange;

@Mapper
public interface WeeklyChallangeRepository {
  @Insert(
      "INSERT INTO WEEKLY_CHALLANGE (SONGS_SONG_ID, START_DATE, END_DATE) "
          + "VALUES ( #{songs.songId}, #{startDate}, #{endDate} )")
  public void save(WeeklyChallange wc);

  @SelectProvider(type = WeeklyChallangeSqlBuilder.class, method = "buildWeeklyChallangeSql")
  @Results(
      id = "weekly_challange",
      value = {
        @Result(column = "SONG_ID", property = "songs.songId"),
        @Result(column = "SONG_NAME", property = "songs.songName"),
        @Result(column = "JIRIKI_RANK", property = "songs.jirikiRank"),
        @Result(column = "CONTRIBUTOR", property = "songs.contributor"),
        @Result(column = "INSTRUMENT", property = "songs.instrument")
      })
  public WeeklyChallange findLatest();
}
