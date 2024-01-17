<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:we="http://www.axa.fr/WebEngine/2022">

    <xsl:import href="status-template.xslt"/>

    <xsl:template name="sub-tree-view-template">
        <xsl:param name="firstNode"/>
        <ul>
            <xsl:for-each select="$firstNode">
                <li>
                    <xsl:choose>
                        <xsl:when test="we:SubActionReports">
                            <details>
                                <xsl:call-template name="status-template">
                                    <xsl:with-param name="status" select="we:Result"/>
                                    <xsl:with-param name="label" select="we:Name"/>
                                    <xsl:with-param name="id" select="we:Id"/>
                                    <xsl:with-param name="summary" select="'active'"/>
                                </xsl:call-template>
                                <xsl:call-template name="sub-tree-view-template">
                                    <xsl:with-param name="firstNode" select="we:SubActionReports/we:ActionReport"/>
                                </xsl:call-template>
                            </details>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="status-template">
                                <xsl:with-param name="status" select="we:Result"/>
                                <xsl:with-param name="label" select="we:Name"/>
                                <xsl:with-param name="id" select="we:Id"/>
                                <xsl:with-param name="summary" select="'not-active'"/>
                            </xsl:call-template>
                        </xsl:otherwise>
                    </xsl:choose>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>

    <xsl:template name="tree-view-template">
        <div id="body-left-container" class="body-left-container">
            <div id='id-tree-header-container' class="tree-header-container">
                <div class="tree-title-container">
                    <h1>Test cases</h1>
                </div>
                <div class="tree-action-container">
                    <div class="tree-one-action-container dot" onclick="expandAll()">
                        <i class="fa fa-plus icon-action-tree"></i>
                    </div>
                    <div class="tree-one-action-container dot" onclick="collapseAll()">
                        <i class="fa fa-minus icon-action-tree"></i>
                    </div>
                </div>
            </div>
            <div id='id-tree-container' class="tree-container">
                <ul class="tree">
                    <xsl:for-each select="we:TestResult">
                        <li>
                            <details>
                                <xsl:call-template name="status-template">
                                    <xsl:with-param name="status" select="we:Result"/>
                                    <xsl:with-param name="label" select="we:TestName"/>
                                    <xsl:with-param name="id" select="we:Id"/>
                                    <xsl:with-param name="summary" select="'active'"/>
                                </xsl:call-template>

                                <xsl:call-template name="sub-tree-view-template">
                                    <xsl:with-param name="firstNode" select="we:ActionReports/we:ActionReport"/>
                                </xsl:call-template>
                            </details>
                        </li>
                    </xsl:for-each>
                </ul>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>