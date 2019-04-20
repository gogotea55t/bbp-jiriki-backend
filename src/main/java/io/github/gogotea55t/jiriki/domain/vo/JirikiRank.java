package io.github.gogotea55t.jiriki.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;

public enum JirikiRank {
  JIRIKI_S_PLUS(1, "地力Ｓ＋"),
  KOJINSA_S_PLUS(2, "個人差Ｓ＋"),
  JIRIKI_S(3, "地力Ｓ"),
  KOJINSA_S(4, "個人差Ｓ"),
  JIRIKI_A_PLUS(5, "地力Ａ＋"),
  KOJINSA_A_PLUS(6, "個人差Ａ＋"),
  JIRIKI_A(7, "地力Ａ"),
  KOJINSA_A(8, "個人差Ａ"),
  JIRIKI_B_PLUS(9, "地力Ｂ＋"),
  KOJINSA_B_PLUS(10, "個人差Ｂ＋"),
  JIRIKI_B(11, "地力Ｂ"),
  KOJINSA_B(12, "個人差Ｂ"),
  JIRIKI_C(13, "地力Ｃ"),
  KOJINSA_C(14, "個人差Ｃ"),
  JIRIKI_D(15, "地力Ｄ"),
  KOJINSA_D(16, "個人差Ｄ"),
  JIRIKI_E(17, "地力Ｅ"),
  KOJINSA_E(18, "個人差Ｅ"),
  JIRIKI_F(19, "地力Ｆ"),
  KOJINSA_F(20, "個人差Ｆ"),
  UNACCOMPLISHED(21, "求90"),
  NON_DETERMINED(22, "未決定");

  private int jirikiId;
  private String jirikiRank;

  private JirikiRank(int jirikiId, String jirikiRank) {
    this.jirikiId = jirikiId;
    this.jirikiRank = jirikiRank;
  }

  public int getJirikiId() {
    return jirikiId;
  }

  @JsonValue
  public String getJirikiRank() {
    return jirikiRank;
  }

  public static JirikiRank getJirikiRankFromId(int jirikiId) {
    for (JirikiRank j : JirikiRank.values()) {
      if (j.getJirikiId() == jirikiId) {
        return j;
      }
    }
    return JirikiRank.NON_DETERMINED;
  }

  public static JirikiRank getJirikiRankFromRankName(String jirikiRank) {
    for (JirikiRank j : JirikiRank.values()) {
      if (j.getJirikiRank().equals(jirikiRank)) {
        return j;
      }
    }
    return JirikiRank.NON_DETERMINED;
  }
}
