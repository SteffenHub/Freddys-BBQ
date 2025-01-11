# 3_2 Mit Annotationen Spring Beans erzeugen

### @Component
- Auf Klassenebene vergeben (@ über Klassennamen)
- Kennzeichnet Spring Beans 

### @Service
- Auf Klassenebene vergeben (@ über Klassennamen)
- Kennzeichnet Spring Beans mit Geschäftslogik

### @Bean
- An Methoden vergeben (@ über Methodennamen)
- Instanzen als Spring Beans vom IoC Container verwaltet 

### @Controller
- Fürs Web

### @Repository
- Für die Persistenz


# 4_7 Fehler robust behandeln

### 1 Die Spring ResponseStatusException-Klasse
- new ResponseStatusException(HttpStatus.NOT_FOUND)  
```java
@GetMapping("/e1/{id}")  
public DirtySecret getByIdE1(@PathVariable String id) {  
    return this.repository.getById(id)  
    .orElseThrow(() -> new ResponseStatusException(  
        HttpStatus.NOT_FOUND,  
        "Entry not found!"  
    ));  
}
```
### 2 Die Annotation @ResponseStatus an Exception-Klasse
- @ResponseStatus(value=HttpStatus.NOT_FOUND)
```java
@GetMapping("/e2/{id}")
public DirtySecret getByIdE2(@PathVariable String id) {
    return this.repository.getById(id)
    .orElseThrow(() -> new NoSecretFoundWebException());
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSecretFoundWebException extends RuntimeException {}
```

### 3 Eigene Exception-Handler-Methode mit @ExcpetionHandle
```java
@GetMapping("/e3/{id}")
public DirtySecret getByIdE3(@PathVariable String id) {
    return this.repository.getById(id)
    .orElseThrow(() -> new NoSecretFoundException());
}

@ExceptionHandler({NoSecretFoundException.class})
public ResponseEntity<String> handleNoSecretException() {
    return ResponseEntity.internalServerError().body("No Entry found!");
}

public class NoSecretFoundException extends RuntimeException{}
```