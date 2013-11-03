# Architecture

An overview of the whole architecture.

## Publishing Concept 

<img alt="concept publish" src="images/concept_publish.png"/>

## Model

### Data Files

The pure blog data is stored in plain text files with [Markdown syntax][1] with some 
usefull extensions (see [Pegdown Processor][2] for details) and optional preprocessor
blocks for meta data. The format is as follows:

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

Meta data in data files are stored in a preprocesor block (separated by `<?juberblog` and `?>`).
Inside these blocks the preprocessor recognizes simple key value pairs.

Syntax:

    NL    = [ '\r' ] '\n' ;
    ALPHA = 'a' .. 'Z' ;
    NUM   = '0' .. '9' ;
    ALNUM = ALPHA | NUM ;
    
    metadata       = '<?juberblog', NL, { key_value_pair NL }, '?>' ;
    key_value_pair = key, ':', value ;
    key            = ALNUM { ALNUM } ;
    value          = ANY_WORD, NL ;

### Rubntime Model Representation

<img alt="model" src="images/model.png"/>

## Template And Filter

<img alt="template and filters" src="images/template_and_filters.png"/>

[1]: http://daringfireball.net/projects/markdown/syntax
[2]: https://github.com/sirthias/pegdown#introduction