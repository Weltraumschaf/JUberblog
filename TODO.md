# TODO

## Site

- add Download section to site (move dist/) in site

## Features

- implement create sub command
    - create Markdown stub in data folder
        - site
        - post
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