Welcome to QuAM Framework!
===================


**QuAM** aims to be as flexible as possible while helping you with easing the way you monitor quality attributes. The basic goal of **QuAM** is to monitor your software in real-time based on its architecture without cluttering your code. All of the data collected is stored in JSON files in order to facilitate integration with other tools.

----------

How does it work?
-------------

QuAM is an aspect-oriented program developed with [AspectJ](https://eclipse.org/aspectj/). It provides the developer with some [Java Annotations](https://en.wikipedia.org/wiki/Java_annotation) which may be used to tell which code elements should be monitored and specify logging criteria. For those who would like to perform custom monitoring, QuAM may be extended so that you can log whatever you like.

----------

How to use
-------------------

Type the following command in your terminal to create your JAR file:
```console
./gradle clean jar
```
Now you can find the JAR file file under ```build/libs/``` and add it to your project.

> **Note:**

> - Run your program as an AspectJ application so that the framework can work.
> - Compile your software with the **-parameters** compiler option so that you can get the parameter's names in the logs. If you're using Eclipse IDE, open ```Window -> Preferences -> Java -> Compiler``` and then make sure *Store information about method parameters (usable via reflection)* is checked.

Assuming that you have successfully added QuAM to your project, you can now annotate your code with **@Loggable** and **@NotLoggable** and start to monitor your code.


## Creator

**Igor C. A. de Lima**: 

* [igorcadelima [at] gmail [dot] com](mailto:%69%67%6F%72%63%61%64%65%6C%69%6D%61%40%67%6D%61%69%6C%2E%63%6F%6D)
* <https://github.com/igorcadelima>

## License

QuAM Framework is distributed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).


