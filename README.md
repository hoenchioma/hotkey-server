# Hotkey (server application)
Hotkey is a cross platform solution for controlling your PC from a mobile device

(This is a university project for the course CSE-2216 (Application Development Lab) at the [Department of Computer Science and Engineering, University of Dhaka](http://www.cse.du.ac.bd/).)

This is the repository for the server (desktop side) application. It is a JavaFX (gradle) project. For more information refer the parent repository [hotkey](https://github.com/hoenchioma/hotkey).

## Building
In order to build the source code yourself you will need to first clone the repository. Open the terminal (for windows `cmd` or `git bash`) in the directory of your choice and run:
```
git clone https://github.com/hoenchioma/hotkey-server.git
```
#### Command line
Then in order to build enter the directory you just cloned into and run a gradle build:
```
cd hotkey-server
./gradlew build
```
Note: your JDK must be v11.0 or higher, otherwise there might be errors in build.

To run the app run:
```
./gradlew run
```
For other seeing a list of other available tasks such as `jar` and `shadowJar` (for building a jar file and a fat jar respectively) you can run `./gradlew tasks`. For more info see [this](https://gradle.org/guides/)

#### GUI
Alternatively you can open this folder in any Java IDE (such as IntelliJ IDEA or Eclipse) and import the gradle project.
