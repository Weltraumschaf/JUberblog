# Welcome to the JUberblog Project

JUberblog is a command line based  blog engine which publishes static HTML files
from given Markdown files.  The simple idea behind it is:  Only do the necessary
things. So  the blog does  not care about storage  or versioning of  the content
data. This  aspect is leaved to  other tools which  can do this better  (such as
Git for example).

## Where Go Go Next

First you  need to [install  and configure](installation.html) your  blog. After
that you can start.  For a brief overview how to use  the JUberblog command line
tool see the [usage](usage.html).

## Story Behind

Long time ago  (roundabout seven years) I blogged with  Wordpress and programmed
with PHP. View years ago I realized that  PHP is a piece of crap. In consequence
of that Wordpress  is also a piece of  crap. Also I realized that I  do not need
the most features  of Wordpress. What I want  is an easy way to  write my posts,
version them and publish them as HTML.

As a programmer I'm used to use a  version control system for my source code. So
why  not   using  it   for  my  blog   posts  too?  Also   I'm  used   to  write
[Markdown][markdown] files (mostly  the documentation of my  software is written
in [Markdown][markdown]). So  why put my blog  texts into tables and  rows of an
SQL database only for  putting them together on each page  request. So I decided
to make my blog  as a repository of Markdown files from  which I generate static
HTML files periodically (with crontab e.g.).

My  first  approach  was  [Uberblog][uberblog]:  A  command  line  Ruby  scripts
collection to create and  publish the static files of my  blog. JUberblog is the
next  step: Divide  the code  from the  content (Uberblog  combines both  in one
repository). I've chosen  Java as platform because I've earn  my money with Java
programming so it  is a good coding practice  for me. Also I can  provide a self
contained binary  with all dependencies (You  only need a [Java  VM][jvm]). With
Ruby   I've   had   several   problems   to  set   up   all   dependencies   for
[Uberblog][uberblog] on a new machine. I've  also had lot of problems setting up
other Ruby  tools from scratch. Sadly  there is a  lot of API version  hassle in
the Ruby world.

For installation  instructions go  [here](install.html). For a  there is  also a
complete [usage](usage.html)  overview for JUberblog  command line tool.  If you
are   interested   in   the   details    how   it   is   implemented   see   the
[architecture](architecture.html) page.

[uberblog]: https://github.com/Weltraumschaf/uberblog
[markdown]: https://daringfireball.net/projects/markdown/
[jvm]:      http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html