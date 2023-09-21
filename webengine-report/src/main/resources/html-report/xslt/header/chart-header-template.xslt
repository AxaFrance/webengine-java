<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:we="http://www.axa.fr/WebEngine/2022">

    <xsl:template name="chart-header-template">
        <div id="myChart"> </div>
        <script>
            google.charts.load('current', {'packages':['corechart']});
            google.charts.setOnLoadCallback(drawChart);

            var testPassed = <xsl:value-of select="we:Passed"/>;
            var testFailed = <xsl:value-of select="we:Failed"/>;
            var testIgnored = <xsl:value-of select="we:Ignored"/>;

            function drawChart() {
            const data = google.visualization.arrayToDataTable([
            ['Test case', 'Percentage'],
            ['Passed',testPassed],
            ['Failed',testFailed],
            ['Ignored',testIgnored],
            ]);


            const options = {
            title:'Test case execution status',
            slices: {
            0: { color: '#27AE60' },
            1: { color: '#E74C3C' },
            2: { color: '#ABB2B9' }
            }
            };

            const chart = new google.visualization.PieChart(document.getElementById('myChart'));
            chart.draw(data, options);
            }
        </script>

    </xsl:template>

</xsl:stylesheet>