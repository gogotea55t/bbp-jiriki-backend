package io.github.gogotea55t.jiriki.domain.response;

import io.github.gogotea55t.jiriki.domain.entity.Songs;
import io.github.gogotea55t.jiriki.domain.vo.JirikiRank;
import io.github.gogotea55t.jiriki.domain.vo.song.Contributor;
import io.github.gogotea55t.jiriki.domain.vo.song.Instrument;
import io.github.gogotea55t.jiriki.domain.vo.song.SongId;
import io.github.gogotea55t.jiriki.domain.vo.song.SongName;
import lombok.Data;

@Data
public class SongsResponse {
  /** 楽曲ID */
  private SongId songId;

  private JirikiRank jirikiRank;

  /** 楽曲名 */
  private SongName songName;

  /** 投稿者名 */
  private Contributor contributor;

  /** 楽器 */
  private Instrument instrument;

  public static SongsResponse of(Songs song) {
    SongsResponse songsResponse = new SongsResponse();
    songsResponse.setContributor(song.getContributor());
    songsResponse.setInstrument(song.getInstrument());
    songsResponse.setJirikiRank(song.getJirikiRank());
    songsResponse.setSongId(song.getSongId());
    songsResponse.setSongName(song.getSongName());
    return songsResponse;
  }
}
