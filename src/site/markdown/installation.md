# Installation

There are two ways of get JUberblog:

1. download prebuilt binary
2. build it from source

## Download or Build

You can  either choose to download  a prebuilt binary  or you can build  it from
scratch.

### Download

The prebuilt binary download is [here](download.html).

### Build From Source

If you want to build from source you need some build tools as prerequisites:

1. you need [git][git] to checkout the source (alternative you may download the 
   source tar-ball from GitHub)
2. for compiling the sources you need [Java 7 JDK][jdk] or newer
3. also you need [Maven 3][mvn] as build tool

First clone the repository:

    $ git clone https://github.com/Weltraumschaf/JUberblog.git

Then build the project:

    $ cd JUberblog
    $ mvn clean install
    
This will install the final binary (`juberblog.jar`) in the `bin/` directory.

## Setup Your Blog

Your blog setup will consist of three components:

1. the JUberblog command line tool (you built/downloaded that according the 
   previous section)
2. the "data storage" of your blog
3. a webserver which serves your blog


The basic  idea is that  you have a  data storage where  you put the  content as
[Markdown  files][markdown]. Then  you use  the JUberblog  command line  tool to
generate static HTML from these files.  And finally your web server serves these
static files.

### Create the Scaffold

JUberblog requires a  particular directory layout as storage place  of your blog
content. You  may configure this  in various ways. But  for the first  steps you
should  use  the defaults.  To  create  this  structure JUberblog  provides  the
`install` sub command.


Create the initial scaffold directory for your blog data:

    $ mkdir -p /where/you/want/to/store/your/blog
    $ juberblog install -l /where/you/want/to/store/your/blog
    $ cd /where/you/want/to/store/your/blog

An optional  step is making this  directory a version controlled  repository. If
you  set up  your blog  only on  a single  machine (everything  is done  on this
machine), then this is not necessary. But  I recommend it because this gives you
the "feature" of versioning of your  content. In the examples here [Git][git] is
used. You may use other tools like Subversion, Mercurial, Darcs etc.

(optional) Make the blog storage directory a repository:

    $ git init
    $ git add -A
    $ git commit -m "Initial commit."

#### Overview Of The Directory Layout

TODO

[jdk]:      http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
[mvn]:      https://maven.apache.org/download.cgi
[git]:      http://git-scm.com/
[markdown]: https://daringfireball.net/projects/markdown/