# Cosmic Conquistadors

2nd year 'Group' Project to create space invaders in Java. On a personal note this was very much self-taught as this was in April 2020 which needs no introduction - fun and insightful regardless.

### Project Details
- Project consists of a package called GameDev6
- Our main class is MainGame which is in located in src\GameDev6\MainGame.java
- Our the other classes are GameObject and ObjectManager(which in itself contains 3 Classes, each
being Critter, Bullet and Player) There is also a file called ID.


### How to run:
To compile the project, please execute the following codes from the command line while located in the outermost folder of the project:

If using a Windows-based Terminal:

```javac -classpath .\src\stdlib-package.jar -d .\ src\GameDev6\*.java```\
```java -classpath ".\;.\src\stdlib-package.jar" GameDev6.MainGame ```

If using a Linux-based Terminal (Best Method):

```javac -classpath ./src/stdlib-package.jar -d ./ src/GameDev6/*.java```\
```java -classpath "./;./src/stdlib-package.jar" GameDev6.MainGame ```

(Please note due to technical difficulties we were unable to add stdlib to the classpath so the following method has not personally been tested):

If StdLib is in your Classpath and acts like the Computers in FIRGA
Please enter each of the classes and comment out the statements that import any StdLib libraries.

```javac -d ./src src/GameDev6/.java```\
```java -classpath ./ GameDev6.MainGame```

The program runs perfectly from within the IntelliJ IDE where StdLib has been added to the libraries.

### Disclaimer - Use at own risk
This should run pending regardless version changes (I have not touched it since 2nd year)
