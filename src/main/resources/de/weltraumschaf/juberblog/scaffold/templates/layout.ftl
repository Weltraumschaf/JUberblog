<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <title>${title}</title>
        <meta name="description" content="${description}"/>
        <meta name="keywords" content="${keywords}"/>
        <meta charset="${encoding}"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
        <meta name="viewport" content="width=device-width"/>

        <link rel="stylesheet" type="text/css"
              href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,200,200italic,400italic,700,700italic|Source+Code+Pro"/>
        <link rel="stylesheet" href="${basetUri}css/main.css" type="text/css"/>
        <link rel="stylesheet" href="${basetUri}css/custom.css" type="text/css"/>
        <link rel="shortcut icon" href="${basetUri}img/favicon.ico" type="image/x-icon"/>
        <style>

        </style>
    </head>
    <body>
        <div id="page">
            <header id="branding">
                <hgroup>
                    <h1 id="site-title">${headline}</h1>
                    <h2 id="site-description">${description}</h2>
                    <a id="rss" href="${basetUri}feed.xml" title="RSS">
                        <img src="${basetUri}img/rss.png" width="128" height="128"/>
                    </a>
                </hgroup>

                <img width="940" height="198" src="${basetUri}img/logo.jpg">

                <nav id="access">
                    <div>
                        <ul>
                            <li class="current_page_item">
                                <a title="Home" href="${basetUri}">Home</a>
                            </li>
                            <!-- TODO Fix loop.
                            <% for site in sites %>
                            <li>
                                <a title="${title}" href="${basetUri}">TODO site.navi</a>
                            </li>
                            <% end %>
                            -->
                        </ul>
                    </div>
                </nav>
            </header>

            <div id="main">
                <div id="primary">
                    <div id="content" role="main">
                        ${content}
                    </div>
                </div>

                <div id="secondary">
                    <aside>
                        <h3 class="meta">me</h3>
                        <ul>
                            <li><a href="https://plus.google.com/111699688981457167133/posts" rel="me" title="My profile in Google Plus.">Me at G+</a></li>
                            <li><a href="http://github.com/Weltraumschaf" rel="me" title="My Github profile.">Me at Github</a></li>
                            <li><a href="http://www.kwick.de/Weltraumschaf" rel="me" title="Here is where I work.">Me at KWICK!</a></li>
                            <li><a href="http://www.xing.com/profile/Sven_Strittmatter" rel="me" title="My Xing profile.">Me at Xing</a></li>
                            <li><a href="http://www.linkedin.com/pub/sven-strittmatter/21/751/537" rel="me" title="My LinkedIn profile">Me at LinkedIn</a></li>
                        </ul>
                    </aside>
                    <aside>
                        <h3 class="meta">others</h3>
                        <ul>
                            <li><a href="http://chaosradio.ccc.de/chaosradio_express.html" title="The Chaos Radio Express podcast (german).">Chaosradio Express</a></li>
                            <li><a href="http://dypsilon.com/notes" rel="friend met colleague" title="Business Value driven Web Developer.">Double Ypsilon</a></li>
                            <li><a href="http://www.garanbo.de/" title="Easy organize your warranty papers.">Garanbo</a></li>
                            <li><a href="http://www.heise.de/developer/podcast/" title="The Heise Developer Podcast (german).">Heise Developer Podcast</a></li>
                            <li><a href="http://twitter.com/_stritti_" rel="met sibling" title="My big brothers Twitter.">Mo Brother</a></li>
                        </ul>
                    </aside>
                    <aside>
                        <h3 class="meta">projects</h3>
                        <ul>
                            <li><a href="https://github.com/Weltraumschaf/uberblog">Uberblog</a></li>
                            <li><a href="http://weltraumschaf.github.com/jebnf/">JEBNF</a></li>
                            <li><a href="http://weltraumschaf.github.com/umleto/">UMLeto</a></li>
                            <li><a href="https://github.com/Weltraumschaf/darcs-plugin">Jenkins Darcs Plugin</a></li>
                        </ul>
                    </aside>
                    <aside>
                        <h3 class="meta">version ${version}</h3>
                    </aside>
                </div>

                <footer role="contentinfo">
                    Proudly made without PHP and MySQL, but with some <a title="Source code at GitHub." href="https://github.com/Weltraumschaf/uberblog">Ruby</a>.
                </footer>
            </div>
        </div>
    </body>
</html>