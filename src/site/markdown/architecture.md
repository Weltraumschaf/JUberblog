# Architecture

An overview of the whole architecture.

## Publishing Concept

The publishing concept  is very simple: The publish command  crawls a configured
directory for  [Markdown][1] files  and converts  them to static  HTML in  a web
servers document root. So that's all. Nearly.

What about  versioning? No database  at all? No, no  database. In my  opinion it
make no  sense to structure unrelational  data like documents into  a relational
database scheme. Especially if  I have to reassemble it from  the tables back to
a document on  each page request. Then  adding caching and such.  why not saving
documents as is: A document. In a file.

I've chosen the  Markdown syntax for the  files because it is  more readable and
easier to maintain than pure HTML.  Also I've added some basic template features
(see below). And last  but not least, if You really want  versioning: Use Git or
any other  VCS for  the directory  where you  save your  Markdown files  for the
blog. You have full-blown version features with diff and so on.

Ok, version  is done  by a VCS.  What about automatic  publishing of  new posts?
Easy going:  Just use  a system  like cron or  atd and  at a  periodic execution
which runs the publisher.

<img alt="concept publish" src="images/concept_publish.png"/>

### A Default Setup

All above sounds  very easy and simple. But  for some it might be to  easy. So I
give here an idea of a default setup:

-  A central  repository  where you  save  your blog  data  (the whole  scaffold
   direcotry, except the configuration).
- A server which
    - has a webserver  which serves the `public` direcotry (or other configured)
      from the scaffold.
     -  has a cronjob  which pulls the central  repository (above) and  runs the
        publihser against it.  
- You write  your posts on a  local clone of the  above repo and if  you want to
  puplish it: push to origin.
- And if you  want to scale up: Add more webservers with  cornjobs behind a HTTP
  load balancer

## Model

### Data Files

The pure blog data is stored in  plain text files with [Markdown syntax][1] with
some usefull  extensions (see [Pegdown  Processor][2] for details)  and optional
preprocessor blocks for meta data. The format is as follows:

    <?juberblog
        Navi: Projects
        Description: My personal projects I'm working on
        Keywords: Projects, Jenkins, Darcs
    ?>
    ## The Headline

    Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
    sed diam nonumy eirmod tempor invidunt ut labore et dolore
    magna aliquyam erat, sed diam voluptua. At vero eos et
    accusam et justo duo dolores et ea rebum. Stet clita kasd
    gubergren, no sea takimata sanctus est Lorem ipsum dolor
    sit amet.

    Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
    sed diam nonumy eirmod tempor invidunt ut labore et dolore
    magna aliquyam erat, sed diam voluptua. At vero eos et
    accusam et justo duo dolores et ea rebum.

### Meta Data

Meta  data  in data  files  are  stored in  a  preprocesor  block (separated  by
`<?juberblog` and `?>`). Inside these  blocks the preprocessor recognizes simple
key value pairs.

Syntax:

    NL    = [ '\r' ] '\n' ;
    ALPHA = 'a' .. 'Z' ;
    NUM   = '0' .. '9' ;
    ALNUM = ALPHA | NUM ;

    metadata       = '<?juberblog', NL, { key_value_pair NL }, '?>' ;
    key_value_pair = key, ':', value ;
    key            = ALNUM { ALNUM } ;
    value          = ANY_WORD, NL ;

### Runtime Model Representation

TODO

<img alt="model" src="images/model.png"/>

### Templating

TODO

## Template And Filter

<img alt="template and filters" src="images/template_and_filters.png"/>

[1]: http://daringfireball.net/projects/markdown/syntax
[2]: https://github.com/sirthias/pegdown#introduction