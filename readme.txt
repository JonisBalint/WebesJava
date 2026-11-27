WebesJava autónyilvántartó

Java webalkalmazás autók nyilvántartására. A böngészőben
lehet autót felvenni, listázni, szerkeszteni és törölni. Márka és
azonosító alapján is lehet keresni. A REST API JSON-t használ.

Technológiák
Java 17, Maven, Spring Boot 3.5.16, Spring MVC, Spring Data MongoDB,
Thymeleaf, HTML, CSS és JavaScript.

Maven-modulok
core - az autó adatai, üzemanyagtípusok és saját kivételek
dao - MongoDB-adatelérés
service - az autókkal kapcsolatos műveletek és adatellenőrzés
controller - az MVC- és a REST-végpontok, valamint a REST-hibaválaszok
web - az alkalmazás indítása, a mintaadatok és a webes felület

Indítás
JDK 17 vagy újabb, Maven 3.6.3 vagy újabb és futó MongoDB kell.
A MongoDB a localhost:27017 címen legyen elérhető. Az alkalmazás a
car_database adatbázis cars gyűjteményét használja. Üres gyűjtemény
esetén induláskor öt mintaautót tölt be.

mvn clean verify
java -jar web/target/web-1.0.0.jar

Weboldal és REST API
http://localhost:8080/cars - autólista és keresés
http://localhost:8080/cars/new - új autó felvitele MVC-űrlapon
http://localhost:8080/api/cars - a REST API alapcíme

GET /api/cars - az összes autó
GET /api/cars/{id} - autó lekérdezése azonosító alapján
GET /api/cars/search?brand=Subaru - keresés márka szerint
POST /api/cars - új autó felvitele
PUT /api/cars/{id} - autó módosítása
DELETE /api/cars/{id} - autó törlése
