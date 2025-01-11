package LinkedInLearning.SpringBootApp;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;;

public interface DirtySecretsRepository extends CrudRepository<DirtySecret, UUID>{

  
}

/*
 * @Repository
 * public class DirtySecretsRepository {
 * 
 * private Map<String, DirtySecret> secrets = new TreeMap<>();
 * 
 * public DirtySecretsRepository() {
 * var secret = new DirtySecret();
 * secret.setId("test-123");
 * secret.setName("Doug");
 * secret.setSecret("Ex Alcoholic");
 * this.secrets.put(secret.getId(), secret);
 * }
 * 
 * Optional<DirtySecret> getById(String id) {
 * if (!this.secrets.containsKey(id)) {
 * return Optional.empty();
 * }
 * return Optional.of(this.secrets.get(id));
 * }
 * 
 * int count() {
 * return this.secrets.size();
 * }
 * 
 * DirtySecret save(DirtySecret secret) {
 * // Id generieren
 * secret.setId(UUID.randomUUID().toString());
 * 
 * // Secret merken
 * this.secrets.put(secret.getId(), secret);
 * 
 * // Secret mit Id zurück geben
 * return secret;
 * }
 * 
 * }
 */