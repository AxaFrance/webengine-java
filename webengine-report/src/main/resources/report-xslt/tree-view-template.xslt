<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="status-template">
        <xsl:param name="status" />
        <xsl:param name="label" />
        <xsl:param name="id" />
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
        <a href="javascript:void(0)" onclick="showTabByTreeId({$id});"><xsl:value-of select="$label" /></a>
    </xsl:template>

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