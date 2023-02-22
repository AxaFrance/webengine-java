<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="status-template.xslt"/>

    <xsl:template name="sub-tree-view-template">
        <xsl:param name="firstNode" />
        <ul class="nested">
            <xsl:for-each select="$firstNode">
                <li>
                    <xsl:choose>
                        <xsl:when test="SubActionReports">
                            <span class="caret"></span>
                            <xsl:call-template name="status-template">
                                <xsl:with-param name="status" select="Result" />
                                <xsl:with-param name="label" select="Name" />
                                <xsl:with-param name="id" select="Id" />
                            </xsl:call-template>

                            <xsl:call-template name="sub-tree-view-template">
                                <xsl:with-param name="firstNode" select="SubActionReports/ActionReport" />
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="status-template">
                                <xsl:with-param name="status" select="Result" />
                                <xsl:with-param name="label" select="Name" />
                                <xsl:with-param name="id" select="Id" />
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
                    <span class="caret"></span>
                    <xsl:call-template name="status-template">
                        <xsl:with-param name="status" select="Result" />
                        <xsl:with-param name="label" select="TestName" />
                        <xsl:with-param name="id" select="Id" />
                    </xsl:call-template>

                    <xsl:call-template name="sub-tree-view-template">
                        <xsl:with-param name="firstNode" select="ActionReports/ActionReport" />
                    </xsl:call-template>
                </li>
            </xsl:for-each >
        </ul>
    </xsl:template>

</xsl:stylesheet>