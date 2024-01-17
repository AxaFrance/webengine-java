<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="status-template">
        <xsl:param name="status"/>
        <xsl:param name="label"/>
        <xsl:param name="id"/>
        <xsl:param name="summary"/>

        <xsl:if test="$summary='active'">
            <summary onclick=";openTestResultContent(event,'{$id}');">
                <xsl:if test="$status='Passed'">
                    <i class="fa fa-check icon-green icon-around fa-lg" aria-hidden="true"></i>
                    <xsl:value-of select="$label"/>
                </xsl:if>
                <xsl:if test="$status='Failed'">
                    <i class="fa fa-times icon-red icon-around fa-lg" aria-hidden="true"></i>
                    <xsl:value-of select="$label"/>
                </xsl:if>
                <xsl:if test="$status='Ignored'">
                    <i class="fa fa-circle icon-gray icon-around fa-lg" aria-hidden="true"></i>
                    <xsl:value-of select="$label"/>
                </xsl:if>
            </summary>
        </xsl:if>

        <xsl:if test="$summary='not-active'">
            <a onclick=";openTestResultContent(event,'{$id}');">
                <xsl:if test="$status='Passed'">
                    <i class="fa fa-check icon-green icon-around fa-lg" aria-hidden="true"></i>
                    <xsl:value-of select="$label"/>
                </xsl:if>
                <xsl:if test="$status='Failed'">
                    <i class="fa fa-times icon-red icon-around fa-lg" aria-hidden="true"></i>
                    <xsl:value-of select="$label"/>
                </xsl:if>
                <xsl:if test="$status='Ignored'">
                    <i class="fa fa-circle icon-gray icon-around fa-lg" aria-hidden="true"></i>
                    <xsl:value-of select="$label"/>
                </xsl:if>
            </a>
        </xsl:if>

    </xsl:template>

</xsl:stylesheet>