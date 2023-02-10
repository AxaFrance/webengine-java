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

                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous"/>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous"/>

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
                        <xsl:call-template name="general-information-template"/>
                    </div>

                    <div class="body-content-container">
                        <div class="tree-view-container">
                            <xsl:call-template name="tree-view-template"/>
                        </div>

                        <div class="content-view">
<!--                            <xsl:call-template name="content-view-template"/>-->
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

    <xsl:template name="general-information-template">
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
            <span class="badge badge-secondary badge-action">Number of test case : <xsl:value-of select="TestSuiteReport/NumberOfTestcase"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action "><i class="fa fa-check badge-action-green" aria-hidden="true"></i> Succes test : <xsl:value-of select="TestSuiteReport/Passed"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action "><i class="fa fa-times badge-action-red" aria-hidden="true"></i> Failed test : <xsl:value-of select="TestSuiteReport/Failed"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action "><i class="fa fa-circle badge-action-blue" aria-hidden="true"></i> Ignored test : <xsl:value-of select="TestSuiteReport/Failed"/></span>
        </div>
    </xsl:template>

    <xsl:template name="status-template">
        <xsl:param name="status" />
        <xsl:param name="label" />
        <xsl:if test="$status='Passed'">
            <i class="fa fa-check badge-action-green" aria-hidden="true"></i>
        </xsl:if>
        <xsl:if test="$status='Failed'">
            <i class="fa fa-times badge-action-red" aria-hidden="true"></i>
        </xsl:if>
        <xsl:if test="$status='Ignored'">
            <i class="fa fa-circle badge-action-blue" aria-hidden="true"></i>
        </xsl:if>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$label" />
    </xsl:template>

    <xsl:template name="sub-tree-view-template">
        <xsl:param name="firstNode" />
        <ul class="nested">
            <xsl:for-each select="$firstNode">
                <li>
                    <xsl:choose>
                        <xsl:when test="SubActionReports">
                            <span class="caret">
                                <xsl:call-template name="status-template">
                                    <xsl:with-param name="status" select="Result" />
                                    <xsl:with-param name="label" select="Name" />
                                </xsl:call-template>
                            </span>
                            <xsl:call-template name="sub-tree-view-template">
                                <xsl:with-param name="firstNode" select="SubActionReports/ActionReport" />
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="status-template">
                                <xsl:with-param name="status" select="Result" />
                                <xsl:with-param name="label" select="Name" />
                            </xsl:call-template>
                        </xsl:otherwise>
                    </xsl:choose>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>

    <xsl:template name="tree-view-template">
        <ul id="idTree">
            <xsl:for-each select="TestSuiteReport/TestResult">
                <li>
                    <span class="caret">
                        <xsl:call-template name="status-template">
                            <xsl:with-param name="status" select="Result" />
                            <xsl:with-param name="label" select="TestName" />
                        </xsl:call-template>
                    </span>
                    <xsl:call-template name="sub-tree-view-template">
                        <xsl:with-param name="firstNode" select="ActionReports/ActionReport" />
                    </xsl:call-template>
                </li>
            </xsl:for-each >
        </ul>
    </xsl:template>









</xsl:stylesheet>