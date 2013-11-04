# Installation

## Prerequisites

1. You need [Java 7 JDK][1] installed.
2. You need [Maven 3][2] installed (if you want to build the binary self).
3. You need [Git][3] installed (if you want to build the binary self).

## Download or Build

You can either choose to download a prebuilt binary or you can build it from scratch.

### Download

The prebuilt binary downlaod is [here][4].

### Build

First clone the repository:

    $ git clone https://github.com/Weltraumschaf/JUberblog.git

Then build the project:

    $ cd JUberblog
    $ git checkout stable
    $ mvn clean install

## Create the Scaffold

Create the initial scaffold directory for your blog data:

    $ mkdir -p /where/you/want/to/save
    $ juberblog install -l /where/you/want/to/save
    $ cd /where/you/want/to/save
    $ git init
    $ git add -A
    $ git commit -m "Initial commit."

[1]: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
[2]: https://maven.apache.org/download.cgi
[3]: http://git-scm.com/
[4]: https://github.com/Weltraumschaf/JUberblog/raw/master/dist/juberblog.tar.bz2
