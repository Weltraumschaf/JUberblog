# Installation And Configuration

There are two ways of get JUberblog:

1. download prebuilt binary
2. build it from source

## Download or Build

You can  either choose to download  a prebuilt binary  or you can build  it from
scratch.

### Download

The prebuilt binary download is [here](downloads.html).

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
3. a web server which serves your blog


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
files served  by the web server. The  sub directories _css_, _img_,  and _js_ are
only  a  suggestion. You  may  change  them, but  then  you  have to  adapt  the
templates!  The  generated files  will  go  into this  directory  (_index.html_,
_feed.xml_, and  _sitemap.xml_) and sub  directories according to  the structure
in _data_.
4.  _templates_:  Here  are   the  [Freemarker][freemarker]  templates  used  to
generate the static HTML.

More information about  a particular directory may be found  in a `README.md` in
that directory.

### Configuration

__IMPORTANT__:  The `configuration.properties`  must be  encoded in  iso-8859-1,
unless umlauts and other special characters will be screwed.

The configuration file has two kinds of  options: Some are basic options for the
generated content  such as the  blog title and  description etc. And  some which
tells JUBerblog  where to  find the  directories where  the [Markdown][markdown]
files, the template files and the published files are.

#### All Available Configuration Options

- _title_:             This is the title of your blog. This value is available in 
                       the templates by the [Freemarker][freemarker] variable 
                       expression `${blogTitle}`.
- _description_:       This is the description of your blog. This value is 
                       available in in the templates by the 
                       [Freemarker][freemarker] variable expression 
                       `${blogDescription}`. 
- _siteUrl_:           This is the base URL of your blog. This is used to 
                       generate Links (eg. in the _feed.xml_ or _sitemap.xml_). 
                       It is also available in the in the templates by the
                       [Freemarker][freemarker] variable expression `${baseUrl}`. 
- _language_:          This is the title of your blog. This value is available in 
                       in the templates by the [Freemarker][freemarker] variable 
                       expression `${language}`. 
- _encoding_:          This is the overall encoding used to read/write files by 
                       the command line tool and is also available in the in the 
                       templates by the [Freemarker][freemarker] variable 
                       expression `${encoding}`.
- _dataDirectory_:     This is the directory where JUberblog finds the 
                       [Markdown][markdown] content data. If the path is not 
                       absolute then it is interpreted relative from where you 
                       invoke the JUberblog command. 
- _templateDirectory_: This is the directory where JUberblog finds the
                       [Freemarker][freemarker] templates to render the static 
                       HTML. If the path is not absolute then it is interpreted 
                       relative from where you invoke the JUberblog command. 
- _publicDirectory_:   This is the directory where JUberblog puts the rendered 
                       static HTML data. If the path is not absolute then it is
                       interpreted relative from where you invoke the JUberblog 
                       command.

## Customize The Templates

To generate HTML from the  [Markdown][markdown] files templates are used instead
of plain converting  it with a processor  to HTML. This gives us  the ability to
assign  variables from  JUberblog or  use other  constructs known  from template
engines  (includes, loops,  conditional etc.).  Under the  hood the  widely used
[Freemarker][freemarker] library is used.

All templates are  in the scaffold directory `templates`,  unless you configured
an other  location. There  are several  template files. There  are two  kinds of
templates:

1. Templates for the HTML generation: located in the root of the template 
   directory.
2. Templates to create the Markdown templates for the raw data files: located in 
   subdirectory _create_.

### Templates

JUberblog brings some  very basic templates to generate the  static content. You
can change them to adopt the static generated content to your own custom theme.

#### feed.ftl

This is  the template file  which is used to  generate the [RSS][rss]  feed XML.
You should only change this template if you know what you do.

The available template variables are:

| variable        | description                                               |
|-----------------|-----------------------------------------------------------|
| blogTitle       | This is the blog title from your configuration.           |
| link            | This is the site URL from your configuration.             |
| blogDescription | This is the blog description from your configuration.     |
| lastBuildDate   | This is the file creation date as ISO time stamp.         |
| items           | This is a collection of objects representing a blog post. |

The objects in the list _items_ have the properties:

| property    | description                                                     |
|-------------|-----------------------------------------------------------------|
| title       | The title of the blog post.                                     |
| description | This comes from from the preprocessor directive.                |
| pubDate     | Publishing date in the format `Fri, 19 Mar 2010 09:42:24 +0100`.|
| dcDate      | Publishing date in the format `2010-03-19T09:42:24+01:00`.      | 

#### index.ftl

This  is  the  inner  template  for  blog posts  which  will  be  inserted  into
_lauout.ftl_  to  the place  where  the  variable  `${content}` is  placed.  All
variables which are available in _layout.ftl_ are also available here.

The available template variables are:

The available template variables are:

| variable | description                                               |
|----------|-----------------------------------------------------------|
| posts    | This is a collection of objects representing a blog post. |

The objects in the list _posts_ have the properties:

| property | description                                                      |
|----------|------------------------------------------------------------------|
| link     | Direct link to the blog post.                                    |
| title    | Title of the blog post.                                          |
| pubDate  | Publishing date as Java date object ([formatting][date-format]). |

#### layout.ftl

This is  the outer layout  template. JUberblog uses  a common pattern  named two
step layout:  This means  you have an  outer template for  the things  all sites
have  in common  and inside  this  template is  a  second one  inserted for  the
different things. As schema you can imagine it as follows:

    +--------------------+
    |  laout.ftl         |
    |  +--------------+  |
    |  |  index.ftl/  |  |
    |  |  post.ftl/   |  |
    |  |  site.ftl    |  |
    |  +--------------+  |
    +--------------------+

The available template variables are:

| variable        | description                                           |
|-----------------|-------------------------------------------------------|
| description     | This comes from from the preprocessor directive.      |
| keywords        | This comes from from the preprocessor directive.      |
| encoding        | This is the encoding from your configuration.         |
| baseUrl         | This is the site URL from your configuration.         |
| blogTitle       | This is the blog title from your configuration.       |
| blogDescription | This is the blog description from your configuration. |
| content         | This is the rendered content from the inner template. |

#### post.ftl

This  is  the  inner  template  for  blog posts  which  will  be  inserted  into
_lauout.ftl_  to  the place  where  the  variable  `${content}` is  placed.  All
variables which are available in _layout.ftl_ are also available here.

The available template variables are:

The available template variables are:

| variable | description                     |
|----------|---------------------------------|
| content  | The rendered blog post content. |

#### site.ftl

This  is  the  inner  template  for  blog posts  which  will  be  inserted  into
_lauout.ftl_  to  the place  where  the  variable  `${content}` is  placed.  All
variables which are available in _layout.ftl_ are also available here.

The available template variables are:

| variable | description                     |
|----------|---------------------------------|
| content  | The rendered blog post content. |

#### site_map.ftl

This template  is used to generate  a [site map XML][sitemap]  for better search
engine indexing. You should only change this template if you know what you do.

The available template variables are:

| variable | description                                                        |
|----------|--------------------------------------------------------------------|
| encoding | This is the encoding from your configuration.                      |
| urls     | This is a collection of objects representing al URLs of your blog. |

The objects in the list _urls_ have the properties:

| property   | description                                                                  |
|------------|------------------------------------------------------------------------------|
| loc        | The URL of the content.                                                      |
| lastmod    | When the file was last published.                                            |
| changefreq | At the moment this is hardcoded to _daily_ for posts and _weekly_ for sites. |
| priority   | At the moment this is hardcoded to _0.8_ for posts and _0.5_ for sites.      |

### Markdown Templates

#### post_or_site.md.ftl

This template is  used by the `create`  sub command to produce  an empty content
file for you. It contains a pre  processor block (see below) and the title given
by the CLI options. There you place your content.

The available template variables are:

| variable | description                     |
|----------|---------------------------------|
| title    | The post/site title.            |

### Make Custom Style With SASS

Of course you can style your blog with  simple CSS. You only have to put the CSS
files somewhere in your public folder  served by the web server. Recommended and
default is  _public/css_. If  you prefer  another location  you must  change the
link in the _layout.ftl_.

But  you can  also use  [SASS][sass]. For  this use  case there  is a  directory
called _sass_ in  the freshly created scaffold data directory.  It also brings a
a  [normalizer][normalizer]  script  which  sets  some  sane  defaults  for  all
browser. To  generate CSS you need  [SASS][sass] installed and use  this command
from your scaffold dir:

    $ sassls sassc sass/main.scss public/css/main.css

## Create Content

### Create Posts

TODO

### Create Sites

TODO

### Preprocessor Directives

The content  files for posts and  sites support a preprocessor  directive to add
some meta data:

    <?juberblog
        navigation:  some navigation
        description: some description.
        keywords:    some, keywords
    ?>

The syntax is  the same as for  XML preprocessor directives. So to  start it use
`<?juberblog` and  to end the  directive use  `?>`. The directive  for JUberblog
supports simple  key-value pairs. The  key must  not contain any  characters but
letters and numbers. The  value may contain any word character.  The key and the
value are separated by  a colon. Obviously you can't use the  colon in the value.
A key value pair is separated by  newline. Also obviously you can't use newlines
in  the  value.  The  key  values  will be  assigned  to  the  layout  template.
`Description` and `Keywords` are used for the meta tags by default.

[jdk]:          http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
[mvn]:          https://maven.apache.org/download.cgi
[git]:          http://git-scm.com/
[markdown]:     https://daringfireball.net/projects/markdown/
[freemarker]:   http://freemarker.org/
[rss]:          https://en.wikipedia.org/wiki/RSS
[date-format]:  http://freemarker.org/docs/ref_builtins_date.html
[sitemap]:      https://en.wikipedia.org/wiki/Sitemaps
[sass]:         http://sass-lang.com/