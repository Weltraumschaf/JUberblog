<!DOCTYPE html>
<html lang="${language}">
    <head>
        <meta name="robots" content="all"/>
        <meta name="description" content="${description}"/>
        <meta name="keywords" content="${keywords}"/>
        <meta charset="${encoding}"/>
        <link href="${baseUrl}/css/main.css" rel="stylesheet" type="text/css" media="screen"/>
        <link href="${baseUrl}/img/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    </head>

    <body>
        <header>
            <h1>${blogTitle}</h1>
            <h2>${blogDescription}</h2>
        </header>

        <section>
        ${content}
        </section>

        <footer>
            Powered by JUberblog.
        </footer>

        <script type="text/javascript" src="${baseUrl}/js/main.js"></script>
    </body>
</html>