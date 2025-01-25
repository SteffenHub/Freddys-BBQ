#  Microservices mit Spring Boot

Dies ist das Repository für den **LinkedIn Learning** Kurs `Microservices mit Spring Boot`. Den gesamten Kurs finden Sie auf [LinkedIn Learning][lil-course-url].

![COURSENAME][lil-thumbnail-url] 

Ein Microservice kann schnell entwickelt werden, autonom von einem Team. Der Microservices skaliert flexibel, ist performant und leicht zu erweitern. Ansonsten einfach austauschen. Aber wie funktioniert das?

Spring Boot bietet eine exzellente Runtime auf der Java Virtual Machine und ein riesiges Ökosystem. Ob Kafka, Kubernetes oder Monitoring-Unterstützung – Spring Boot bringt alles mit, was Sie für Microservices brauchen. Lernen Sie mit Jan Stamer in diesem LinkedIn Learning-Kurs, was Sie sich von Microservices versprechen können.

Mit der Hilfe von praktischen Challenge/Solution-Einheiten am Ende der Kapitel können Sie ihr Wissen immer wieder überprüfen und Ihre Lösung mit der Ihres Trainers vergleichen.

## Anleitung

Dieses Repository hat Branches für jedes Video im Kurs. Verwenden Sie das Ausklappmenü "Branch: ..." in GitHub um zwischen den unterschiedlichen Branches hin und her zu wechseln bzw. um bei einem spezifischen Status einzusteigen. Oder Sie fügen `/tree/BRANCH_NAME` der URL hinzu um direkt in den gewünschten Branch zu wechseln.

## Branches

Die Git Branches sind passend zu den Videos im Kurs strukturiert. Die Namenskonvention lautet `Kapitel#_Video#`. Der Branch `02_03` beinhaltet zum Beispiel die Übungen für das dritte Video im zweiten Kapitel. 
Einige Branches haben einen Anfangsstatus (`b`) für "beginning" und einen Endstatus (`e`). Der Branch mit dem `e` am Ende beinhaltet in diesem Fall stets den Code der am Ende des Videos zu sehen ist. Der `main` Branch beinhaltet den fertigen Quellcode und wird nicht für die Übungen innerhalb des Kurses genutzt.

Wenn Sie von einem Branch nach Änderungen zum nächsten Branch wechseln, erhalten Sie möglicherweise die folgende Meldung:

```
error: Your local changes to the following files would be overwritten by checkout:        [files]
Please commit your changes or stash them before you switch branches.
Aborting
```

Dieses Problem lösen Sie wie folgt:
    Add changes to git using this command: git add .
    Commit changes using this command: git commit -m "some message"

## Installation

1. Klonen Sie das Repository in Ihre lokale Maschine unter Verwendung von terminal (Mac), CMD (Windows) oder ein anderes Werkzeug mit grafischer Bedienoberfläche wie SourceTree.

2. Setzen des Keycloak Hostnamens

Zur Authentifizierung muss der Hostname `keycloak` auf Ihrem System auflösen zu `127.0.0.1`. Dazu müssen Sie folgende Zeile in ihre Hosts Datei hinzufüren (`/etc/hosts` auf Mac/Linux und auf Windows `c:\Windows\System32\Drivers\etc\hosts` als Administrator): 

    127.0.0.1   keycloak

3. Ausführen mit Docker Compose
```
    docker compose up
```

### Autor

**Jan Stamer**

_Softwarearchitekt_

Sehen Sie sich andere Kurse des Autors auf [LinkedIn Learning](https://www.linkedin.com/learning/instructors/jan-stamer) an.

[0]: # (Replace these placeholder URLs with actual course URLs)
[lil-course-url]: https://www.linkedin.com/learning/microservices-mit-spring-boot-24015928
[lil-thumbnail-url]: https://media.licdn.com/dms/image/v2/D4E0DAQEzd7yG-BgU0Q/learning-public-crop_675_1200/learning-public-crop_675_1200/0/1730110372380?e=2147483647&v=beta&t=smoVDiyxs8YC-EsMgzoH_fFC9HQzbl1iYzlEhNspOGQ
