# Träningsregister Spring H2
[![CI + docs](https://github.com/postlundhall/traningsapp/actions/workflows/ci-cd.yml/badge.svg?branch=version-4-webb-MVC)](https://github.com/postlundhall/traningsapp/actions/workflows/ci-cd.yml)
## 🇸🇪 Svenska
Träningsregister byggt med **Spring Boot**, **H2-databas**, **Thymeleaf**, och **Bootstrap**.  
Syftet är att bygga grunder och lager för en webbaserad CRUD-applikation som är beredd att byggas vidare med features.

### Funktionalitet
- Hantering av **Övningar**, **Instruktioner** (kommer snart), **Träningspass** (kommer snart) och **Användare**
- Inloggning med lösenordshashning (BCrypt)
- Felhantering via central `GlobalExceptionHandler`
- Internationellt stöd (svenska och engelska med i18n `messages.properties`)
- Bootstrap/Thymeleaf-baserat gränssnitt

### Teknisk struktur
- **Controller / Service / Repository / Model / View ** enligt Spring-MVC mönster
- **H2** enkel filbaserad databas
- **Validering och sanering** (JSR-303 och `SanitizationUtil`)
- **Konfiguration** för säkerhet, lösenord, och initialdata

### Lokal snabbstart
Krav: Java 21 eller nyare
(Maven krävs ej)

```bash
git clone https://github.com/postlundhall/traningsapp.git
cd traningsapp
./mvnw spring-boot:run
```
Eller om du föredrar själv bygga JAR och köra:
```bash
git clone https://github.com/postlundhall/traningsapp.git
cd traningsapp
./mvnw clean package
java -jar target/traningsapp.jar
```
Om `./mvnw` inte körs, kör först: 
```bash
chmod +x mvnw
```
Data sparas i <code>./data/</code>. 
Databas-console tillgänglig via http://localhost:8080/h2-console/ medan app körs.<br> 
Användarnamn: `sa`. Inget lösenord.

### Tester & Javadoc
[JaCoCo testdoc](https://postlundhall.github.io/traningsapp/site/)  
[Javadoc](https://postlundhall.github.io/traningsapp/site/apidocs/)