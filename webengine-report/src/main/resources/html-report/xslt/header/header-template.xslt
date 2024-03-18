<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:we="http://www.axa.fr/WebEngine/2022">

    <xsl:template name="header-template">
        <i class="fa fa-registered fa-3x site-header-icon site-header-icon-application"></i>
        <h2 class="site-header-item">Webengine report viewer</h2>
        <div class="stat-header">
            <div class="dot-stat-green">
                <script>
                    var testPassed = <xsl:value-of select="we:Passed"/> ;
                    document.write(testPassed * 100 / <xsl:value-of select="we:NumberOfTestcase"/> + "%");
                </script>
            </div>
            <div class="dot-stat-red">
                <script>
                    var testFailed = <xsl:value-of select="we:Failed"/> ;
                    document.write(testFailed * 100 / <xsl:value-of select="we:NumberOfTestcase"/> + "%");
                </script>

            </div>
            <div class="dot-stat-gray">
                <script>
                    var testIgnored = <xsl:value-of select="we:Ignored"/> ;
                    document.write(testIgnored * 100 / <xsl:value-of select="we:NumberOfTestcase"/> + "%");
                </script>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>