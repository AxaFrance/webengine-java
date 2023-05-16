<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="sub-header-template">
        <div class="badge-container">
            <span class="badge badge-secondary badge-action"><i class="fa fa-bars" aria-hidden="true"></i> Test suite</span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action"><i class="fa fa-hourglass-start" aria-hidden="true"></i> Start time : <xsl:value-of select="TestSuiteReport/StartTime"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action"><i class="fa fa-hourglass-end" aria-hidden="true"></i> End time : <xsl:value-of select="TestSuiteReport/EndTime"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action"><i class="fa fa-desktop" aria-hidden="true"></i> Hostname : <xsl:value-of select="TestSuiteReport/HostName"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action"><i class="fa fa-square" aria-hidden="true"></i> Number of test case : <xsl:value-of select="TestSuiteReport/NumberOfTestcase"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action "><i class="fa fa-check badge-action-green" aria-hidden="true"></i> Succes tests : <xsl:value-of select="TestSuiteReport/Passed"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action "><i class="fa fa-times badge-action-red" aria-hidden="true"></i> Failed tests : <xsl:value-of select="TestSuiteReport/Failed"/></span>
        </div>

        <div class="badge-container">
            <span class="badge badge-secondary badge-action "><i class="fa fa-circle badge-action-blue" aria-hidden="true"></i> Ignored tests : <xsl:value-of select="TestSuiteReport/Failed"/></span>
        </div>
    </xsl:template>

</xsl:stylesheet>