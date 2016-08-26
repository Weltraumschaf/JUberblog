# Usage

JUberbog is a pure command line tool.  It provides sub commands. This means that
invoking solely `juberblog`  does almost nothing than show you  some help or the
version. To get some help invoke:

    $ juberblog -h

The subcommands provided are: `install`, `create`,  `publish`. To get help for a
particular subcommand invoke:

    $ juberblog SUB_COMMAND -h

Most of the options  are available as shot option with one  dash and long option
with two dashes.

## Install Sub Command

The install  sub command creates  a scaffold directory  for your blog  data. The
details    of   this    directory    is   described    in   the    [installation
guide](install.html).  Basically  the this  commands  need  only a  location  as
argument where to create the scaffold. The directory must exist.

The  other options  are only  for update  startegies: You  can either  force the
installation which  means all  existing files  will be  overwritten. Or  you can
update which  means existing  files will  be backed up  by renaming.  The latter
option  is only  useful  if you  have  no  version controll  in  your blog  data
directory.

## Create Sub Command

This command creates  [Markdown][markdown] stubs into your  blog data directory.
It  also generates  a  propper slugged  and timestamped  filename  in the  right
directory (either post or site, or one of these in drafts).

## Publish Sub Command

TODO

[markdown]: https://daringfireball.net/projects/markdown/