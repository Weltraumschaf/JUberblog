# Usage

JUberbog is a  pure command line tool. It provides  subcommands. This means that
invoking solely `juberblog`  does almost nothing than show you  some help or the
version. To get some help invoke:

    $ juberblog -h

The subcommands provided are: `install`, `create`,  `publish`. To get help for a
particular subcommand invoke:

    $ juberblog SUB_COMMAND -h

Most of the options are available as  short option with one dash and long option
with two dashes.

## Install Sub Command

The install  sub command creates  a scaffold directory  for your blog  data. The
details    of   this    directory    is   described    in   the    [installation
guide](install.html). Basically this  commands need only a  location as argument
where to create the scaffold. The directory must exist.

The  other options  are only  for update  strategies: You  can either  force the
installation which  means all  existing files  will be  overwritten. Or  you can
update which  means existing  files will  be backed up  by renaming.  The latter
option  is only  useful  if you  have  no  version controll  in  your blog  data
directory.

For example to create a new blog data directory run:

    $ mkdir myblog
    $ juberblog install -l myblog

If  there is  a  newer version  of  JUberblog which  brings  new scaffold  files
(templates, example config etc.) you can run:

    $ juberblog install -l myblog -f

After that  files may be changed  or added. You can  check this by your  VCS, if
you have the directory  under version control. If you do  not have the directory
under  version controll,  which I  do  not recommend,  you should  use the  `-u`
option to update:

    $ juberblog install -l myblog -u

All files which would be overwritten are copied with a `.bak` suffix.

## Create Sub Command

This command creates  [Markdown][markdown] stubs into your  blog data directory.
It  also generates  a  propper slugged  and timestamped  filename  in the  right
directory (either post or site, or one of these in drafts).

## Publish Sub Command

TODO

[markdown]: https://daringfireball.net/projects/markdown/