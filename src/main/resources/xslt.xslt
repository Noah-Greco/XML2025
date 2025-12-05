<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- On génère du HTML -->
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <!-- Template racine -->
    <xsl:template match="/">
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                <title>PadChest - Vue patients</title>

                <!-- CSS dans C:\xampp\htdocs\PadChest\Css\style.css -->
                <link rel="stylesheet" type="text/css" href="Css/style.css"/>

                <!-- JS dans C:\xampp\htdocs\PadChest\Js\patients.js -->
                <script type="text/javascript" src="Js/script.js"></script>
            </head>
            <body>
                <h1>Vue individuelle des patients</h1>

                <!-- Navigation -->
                <div id="nav-patients">
                    <button id="prevPatient">← Précédent</button>
                    <span id="patientCounter"></span>
                    <button id="nextPatient">Suivant →</button>
                </div>

                <!-- Conteneur des fiches patients -->
                <div id="patients-container">
                    <xsl:for-each select="/dataset/record">
                        <div class="patient-card">
                            <h2>
                                Patient ID :
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="Patient/@PatientID"/>
                            </h2>

                            <p>
                                <span class="label">Image :</span>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="Image/@ImageID"/>
                            </p>

                            <p>
                                <span class="label">StudyID :</span>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="StudyID"/>
                            </p>

                            <p>
                                <span class="label">Projection :</span>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="Projection"/>
                            </p>

                            <p class="section-title">Labels :</p>
                            <ul>
                                <li>
                                    <xsl:value-of select="Label/@Labels"/>
                                </li>
                            </ul>

                            <p class="section-title">Localisations :</p>
                            <ul>
                                <li>
                                    <xsl:value-of select="Localizations"/>
                                </li>
                            </ul>
                        </div>
                    </xsl:for-each>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
