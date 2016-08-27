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

### Overview Of The Directory Layout

The scaffolded directory layout consists of four directories:

1. _configuration_:  Here you find  an example  configuration file. You  may put
this to  any place  you want. The  JUberblog commands which  need this  need the
path to it anyway.
2. _data_: This is the directory where  you put your blog content. Blog articles
goes  into the  _data/posts_ directory  and sites  into _data/sites_.  Drafts go
into _data/drafts_.  There are  also two directories:  _posts_ and  _sites_. You
get the idea.
3.  _public_: This  directory will  be the  target of  the generated  and static
files served  by the webserver. The  sub directories _css_, _img_,  and _js_ are
only  a  suggestion. You  may  change  them, but  then  you  have to  adapt  the
templates!  The  generated files  will  go  into this  directory  (_index.html_,
_feed.xml_, and  _sitemap.xml_) and sub  directories according to  the structure
in _data_.
4.  _templates_:  Here  are   the  [Freemarker][freemarker]  templates  used  to
generate the static HTML.

### Configuration

__IMPORTANT__:  The `configuration.properties`  must be  encoded in  iso-8859-1,
unless umlauts and other special characters will be screwed.

The configuration file has two kinds of  options: Some are basic options for the
generated content  such as the  blog title and  description etc. And  some which
tells JUBerblog  where to  find the  directories where  the [Markdown][markdown]
files, the template files and the published files are.

#### All Available Configuration Options

* _title_:  This is the title  of your blog. This  value is available in  in the
templates by the [Freemarker][freemarker] variable expression `${blogTitle}`.
* _description_: This  is the description of your blog.  This value is available
in  in  the  templates   by  the  [Freemarker][freemarker]  variable  expression
`${blogDescription}`.
* _siteUrl_: This is  the base URL of your blog. This is  used to generate Links
(eg. in  the _feed.xml_ or  _sitemap.xml_). It is also  available in the  in the
templates by the [Freemarker][freemarker] variable expression `${baseUrl}`.
* _language_: This is the title of your  blog. This value is available in in the
templates by the [Freemarker][freemarker] variable expression `${language}`.
*  _encoding_: This  is the  overall encoding  used to  read/write files  by the
command  line  tool and  is  also  available in  the  in  the templates  by  the
[Freemarker][freemarker] variable expression `${encoding}`.
* _dataDirectory_:  This   is  the   directory  where   JUberblog  finds   the
[Markdown][markdown]  content data.  If  the path  is not  absolute  then it  is
interpreted relative from where you invoke the JUberblog command.
*  _temlateDirectory_:  This   is  the  directory  where   JUberblog  finds  the
[Freemarker][freemarker] templates  to render  the static HTML.  If the  path is
not  absolute  then  it  is  interpreted relative  from  where  you  invoke  the
JUberblog command.
* _publicDirectory_:  This is  the directory where  JUberblog puts  the rendered
static HTML data.  If the path is  not absolute then it  is interpreted relative
from where you invoke the JUberblog command.

## Customize The Templates

TODO

[jdk]:          http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
[mvn]:          https://maven.apache.org/download.cgi
[git]:          http://git-scm.com/
[markdown]:     https://daringfireball.net/projects/markdown/
[freemarker]:   TODO
