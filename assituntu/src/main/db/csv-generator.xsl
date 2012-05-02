<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="utf-8"/>

    <xsl:variable name='newline'><xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:template match="/table">
        <xsl:apply-templates select="data/tr"/>
    </xsl:template>

    <xsl:template match="/table/data/tr">
        <xsl:if test="position()>1">
            <xsl:value-of select="$newline"/>
        </xsl:if>
        <xsl:for-each select="td">
            <xsl:if test="position()>1">
                <xsl:text>;</xsl:text>
            </xsl:if>
            <xsl:value-of select="."/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>