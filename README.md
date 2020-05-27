# Simple Heroku Java Webapp #

## Build ##
```
mvn clean package
```

## Deploy ##
```
heroku plugins:install java
heroku apps:create --region eu
heroku war:deploy target/simple-webapp.war
heroku open
```
