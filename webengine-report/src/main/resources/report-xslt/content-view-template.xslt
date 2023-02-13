<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="tab-template">
        <xsl:param name="id" />

        <div class="tab-bar">

            <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-information-{$id}', 'content-id-information-{$id}');">
                <div id="tab-id-information-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Information</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-log-{$id}', 'content-id-log-{$id}');">
                <div id="tab-id-log-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Log</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-context-value-{$id}', 'content-id-context-value-{$id}');">
                <div id="tab-id-context-value-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Context value</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-test-data-{$id}', 'content-id-test-data-{$id}');">
                <div id="tab-id-test-data-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Test data</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-image-{$id}', 'content-id-image-{$id}');">
                <div id="tab-id-image-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Image</div>
            </a>
        </div>
    </xsl:template>

    <xsl:template name="tab-information-template">
        <xsl:param name="id" />
        <div id="content-id-information-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>Start time : <xsl:value-of select="StartTime" /></p>
            <p>End time : <xsl:value-of select="EndTime" /></p>
        </div>
    </xsl:template>

    <xsl:template name="tab-log-template">
        <xsl:param name="id" />
        <div id="content-id-log-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>
                <xsl:value-of select="Log" />
            </p>
        </div>
    </xsl:template>

    <xsl:template name="tab-context-value-template">
        <xsl:param name="id" />
        <div id="content-id-context-value-{$id}" class="tab-content-container class-container-tab " style="display:none">
            <xsl:call-template name="array-view-template">
                <xsl:with-param name="parentTag" select="ContextValues"></xsl:with-param>
            </xsl:call-template>
        </div>
    </xsl:template>

    <xsl:template name="tab-test-data-template">
        <xsl:param name="id" />
        <div id="content-id-test-data-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <xsl:call-template name="array-view-template">
                <xsl:with-param name="parentTag" select="TestData"></xsl:with-param>
            </xsl:call-template>
        </div>
    </xsl:template>

    <xsl:template name="tab-image-template">
        <xsl:param name="id" />
        <div id="content-id-image-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>Image à insérer.</p>
        </div>
    </xsl:template>

    <xsl:template name="content-view-template-by-id">

        <xsl:param name="id" />

        <div id="{$id}" class="body-right-container body-right-container-class">

            <xsl:call-template name="tab-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-information-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-log-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-context-value-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-test-data-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-image-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

        </div>
    </xsl:template>

    <xsl:template name="array-view-template">
        <xsl:param name="parentTag" />
        <table class="table-common">
            <tr>
                <th>Key</th>
                <th>Value</th>
            </tr>
            <xsl:choose>
                <xsl:when test="$parentTag">
                    <xsl:for-each select="$parentTag/Variable">
                        <tr>
                            <td>
                                <xsl:value-of select="Name"/>
                            </td>
                            <td>
                                <xsl:value-of select="Value"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </xsl:when>
            </xsl:choose>
        </table>
    </xsl:template>

    <xsl:template name="content-view-template">
        <xsl:for-each select="TestSuiteReport/TestResult">
            <xsl:call-template name="content-view-template-by-id">
                <xsl:with-param name="id" select="Id" />
            </xsl:call-template>
        </xsl:for-each >


        <xsl:for-each select="//ActionReport">
            <xsl:call-template name="content-view-template-by-id">
                <xsl:with-param name="id" select="Id" />
            </xsl:call-template>
        </xsl:for-each >
    </xsl:template>

</xsl:stylesheet>