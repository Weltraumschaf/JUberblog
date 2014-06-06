# TODO

## home dir

- $JUBERBLOG_HOME is ~/.juberblog
    - lock file to prevent multiple processes: $JUBERBLOG_HOME/lock
    - file to hold metadata about published sites/posts: $JUBERBLOG_HOME/published
        - JSON https://sites.google.com/site/gson/gson-user-guide
        - serialized Map<String, Page>, key is data file name
        
## Features

- use command from JCommander
- implement publish sub command
    - read and format Markdown file from data folder
        - site
        - post
    - embed formatted content in layout template and format
- Categories
- RSS

## Bugs

## Publish Sub Command

- publish all posts
    - read all data files
    - preprocess meta data
    - extract slug
    - check if already published
        - if exists publish if purge
        - else publish file
            - assign meta data
            - write file
- if sites publish all sites
    - same as post
- generate feed.xml
- generate sitemap.xml