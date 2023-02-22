<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="content-tab-information-template">
        <xsl:param name="id"/>
        <div id="content-id-information-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>Start time :
                <xsl:value-of select="StartTime"/>
            </p>
            <p>End time :
                <xsl:value-of select="EndTime"/>
            </p>

            <xsl:choose>
                <xsl:when test="Screenshots">
                    <xsl:for-each select="ScreenshotReport">
                        <xsl:variable name="idImg" select="Id" />
                        <img src="img/{$idImg}" class="img-thumbnail" width="304" height="236"/>
                    </xsl:for-each>
                </xsl:when>
            </xsl:choose>

        </div>
    </xsl:template>

</xsl:stylesheet>