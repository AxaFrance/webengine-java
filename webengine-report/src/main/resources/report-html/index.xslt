<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <meta charset="utf-8" />
                <link href="assets/css/global.css" rel="stylesheet" />
                <link href="assets/css/header.css" rel="stylesheet" />
                <link href="assets/css/table.css" rel="stylesheet" />
                <link href="assets/css/badge.css" rel="stylesheet" />
                <link href="assets/css/banner-information.css" rel="stylesheet" />
                <link href="assets/css/body-content.css" rel="stylesheet" />
                <link href="assets/css/tree.css" rel="stylesheet" />
                <link href="assets/css/content-view.css" rel="stylesheet" />
                <link href="assets/css/tab.css" rel="stylesheet" />
                <script src="assets/js/global-js.js"></script>

                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">

                <title>Webengine report viewer</title>
            </head>

            <body>
                <div id="root">

                    <div class="site-header common-font">
                        <div class="site-header-section site-header-section-left ">
                            <i class="fa fa-registered fa-3x site-header-icon site-header-icon-application"></i>
                            <div class="site-header-item">Webengine report viewer</div>
                        </div>
                    </div>

                    <div class="general-information scrollbar">
                        <xsl:apply-templates select="general-information-template"/>
                    </div>

                    <div class="body-content-container">
                        <div class="tree-view-container">
                            <xsl:apply-templates select="tree-view-template"/>
                        </div>

                        <div class="content-view">
                            <xsl:apply-templates select="content-view-template"/>
                        </div>
                    </div>
                </div>

                <script src='https://kit.fontawesome.com/a076d05399.js'></script>
                <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
                <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>

                <script>
                    openSelectedLineInTree();
                </script>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="general-information-template">
        <div class="badge-container">
            <span class="badge badge-secondary badge-action">Test suite</span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action">Start time : <xsl:value-of select="TestSuiteReport/StartTime"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action">End time : <xsl:value-of select="TestSuiteReport/EndTime"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action">Hostname : <xsl:value-of select="TestSuiteReport/HostName"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action">Number of test case : 30</span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action ">
                <i class="fa fa-check badge-action-green" aria-hidden="true"></i> Succes test : 10 (33%)
            </span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action ">
                <i class="fa fa-times badge-action-red" aria-hidden="true"></i> Failed test : 10 (33%)
            </span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action ">
                <i class="fa fa-circle badge-action-blue" aria-hidden="true"></i> Ignored test : 10 (33%)
            </span>
        </div>
    </xsl:template>

    <xsl:template match="tree-view-template">
        <ul id="idTree">
            <li><span class="caret"><i class="fa fa-times badge-action-red" aria-hidden="true"></i> Scénario : RC collectivité publique</span>
                <ul class="nested">
                    <li><span class="caret"><i class="fa fa-check badge-action-green" aria-hidden="true"></i> Phase de connexion</span>
                        <ul class="nested">
                            <li><i class="fa fa-check badge-action-green" aria-hidden="true"></i> Ouverture de la page</li>
                            <li><i class="fa fa-check badge-action-green" aria-hidden="true"></i> Je renseigne le login</li>
                            <li><i class="fa fa-check badge-action-green" aria-hidden="true"></i> Je renseigne le password</li>
                            <li><i class="fa fa-check badge-action-green" aria-hidden="true"></i> Je clique sur le bouton valider</li>
                        </ul>
                    </li>

                    <li><span class="caret"><i class="fa fa-times badge-action-red" aria-hidden="true"></i> Rechercher un contrat</span>
                        <ul class="nested">
                            <li><i class="fa fa-times badge-action-red" aria-hidden="true"></i> Je saisi le numéro de contrat</li>
                            <li><i class="fa fa-circle badge-action-blue" aria-hidden="true"></i> Je saisi le type de contrat</li>
                            <li><i class="fa fa-circle badge-action-blue" aria-hidden="true"></i> Je saisi la date de réclamation</li>
                            <li><i class="fa fa-circle badge-action-blue" aria-hidden="true"></i> Je clique sur le bo</li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>
    </xsl:template>

    <xsl:template match="content-view-template">

        <div class="tab-bar">
            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idInformation');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Information</div>
            </a>
            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idContextValue');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Context value</div>
            </a>
            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idImage');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Image</div>
            </a>
        </div>

        <div id="idInformation" class="tab-content-container class-container-tab" style="display:none">
            <p>
                Exception in thread "main" java.lang.NullPointerException
                at Printer.printString(Printer.java:13)
                at Printer.print(Printer.java:9)
                at Printer.main(Printer.java:19)
            </p>
        </div>

        <div id="idContextValue" class="tab-content-container class-container-tab " style="display:none">
            <table class="table-common">
                <tr>
                    <th>Context</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>TEST</td>
                    <td>VALUE 1</td>
                </tr>
                <tr>
                    <td>TEST</td>
                    <td>VALUE 2</td>
                </tr>
            </table>
        </div>

        <div id="idImage" class="tab-content-container class-container-tab" style="display:none">
            <p>Image à insérer.</p>
        </div>
    </xsl:template>

</xsl:stylesheet>