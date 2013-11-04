# Usage

JUberbog is a pure command line tool. It provides sub commands:

    $ juberblog -h
    $ Usage: juberblog [-v|--version] [-h|--help] [create|publish|install]

## Install Sub Command: `juberblog install`

TBD

## Create Sub Command: `juberblog create`

    -c, --config <FILE>     Config file to use. [required]
    -t, --title TITLE       Title of the blog post.
    -d, --draft             Will mark the file name as draft.
    -s, --site              Will create a site instead of a post.
    -v, --verbose           Tell you more.
    -h, --help              Show this message.

## Publish Sub Command: `juberblog publish`

    -c, --config <FILE>     Config file to use. [required]
    -p, --purge             Regenerate all blog posts.
    -q, --quiet             Be quiet and don't post to social networks.
    -s, --sites             Generate static sites.
    -d, --draft             Publish drafts.
    -v, --verbose           Tell you more.
    -h, --help              Show this message.
