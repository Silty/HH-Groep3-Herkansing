# README #

Deze Hartige Hap webapplicatie is gemaakt met Spring MVC.

### What is this repository for? ###

De applicatie bevat de volgende niet-funtionele aspecten:

* architectuur (gelaagde architectuur met MVC in de presentatielaag)
* persistentie in de data access laag met behulp van het object-relation mapping tool JPA 
* transactionaliteit (methodes op de service interfaces)
* tiling (header, footer, menu, body)
* i18n (Engels en Nederlands)
* security (autorisatie en personalisatie)
* theming
* input validatie
* pagination (opgevraagde content wordt geretourneerd in pagina's)
* ajax
* RESTful web service
* enkele tests


Open eindjes:

* basic css en GUI (dit is geen hoofddoel, maar ik ben het met je eens als je vindt dat het er niet uit ziet!)
* voldoende tests


### How do I get set up? ###

Installatie van de applicatie:

* De applicatie maakt gebruik van een MySQL database met de naam "hh".
* De database moet aanwezig zijn om de applicatie te laten werken, maar tabellen hoeven niet handmatig te worden aangemaakt. Dat doet Hibernate voor je.
* Mocht je een eerdere versie van de Hartige Hap webapplicatie geïnstalleerd hebben gehad, dan de oude database volledig weggooien en een nieuwe, lege database creëren, aangezien de tabellenstructuur anders is. 
* In het bestand src/main/resources/datasource-jpa-tx.xml staat de configuratie om de database te benaderen, die je indien nodig kunt aanpassen, met name username, password en URL. 
* In het bestand datasource-jpa-tx.xml staat de property <prop key="hibernate.hbm2ddl.auto">create</prop>. Met deze property worden de tabellen telkens weggegooid en opnieuw gecreëerd, als de applicatie wordt gedeployed. Als je dit niet wilt, verwijder dan deze property uit het bestand.
* In het tips & tricks document staat hoe je een bestaande applicatie importeert in STS. STS is de Spring ontwikkelomgeving.
* Deze applicatie maakt gebruik van Lombok. Hiervoor moet STS worden aangepast, anders begrijpt STS de broncode niet en geeft foutieve foutmeldingen. Om dit te doen, (1) download lombok.jar van http://projectlombok.org/, (2) dubbelklik op lombok.jar, (3) bevestig dat STS aangepast mag worden door Lombok.
* In STS, Ga menu "Run"/"Run Configurations...". Ga naar tabblad "Arguments" en zet bij "VM Arguments" de opties "-Xmx1024m -Xss192k -XX:MaxPermSize=256m -Dinsight-max-frames=6000" (zonder de quotes) erbij. Met het vergroten van de heap size en perm heap size naar de gegeven waarden voorkom je de foutmelding "java.lang.ClassNotFoundException: org.springframework.web.context.ConfigurableWebEnvironment". Met het vergroten van insight-max-frames naar 6000 voorkom je mogelijk de foutmelding "Imbalanced frame stack! (exit() called too many times)". Echter deze foutmelding wijst meestal op een fout in het JPA-deel van de applicatie.
* Als je een langzame computer hebt, is 120 seconden soms niet genoeg voor het bouwen en deployen van de applicatie. Als je het dan nogmaals probeert, lukt het wel, maar toch irritant. Je kunt de timeout van 120 seconden groter zetten door: in STS, dubbelklik op "VMware vFabric tc Server ..." die je vindt in de "Servers" tab linksonder. Er komt dan een schermpje op waarbij er een "Timouts" tab is. Als je Start (in seconds) op 240 zet, ben je van het probleem af. Zo niet, dan wordt het echt tijd voor een nieuwe computer.

Gebruik van de applicatie:

* deploy de applicatie:
    * rechter muisklik op project, "run as", "run on server"
    * check de console log op excepties
    * STS start automatisch een web browser op binnen STS
    * andere browser? Gebruik: http://localhost:8080/hh/
* log in met gebruikersnaam "employee" en password "employee"
* klik op het restaurant naar keuze
    * subsysteem bestelling: kies een tafel naar keuze, kies je gerechten, doe een of meer bestellingen, vraag om afrekenen
    * subsysteem keuken: plan bestelling, meld bestelling gereed
    * subsysteem bediening: meld bestelling geserveerd, meld rekening betaald
    * klanten subsysteem: CRUD op klanten
* geïmplementeerde business rules:
    * een lege bestelling kan niet gedaan worden
    * een lege rekening kan niet afgerekend worden
    * een rekening met nog niet bestelde bestelling mag niet afgerekend worden
* autorisatie en personalisatie:
    * er is ook een gebruiker "customer" met password "customer". Deze gebruiker heeft minder rechten
    * als je in het geheel niet bent ingelogd, mag je wel alles zien, maar mag je geen acties ondernemen