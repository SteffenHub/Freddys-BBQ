package LinkedInLearning.SpringBootApp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DirtySecret {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
  
    private String name;
  
    private String secret;
  
    public String getId() {
      return id;
    }
  
    public void setId(String id) {
      this.id = id;
    }
  
    public String getName() {
      return name;
    }
  
    public void setName(String name) {
      this.name = name;
    }
  
    public String getSecret() {
      return secret;
    }
  
    public void setSecret(String secret) {
      this.secret = secret;
    }
  }