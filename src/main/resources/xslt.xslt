<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Template principal -->
    <xsl:template match="/">
        <html>
            <head>
                <title>Dataset Viewer</title>
                <meta charset="UTF-8"/>
            </head>
            <body>
                <h1>Liste des enregistrements</h1>

                <table border="1" cellpadding="5" cellspacing="0">
                    <tr>
                        <th>ImageID</th>
                        <th>StudyID</th>
                        <th>PatientID</th>
                        <th>Projection</th>
                        <th>Labels</th>
                        <th>Localizations</th>
                    </tr>

                    <!-- Boucle sur les records -->
                    <xsl:for-each select="/dataset/record">
                        <tr>
                            <td><xsl:value-of select="Image/@ImageID"/></td>
                            <td><xsl:value-of select="StudyID"/></td>
                            <td><xsl:value-of select="Patient/@PatientID"/></td>
                            <td><xsl:value-of select="Projection"/></td>

                            <td>
                                <ul>
                                    <xsl:for-each select="Label/LabelItem">
                                        <li><xsl:value-of select="."/></li>
                                    </xsl:for-each>
                                </ul>
                            </td>

                            <td>
                                <ul>
                                    <xsl:for-each select="Localizations/LocItem">
                                        <li><xsl:value-of select="."/></li>
                                    </xsl:for-each>
                                </ul>
                            </td>
                        </tr>
                    </xsl:for-each>

                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
