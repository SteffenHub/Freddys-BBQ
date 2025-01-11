package LinkedInLearning.SpringBootApp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/dirty-secrets")
public class DirtySecretsRestController {

    private DirtySecretsRepository repository;

    public DirtySecretsRestController(DirtySecretsRepository repository){
        this.repository = repository;
    }


    @GetMapping("/count")
    public int count() {
        return this.repository.count();
    }

    @GetMapping("/e1/{id}")
    public DirtySecret getByIdE1(@PathVariable String id) {
        return this.repository.getById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Entry not found!"
        ));
    }

    @GetMapping("/e2/{id}")
    public DirtySecret getByIdE2(@PathVariable String id) {
        return this.repository.getById(id)
        .orElseThrow(() -> new NoSecretFoundWebException());
    }

    @GetMapping("/e3/{id}")
    public DirtySecret getByIdE3(@PathVariable String id) {
        return this.repository.getById(id)
        .orElseThrow(() -> new NoSecretFoundException());
    }

    @ExceptionHandler({NoSecretFoundException.class})
    public ResponseEntity<String> handleNoSecretException() {
        return ResponseEntity.internalServerError().body("No Entry found!");
    }

    @GetMapping("/{id}")
    public DirtySecret getById(@PathVariable String id) {
        var secret = this.repository.getById(id);
        return secret.orElse(null);
    }

    @PostMapping
    public DirtySecret post(@RequestBody DirtySecret secret){
        return this.repository.save(secret);
    }

}
