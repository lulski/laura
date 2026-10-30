# Laura

## Hot reload during development

Spring Boot DevTools is included as a development-only dependency. Start the app
with `./gradlew bootRun`, then save a change. The IDE/build tool must compile
changed Java files into `build/classes/java/main`; DevTools watches those class
files and restarts the application context when they change. Resource changes
are refreshed without a full application restart where possible.

DevTools is not included in the packaged production application.
