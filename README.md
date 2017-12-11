## Synopsis

This webcrawler project captures resources like - links, images, html meta, sitemap from given website URL and shows in web interface. 
This may not work properly on all domains except: `http://wiprodigital.com`

Sample screenshot is below: ![web crowler sample](/screenshot.PNG).

## Technologies/Tools Used

1. Spring Boot - 1.5.8
2. Jsoup - Html parsing
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
   e.g: `http://wiprodigital.com`

3. To access swagger rest client: `http://localhost:8080/api`

## Test

Execute 'gradlew test' to run unit and integration tests.

## Scope for improvements

**1. Multithread support**

The links identified in a URL could be large in numbers which may take 
more time to give back the result. It can be solved by using executor framework.

**2. Data extraction**

Data retrieval logic from given URL can be improved for other domains with support of common sitemap pattern. 

**3. User Interface**

User Interface can be improved for look and feel.
