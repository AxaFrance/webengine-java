<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="tab-template">
        <xsl:param name="id" />

        <div class="tab-bar">

            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idInformation-{$id}');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Information</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idLog-{$id}');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Log</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idContextValue-{$id}');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Context value</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idTestData-{$id}');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Test data</div>
            </a>

            <a href="javascript:void(0)" onclick="openSelectedTab(event, 'idImage-{$id}');">
                <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Image</div>
            </a>
        </div>
    </xsl:template>

    <xsl:template name="tab-information-template">
        <xsl:param name="id" />
        <div id="idInformation-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>Start time : 2023-02-01T15:20:26</p>
            <p>End time : 2023-02-01T15:20:26</p>
        </div>
    </xsl:template>

    <xsl:template name="tab-log-template">
        <xsl:param name="id" />
        <div id="idLog-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>
                Log
            </p>
        </div>
    </xsl:template>

    <xsl:template name="tab-context-value-template">
        <xsl:param name="id" />
        <div id="idContextValue-{$id}" class="tab-content-container class-container-tab " style="display:none">
            <table class="table-common">
                <tr>
                    <th>Context</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>TEST</td>
                    <td>VALUE 1</td>
                </tr>
                <tr>
                    <td>TEST</td>
                    <td>VALUE 2</td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template name="tab-test-data-template">
        <xsl:param name="id" />
        <div id="idTestData-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <table class="table-common">
                <tr>
                    <th>Key</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>DATE</td>
                    <td>11/03/2023</td>
                </tr>
                <tr>
                    <td>COMMENT</td>
                    <td>We test the flow</td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template name="tab-image-template">
        <xsl:param name="id" />
        <div id="idImage-{$id}" class="tab-content-container class-container-tab" style="display:none">
            <p>Image à insérer.</p>
        </div>
    </xsl:template>

    <xsl:template name="content-view-template">

        <div class="body-right-container">

            <xsl:call-template name="tab-template">
                <xsl:with-param name="id" select="azerty-1123"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-information-template">
                <xsl:with-param name="id" select="azerty-1123"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-log-template">
                <xsl:with-param name="id" select="azerty-1123"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-context-value-template">
                <xsl:with-param name="id" select="azerty-1123"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-test-data-template">
                <xsl:with-param name="id" select="azerty-1123"></xsl:with-param>
            </xsl:call-template>

            <xsl:call-template name="tab-image-template">
                <xsl:with-param name="id" select="azerty-1123"></xsl:with-param>
            </xsl:call-template>

        </div>
    </xsl:template>

</xsl:stylesheet>