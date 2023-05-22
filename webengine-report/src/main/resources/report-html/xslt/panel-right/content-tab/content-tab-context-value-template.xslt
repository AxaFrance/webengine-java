<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="../general/array-view-template.xslt"/>

    <xsl:template name="content-tab-context-value-template">
        <xsl:param name="id"/>
        <div id="content-id-context-value-{$id}" class="tab-content-container class-container-tab "
             style="display:none">
            <xsl:call-template name="array-view-template">
                <xsl:with-param name="parentTag" select="ContextValues"></xsl:with-param>
            </xsl:call-template>
        </div>
    </xsl:template>



</xsl:stylesheet>