# Welcome to the JUberblog Project

JUberblog is a  command line based blog engine which  publishs static HTML files
from given  Markdown files.  The simple  idea behind is:  Only do  the necessary
things. So  the blog does  not care about storage  or versioning of  the content
data. This  aspect is leaved to  other tools which  can do this better  (such as
Git for example).

## Story Behind

Long time ago  (roundabout seven years) I blogged with  Wordpress and programmed
with PHP. View years ago I realized that  PHP is a piece of crap. In consequence
of that Wordpress  is also a piece of  crap. Also I realized that I  do not need
the most features  of Wordpress. What I want  is an easy way to  write my posts,
version  them and  publish them  as HTML.  As  a programmer  I'm used  to use  a
version control  system for  my source  code. So why  not using  it for  my blog
posts to? Also I'm used to write  Markdown files (mostly the documentation of my
software is written in Markdown). So why  put my blog texts into tables and rows
of an  SQL database only for  putting them together  on each page request.  So I
decided to make my  blog as a repository of Markdownfiles  from which I generate
static  HTML  files  periodicly  (with  crontab e.g.).  My  first  approach  was
[Uberblog][1]: A command  line Ruby script collection to create  and publish the
static files of  my blog. JUberblog is  the next step: Divide the  code from the
content  (Uberblog  combines  both  in  one repository).  I've  chosen  Java  as
platform  because I've  earn my  money with  Java programming  so it  is a  good
coding practice  for me.  Also I can  provide a self  contained binary  with all
dependencies (You only need a Java JVM).  With Ruby I've had several problems to
set up all  dependencies for [Uberblog][1] on  a new machine. I've  also had lot
of problems setting  up other Ruby tools  from scratch. Sadly there is  a lot of
API version hassle in the Ruby world.

[1]: https://github.com/Weltraumschaf/uberblog
