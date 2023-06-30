<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template name="tab-test-case-template">
        <xsl:param name="id"/>
        <div class="tab-bar">
            <div id="tab-id-information-{$id}" class="tab-label tablink "
                 onclick="openSelectedTab('tab-id-information-{$id}', 'content-id-information-{$id}');">
                Information
            </div>
            <div id="tab-id-log-{$id}" class="tab-label tablink "
                 onclick="openSelectedTab('tab-id-log-{$id}', 'content-id-log-{$id}');">
                Log
            </div>
            <div id="tab-id-test-data-{$id}" class="tab-label tablink "
                 onclick="openSelectedTab('tab-id-test-data-{$id}', 'content-id-test-data-{$id}');">
                Test data
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>