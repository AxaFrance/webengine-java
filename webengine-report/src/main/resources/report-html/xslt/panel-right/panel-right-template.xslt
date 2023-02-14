<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="tab-general-template">
        <xsl:param name="id"/>

        <div id="tab-id-information-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
             onclick="openSelectedTab('tab-id-information-{$id}', 'content-id-information-{$id}');">
            <a href="javascript:void(0)"
               onclick="openSelectedTab('tab-id-information-{$id}', 'content-id-information-{$id}');">
                Information
            </a>
        </div>

        <div id="tab-id-log-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
             onclick="openSelectedTab('tab-id-log-{$id}', 'content-id-log-{$id}');">
            <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-log-{$id}', 'content-id-log-{$id}');">
                Log
            </a>
        </div>
    </xsl:template>

    <xsl:template name="tab-test-case-template">
        <xsl:param name="id"/>

        <div class="tab-bar">

            <xsl:call-template name="tab-general-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <div id="tab-id-test-data-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
                 onclick="openSelectedTab('tab-id-test-data-{$id}', 'content-id-test-data-{$id}');">
                <a href="javascript:void(0)"
                   onclick="openSelectedTab('tab-id-test-data-{$id}', 'content-id-test-data-{$id}');">
                    Test data
                </a>
            </div>

        </div>
    </xsl:template>


    <xsl:template name="tab-action-template">
        <xsl:param name="id"/>

        <div class="tab-bar">

            <xsl:call-template name="tab-general-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <div id="tab-id-context-value-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
                 onclick="openSelectedTab('tab-id-context-value-{$id}', 'content-id-context-value-{$id}');">
                <a href="javascript:void(0)"
                   onclick="openSelectedTab('tab-id-context-value-{$id}', 'content-id-context-value-{$id}');">
                    Context value
                </a>
            </div>

            <div id="tab-id-image-{$id}" class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding"
                 onclick="openSelectedTab('tab-id-image-{$id}', 'content-id-image-{$id}');">
                <a href="javascript:void(0)" onclick="openSelectedTab('tab-id-image-{$id}', 'content-id-image-{$id}');">
                    Image
                </a>
            </div>

        </div>
    </xsl:template>


    <xsl:template name="content-tab-information-template">
        <xsl:param name="id"/>
        <div id="content-id-information-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>Start time :
                <xsl:value-of select="StartTime"/>
            </p>
            <p>End time :
                <xsl:value-of select="EndTime"/>
            </p>
        </div>
    </xsl:template>

    <xsl:template name="content-tab-log-template">
        <xsl:param name="id"/>
        <div id="content-id-log-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>
                <xsl:value-of select="Log"/>
            </p>
        </div>
    </xsl:template>

    <xsl:template name="content-tab-context-value-template">
        <xsl:param name="id"/>
        <div id="content-id-context-value-{$id}" class="tab-content-container class-container-tab "
             style="display:none">
            <xsl:call-template name="array-view-template">
                <xsl:with-param name="parentTag" select="ContextValues"></xsl:with-param>
            </xsl:call-template>
        </div>
    </xsl:template>

    <xsl:template name="content-tab-test-data-template">
        <xsl:param name="id"/>
        <div id="content-id-test-data-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <xsl:call-template name="array-view-template">
                <xsl:with-param name="parentTag" select="TestData"></xsl:with-param>
            </xsl:call-template>
        </div>
    </xsl:template>

    <xsl:template name="content-tab-image-template">
        <xsl:param name="id"/>
        <div id="content-id-image-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>Image à insérer.</p>
        </div>
    </xsl:template>

    <xsl:template name="content-view-general-template-by-id">

        <xsl:param name="id"/>

        <xsl:call-template name="content-tab-information-template">
            <xsl:with-param name="id" select="$id"></xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="content-tab-log-template">
            <xsl:with-param name="id" select="$id"></xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <xsl:template name="content-view-test-case-template-by-id">

        <xsl:param name="id"/>

        <div id="{$id}" class="body-right-container body-right-container-class">

            <xsl:call-template name="tab-test-case-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="content-view-general-template-by-id">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="content-tab-test-data-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

        </div>
    </xsl:template>

    <xsl:template name="content-view-action-template-by-id">

        <xsl:param name="id"/>

        <div id="{$id}" class="body-right-container body-right-container-class">

            <xsl:call-template name="tab-action-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="content-view-general-template-by-id">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="content-tab-context-value-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="content-tab-image-template">
                <xsl:with-param name="id" select="$id"></xsl:with-param>
            </xsl:call-template>

        </div>
    </xsl:template>

    <xsl:template name="array-view-template">
        <xsl:param name="parentTag"/>
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
            <xsl:call-template name="content-view-test-case-template-by-id">
                <xsl:with-param name="id" select="Id"/>
            </xsl:call-template>
        </xsl:for-each>


        <xsl:for-each select="//ActionReport">
            <xsl:call-template name="content-view-action-template-by-id">
                <xsl:with-param name="id" select="Id"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>