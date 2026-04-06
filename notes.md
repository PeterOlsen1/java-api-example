# Notes on RestAssured

### Aside on running the server

* this project requires java 17. here are the steps we need to take to run this while also being able to run the class assignments:
  * If you have homebrew, run `brew install openjdk@17`
  * To use java 17 for the duration of one terminal session, run the commands below. As long as you don't add this to your shell config file it won't be permanent, and it won't mess with our special in-class java version
```
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"
```
  * If you want to check, run `which java` and you should see `/opt/homebrew/opt/openjdk@17/bin/java` after running the export command
    * You would also see openjdk 17.0.18 if you run `java --version`, where a different terminal would display the version 11 we use for class

* Once this is complete, cd into `spring-app` and run `./gradlew bootRun` to start the app on port 8080