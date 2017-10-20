# tram-api
Simple Backend for the Next Tram

Before deploying to App Engine put a file like this in src/main/appengine/app.yaml
```yaml
runtime: java
env: flex
env_variables:
  SPRING_PROFILES_ACTIVE: 'google'
```
This will only work if you get your key from trafiklab. Set it as a property in application.properties like this: tram.robot-key=<key> or as an environment variable like TRAM_ROBOT_KEY=...
