package linkedinlearning.springbootapp;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirtySecretsService {
  
  private final DirtySecretsRepository repository;

  public DirtySecretsService(DirtySecretsRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void deleteAll(List<UUID> secretIds){
    secretIds.forEach(((id) -> this.repository.deleteById(id)));
  }
}
