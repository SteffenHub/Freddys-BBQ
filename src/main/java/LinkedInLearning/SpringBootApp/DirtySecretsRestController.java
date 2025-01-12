package LinkedInLearning.SpringBootApp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/dirty-secrets")
public class DirtySecretsRestController {

    private DirtySecretsRepository repository;
    private DirtySecretsService dirtySecretsService;

    public DirtySecretsRestController(DirtySecretsRepository repository, DirtySecretsService dirtySecretsService) {
        this.repository = repository;
        this.dirtySecretsService = dirtySecretsService;
    }

    @GetMapping
    public Iterable<DirtySecret> get() {
        return this.repository.findAll();
    }

    @GetMapping("/count")
    public long count() {
        return this.repository.count();
    }

    @GetMapping("/{id}")
    public DirtySecret getById(@PathVariable String id) {
        return this.repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Found nothing."));
    }

    @PostMapping
    public DirtySecret post(@RequestBody DirtySecret secret) {
        // Secret speichern
        var savedSecret = this.repository.save(secret);

        // Secret mit Id zurück geben
        return savedSecret;
    }

    @DeleteMapping
    public void delete(@RequestBody List<UUID> secretIds) {
        this.dirtySecretsService.deleteAll(secretIds);
    }

}
