<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="tab-action-template">
        <xsl:param name="id"/>

        <div class="tab-bar">

            <div id="tab-id-information-{$id}" class="tab-label w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
                 onclick="openSelectedTab('tab-id-information-{$id}', 'content-id-information-{$id}');">
                <a href="javascript:void(0)"
                   onclick="openSelectedTab('tab-id-information-{$id}', 'content-id-information-{$id}');">
                    <label class="tab-label"> Information / Screenshot </label>
                </a>
            </div>

            <div id="tab-id-log-{$id}" class="tab-label w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
                 onclick="openSelectedTab('tab-id-log-{$id}', 'content-id-log-{$id}');">
                <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-log-{$id}', 'content-id-log-{$id}');">
                    <label class="tab-label"> Log </label>
                </a>
            </div>

            <div id="tab-id-context-value-{$id}" class="tab-label w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
                 onclick="openSelectedTab('tab-id-context-value-{$id}', 'content-id-context-value-{$id}');">
                <a href="javascript:void(0)"
                   onclick="openSelectedTab('tab-id-context-value-{$id}', 'content-id-context-value-{$id}');">
                    <label class="tab-label"> Context value </label>
                </a>
            </div>

        </div>
    </xsl:template>

</xsl:stylesheet>