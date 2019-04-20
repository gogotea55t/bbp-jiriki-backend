package io.github.gogotea55t.jiriki.domain;

import io.github.gogotea55t.jiriki.domain.entity.Songs;
import lombok.Data;

@Data
public class SongsResponse {
  /** 楽曲ID */
  private String songId;

  private String jirikiRank;

  /** 楽曲名 */
  private String songName;

  /** 投稿者名 */
  private String contributor;

  /** 楽器 */
  private String instrument;

  public static SongsResponse of(Songs song) {
    SongsResponse songsResponse = new SongsResponse();
    songsResponse.setContributor(song.getContributor());
    songsResponse.setInstrument(song.getInstrument());
    songsResponse.setJirikiRank(song.getJirikiRank().getJirikiRank());
    songsResponse.setSongId(song.getSongId());
    songsResponse.setSongName(song.getSongName());
    return songsResponse;
  }
}
