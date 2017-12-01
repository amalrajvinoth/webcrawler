## Synopsis

This webcrawler project captures links, images, html meta, sitemap and shows in web interface. 
This may not work properly on other domains except: `http://wiprodigital.com`

Sample screenshot is attached.

## Technologies/Tools Used

1. Spring Boot - 1.5.8
2. jsoup - Html parsing
3. Jackson
4. Gradle
5. Swagger
6. JQuery
7. Boostrap
8. FancyGrid
9. Bootsrap TreeView plugin.

## Run/Build

1. Execute `gradlew bootRun` to start the server manually.

2. Open browser: `http://localhost:8080/index.html` and give any URL for analysis.
   e.g: `http://localhost:8080/index.html`

3. To access swagger rest client: `http://localhost:8080/api`

## Scope for improvements

**1. Multithread support**

The links identified in a URL could be large in numbers which may take 
more time to give back the result. It can be solved by using executor framework.

**2. Data extraction**

Data retrieval logic from given URL can be improved for other domains with support of common sitemap pattern. 

**3. User Interface**

UI can be improved for look and feel.