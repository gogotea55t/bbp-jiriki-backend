package io.github.gogotea55t.jiriki.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.github.gogotea55t.jiriki.domain.WeeklyChallangeService;
import io.github.gogotea55t.jiriki.domain.response.WeeklyChallangeResponse;

@RunWith(SpringRunner.class)
@ActiveProfiles("unittest")
@SpringBootTest
public class WeeklyChallangeControllerTest {
  private MockMvc mockMvc;
  @MockBean WeeklyChallangeService wcService;

  private WeeklyChallangeController controller;

  @Before
  public void init() {
    controller = new WeeklyChallangeController(wcService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void ウィークリーチャレンジ情報が取得できる() throws Exception {
    WeeklyChallangeResponse response = new WeeklyChallangeResponse();
    when(wcService.getLatestWeeklyChallange()).thenReturn(response);
    this.mockMvc.perform(get(new URI("/v1/weekly_challange"))).andExpect(status().is(200));
  }
}
