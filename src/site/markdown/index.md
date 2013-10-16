# Welcome to the JUberblog Project

## Command Line Options

    -v, --verbose           Tell you more.
    -h, --help              Show this message.

Command to publish the blog: `juberblog publish`

    -c, --config <FILE>     Config file to use. [required]
    -p, --purge             Regenerate all blog posts.
    -q, --quiet             Be quiet and don't post to social networks.
    -s, --sites             Generate static sites.
    -d, --draft             Publish drafts.

Command to create drafts: `juberblog create`

    -c, --config <FILE>     Config file to use. [required]
    -t, --title TITLE       Title of the blog post.
    -d, --draft             Will mark the file name as draft.
    -s, --site              Will create a site instead of a post.
    
## Story Behind

Long time ago (roundabout seven years) I blogging with Wordpress and programming with PHP. View years ago I
realized that PHP is a piece of crap. In consequence of that Wordpress is also a piece of crap. Also I
realized that I do not need the most features of Wordpress. What I want was an easy way to write my post, version
them and publish them as HTML. As a programmer I'm used to use a version control system for my source code.
So why not using it for my blog posts to? Also I'm used to write Markdown files (mostly the documentation of my
software). So why put my blog texts into tables and rows of an SQL database only for putting them together on
each page request. So I decided to make my blog as a repository of Markdownfiles from which I generate static
HTML files regular. My first approach was [Uberblog][]: A command line Ruby script collection to create and
publish the static files of my blog. JUberblog is the next step: Divide the code from the content (Uberblog
combines both in one repository). I've chosen Java as platform because I've earn my mney with Java programming
and it is a good practice for me. Also I can provide a self contained binary with all dependencies. With
Ruby I've had several problems to set up all dependencies for [Uberblog][] on a new machine.
