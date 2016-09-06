# Architecture

Here you find a more detailed  explanation of the basic concept behind JUberblog
and some details about the implementation.

## The Basic Idea

The publishing concept  is very simple: The publish command  crawls a configured
directory for [Markdown][markdown]  files and converts them to static  HTML in a
web servers document root. So that's all. Nearly.

What about  versioning? No database  at all? No, no  database. In my  opinion it
make no  sense to structure unrelational  data like documents into  a relational
database scheme. Especially if  I have to reassemble it from  the tables back to
a document on  each page request. Then  adding caching and such.  Why not saving
documents as is: A document. In a file.

I've chosen  the [Markdown][markdown] syntax  for the  files because it  is more
readable  and easier  to maintain  than pure  HTML. Also  I've added  some basic
template  features (see  below). And  last  but not  least, if  You really  want
versioning: Use  Git or  any other  VCS for  the directory  where you  save your
Markdown files  for the  blog. Then  you will  have full-blown  version features
with diff and so on.

Ok, version  is done  by a VCS.  What about automatic  publishing of  new posts?
Easy going: Just use  a system like _cron_ or _atd_ and  at a periodic execution
which runs the publisher.

## Publishing Concepts

The basic publishing concept is shown in the image below:

![concept publish](images/concept_publish.png)

What does this show? 

1. You have a repository (git or such) in which you store your blog data:
    - blog posts, sites and drafts
    - CSS, JavaScript and Images
    - templates for generating the content
    - [SASS][sass] files (optional)
    - whatever you need (optional)
2. You have a web server
    - which has its own clone of the blog dat repo.
    - which points its document root to the _public_ direcotry in this repo
3. On that web server you have a _cron_ or _atd_ job running
    - which frequently pulls changes into the blog data repo
    - which periodically executes the _publish_ sub command of JUberblog
4. A local clone of the repository:
    - here you write your blog posts or sites
    - if ready you push to the remote and they will  be published when the cron
      jobs kick in

That's all.

Of course you can imagine various scenarios:

- All of this on a single machine.
- Host the repository somewehere else remote (GitHub, GitLab, gitolite etc.).
- Run  the publishing  on a  different machine and  rsync the  generated content
  (_public_ direcotry) to one or more web server.

## Model

### Data Files

The pure blog data is stored  in plain text files in [Markdown syntax][markdown]
with some useful  extensions (see [Pegdown Processor][pegdown]  for details) and
an optional preprocessor block for meta data. The format is as follows:

    <?juberblog
        // Used for navigation.
        navi: Projects
        // Used for site description.
        description: My personal projects I'm working on
        // Used for site keywords.
        keywords: Projects, Jenkins, Darcs
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

Meta  data in  data files  are stored  in a  pre processor  block (separated  by
`<?juberblog`  and  `?>`). Inside  these  blocks  the prep  rocessor  recognizes
simple key value pairs.

### Pre Processor Grammar

    NL    = [ '\r' ] '\n' ;
    ALPHA = 'a' .. 'Z' ;
    NUM   = '0' .. '9' ;
    ALNUM = ALPHA | NUM ;

    metadata       = '<?juberblog', NL, { key_value_pair NL }, '?>' ;
    comment        = '//' [^NL]* NL ;
    key_value_pair = key, ':', value ;
    key            = ALNUM { ALNUM } ;
    value          = ANY_WORD, NL ;

You can  add key values  what you  like. All key  values from the  pre processor
will be assigned as variables to templates.  But the these special ones are used
internally for content generation:

- `navi`: This will be used to generate navigation links in the layout.
- `description`: This will be used as  description in the meta of the layout and
  the feed data.
- `keywords`: This will be used as keywords in the meta of the layout.

### Runtime Model Representation

TODO

<img alt="model" src="images/model.png"/>

### Templating

TODO

![template and filters](images/template_and_filters.png)

[markdown]: http://daringfireball.net/projects/markdown/syntax
[pegdown]:  https://github.com/sirthias/pegdown#introduction
[sass]:     http://sass-lang.com/
[fmd]:      https://weltraumschaf.github.io/freemarkerdown/