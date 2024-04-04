<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="tab-action-template">
        <xsl:param name="id"/>
        <div class="tab-bar">
            <div id="tab-id-information-{$id}" class="tab-label tablink "
                 onclick="openSelectedTab('tab-id-information-{$id}', 'content-id-information-{$id}');">
                <h7>Information / Screenshot</h7>
            </div>
            <div id="tab-id-log-{$id}" class="tab-label tablink "
                 onclick="openSelectedTab('tab-id-log-{$id}', 'content-id-log-{$id}');">
                <h7>Log</h7>
            </div>
            <div id="tab-id-context-value-{$id}" class="tab-label tablink "
                 onclick="openSelectedTab('tab-id-context-value-{$id}', 'content-id-context-value-{$id}');">
                <h7>Context value</h7>
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>