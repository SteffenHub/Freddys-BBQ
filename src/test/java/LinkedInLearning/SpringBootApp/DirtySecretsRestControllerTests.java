package LinkedInLearning.SpringBootApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DirtySecretsRestControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
      DockerImageName.parse("postgres:latest"));

  @Test
  public void shouldSaveSecrets() throws Exception {
    // Controller aufrufen
    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/dirty-secrets")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"name\": \"Linda\", \"secret\": \"Cheated her son into Stanford.\" }"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
  }

  
  @Test
  public void shouldIncreaseCount() throws Exception{
    int initialCount = this.getCount();

    // POST ein neues DirtySecret
    this.mockMvc.perform(MockMvcRequestBuilders.post("/dirty-secrets")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "name": "Alice",
                    "secret": "Loves pineapple on pizza"
                }
                """))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());

    // Count prüfen
    int newCount = this.getCount();

    // Sicherstellen, dass der Count um 1 erhöht wurde
    assertEquals(initialCount + 1, newCount);
  }

  private int getCount() throws Exception{
    MvcResult count = this.mockMvc.perform(MockMvcRequestBuilders.get("/dirty-secrets/count"))
    .andExpect(MockMvcResultMatchers.status().isOk())
    .andReturn();
    return Integer.parseInt(count.getResponse().getContentAsString());
  }

}
