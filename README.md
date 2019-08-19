ASGwrapper-asynctools
---------------------

ASGwrapper-asynctools is a library which helps to abstract from calling third party asynchronous tools.

It supports
* [Petrify](https://www.cs.upc.edu/~jordicf/petrify/)
* [PCOMP](http://homepages.cs.ncl.ac.uk/victor.khomenko/tools/tools.html)
* [PUNF](http://homepages.cs.ncl.ac.uk/victor.khomenko/tools/tools.html)
* [MPSAT](http://homepages.cs.ncl.ac.uk/victor.khomenko/tools/tools.html)

We'd like to thank all the people involved in development of these tools for their excellent work. Because these tools are free of charge and allowed to be distributed, these tools are included in this release.

### Build instructions ###

To build ASGwrapper-asynctools, Apache Maven v3 (or later) and the Java Development Kit (JDK) v1.7 (or later) are required.

1. Build [ASGcommon](https://github.com/hpiasg/asgcommon)
2. Execute `mvn clean install -DskipTests`