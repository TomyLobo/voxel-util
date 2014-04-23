<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="text"/>

	<xsl:template match="/">
		<xsl:for-each select="MyObjectBuilder_Sector/SectorObjects/MyObjectBuilder_EntityBase[@xsi:type='MyObjectBuilder_CubeGrid']">
			<xsl:value-of select="concat(EntityId, ': ', CubeBlocks/MyObjectBuilder_CubeBlock[@xsi:type='MyObjectBuilder_Beacon']/CustomName, ' (', GridSizeEnum, ', ', count(CubeBlocks/*), 'x)&#10;')"/>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
