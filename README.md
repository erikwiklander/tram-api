# tram-api
Simple Backend for the Next Tram

Before deploying to App Engine put a file like this in src/main/appengine/app.yaml
```yaml
runtime: java
env: flex
env_variables:
  SPRING_PROFILES_ACTIVE: 'google'
```
