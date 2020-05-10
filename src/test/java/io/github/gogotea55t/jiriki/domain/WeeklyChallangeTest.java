package io.github.gogotea55t.jiriki.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.gogotea55t.jiriki.domain.repository.SongRepository;
import io.github.gogotea55t.jiriki.domain.repository.WeeklyChallangeRepository;
import io.github.gogotea55t.jiriki.domain.response.WeeklyChallangeResponse;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class WeeklyChallangeTest {
  @Autowired WeeklyChallangeRepository weeklyChallangeRepository;
  @Autowired SongRepository songRepository;
  @MockBean SongService songService;

  private WeeklyChallangeService wcService;
  SampleDatum sample = new SampleDatum();

  @Before
  public void init() {
    songRepository.deleteAll();
    weeklyChallangeRepository.deleteAll();
    songRepository.saveAll(sample.getSongs());

    wcService = new WeeklyChallangeService(songService, weeklyChallangeRepository);
  }

  @Test
  public void WeeklyChallangeに追加できる() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    when(songService.getSongEntityByRandom(query)).thenReturn(sample.getSongs().get(0));
    wcService.setWeeklyChallange();

    assertThat(weeklyChallangeRepository.findAll().size()).isEqualTo(1);
  }

  @Test
  public void WeeklyChallangeに追加したうえで参照できる() throws Exception {
    Map<String, String> query = new HashMap<String, String>();
    when(songService.getSongEntityByRandom(query)).thenReturn(sample.getSongs().get(0));
    wcService.setWeeklyChallange();

    WeeklyChallangeResponse response = wcService.getLatestWeeklyChallange();
    assertThat(response.getStart()).isEqualTo(LocalDate.now());
    assertThat(response.getEnd()).isEqualTo(LocalDate.now().plusDays(6));
    assertThat(response.getSong().getSongId()).isEqualTo(sample.getSongs().get(0).getSongId());
  }
}
