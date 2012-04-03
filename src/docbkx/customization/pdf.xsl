<?xml version="1.0" encoding="UTF-8"?>
<!--
 * Copyright (c) 2012 Michael Mimo Moratti.
 *
 * Michael Mimo Moratti licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:d="http://docbook.org/ns/docbook" 
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions"
                version="1.0">

	<xsl:import href="urn:docbkx:stylesheet"/>
	
	<!-- ######################################################################################## -->
	<!-- # Page margins                                                                           -->
	<!-- ######################################################################################## -->
	
	<xsl:param name= "page.margin.top">3mm</xsl:param>
	<xsl:param name="region.before.extent">23mm</xsl:param>  
	<xsl:param name="body.margin.top">26mm</xsl:param>  
	
	<xsl:param name="body.margin.bottom">18mm</xsl:param>
	<xsl:param name="region.after.extent">15mm</xsl:param>
	<xsl:param name="page.margin.bottom">3mm</xsl:param>
	
	<!-- Prevent blank pages in output -->
	<xsl:template name="book.titlepage.before.verso"/>
	<xsl:template name="book.titlepage.verso"/>
	<xsl:template name="book.titlepage.separator"/>

	<!-- ######################################################################################## -->
	<!-- # Page header                                                                            -->
	<!-- ######################################################################################## -->
	
	<xsl:template name="header.content">
		<xsl:param name="pageclass" select="''"/>
		<xsl:param name="sequence" select="''"/>
		<xsl:param name="position" select="''"/>
		<xsl:param name="gentext-key" select="''"/>
		
		<fo:block>
		
		<xsl:if test="$position = 'right'">
            <!-- fo:external-graphic content-height="18mm" src="customization/USP_Logo_oClaim_CMYK.png"/ -->
		</xsl:if>	
		
		</fo:block>
		
	</xsl:template>
	
	<!-- ######################################################################################## -->
	<!-- # Page footer                                                                            -->
	<!-- ######################################################################################## -->
	
	<xsl:template name="footer.content">
		<xsl:param name="pageclass" select="''"/>
		<xsl:param name="sequence" select="''"/>
		<xsl:param name="position" select="''"/>
		<xsl:param name="gentext-key" select="''"/>
        
        <fo:block>
		
		<xsl:if test="$position = 'left'">
			<!-- add current version or document type -->
		</xsl:if>
		
		<xsl:if test="$position = 'right'">
			<fo:page-number/>
		</xsl:if>		

		</fo:block>	
		
	</xsl:template>

	<!-- ######################################################################################## -->
	<!-- # Titles                                                                                 -->
	<!-- ######################################################################################## -->

	<!-- xsl:template name="component.title">
	  <xsl:param name="node" select="."/>
	  <xsl:param name="pagewide" select="0"/>
	</xsl:template -->

	<xsl:template name="component.title">
	  <xsl:param name="node" select="."/>
	  <xsl:param name="pagewide" select="0"/>
	
	  <xsl:variable name="id">
	    <xsl:call-template name="object.id">
	      <xsl:with-param name="object" select="$node"/>
	    </xsl:call-template>
	  </xsl:variable>
	
	  <xsl:variable name="title">
	  	<xsl:apply-templates select="$node" mode="title.markup"/>
	    <!-- xsl:apply-templates select="$node" mode="object.title.markup">
	      <xsl:with-param name="allow-anchors" select="1"/>
	    </xsl:apply-templates -->
	  </xsl:variable>
	
	  <xsl:variable name="titleabbrev">
	    <xsl:apply-templates select="$node" mode="titleabbrev.markup"/>
	  </xsl:variable>
	
	  <xsl:variable name="level">
	    <xsl:choose>
	      <xsl:when test="ancestor::d:section">
	        <xsl:value-of select="count(ancestor::d:section)+1"/>
	      </xsl:when>
	      <xsl:when test="ancestor::d:sect5">6</xsl:when>
	      <xsl:when test="ancestor::d:sect4">5</xsl:when>
	      <xsl:when test="ancestor::d:sect3">4</xsl:when>
	      <xsl:when test="ancestor::d:sect2">3</xsl:when>
	      <xsl:when test="ancestor::d:sect1">2</xsl:when>
	      <xsl:otherwise>1</xsl:otherwise>
	    </xsl:choose>
	  </xsl:variable>
	
	  <xsl:if test="$passivetex.extensions != 0">
	    <fotex:bookmark xmlns:fotex="http://www.tug.org/fotex"
	                    fotex-bookmark-level="2"
	                    fotex-bookmark-label="{$id}">
	      <xsl:value-of select="$titleabbrev"/>
	    </fotex:bookmark>
	  </xsl:if>
	
	  <fo:block xsl:use-attribute-sets="component.title.properties">
	    <xsl:if test="$pagewide != 0">
	      <!-- Doesn't work to use 'all' here since not a child of fo:flow -->
	      <xsl:attribute name="span">inherit</xsl:attribute>
	    </xsl:if>
	    <xsl:attribute name="hyphenation-character">
	      <xsl:call-template name="gentext">
	        <xsl:with-param name="key" select="'hyphenation-character'"/>
	      </xsl:call-template>
	    </xsl:attribute>
	    <xsl:attribute name="hyphenation-push-character-count">
	      <xsl:call-template name="gentext">
	        <xsl:with-param name="key" select="'hyphenation-push-character-count'"/>
	      </xsl:call-template>
	    </xsl:attribute>
	    <xsl:attribute name="hyphenation-remain-character-count">
	      <xsl:call-template name="gentext">
	        <xsl:with-param name="key" select="'hyphenation-remain-character-count'"/>
	      </xsl:call-template>
	    </xsl:attribute>
	    <xsl:if test="$axf.extensions != 0">
	      <xsl:attribute name="axf:outline-level">
	        <xsl:value-of select="count($node/ancestor::*)"/>
	      </xsl:attribute>
	      <xsl:attribute name="axf:outline-expand">false</xsl:attribute>
	      <xsl:attribute name="axf:outline-title">
	        <xsl:value-of select="normalize-space($title)"/>
	      </xsl:attribute>
	    </xsl:if>
	
	    <!-- Let's handle the case where a component (bibliography, for example)
	         occurs inside a section; will we need parameters for this?
	         Danger Will Robinson: using section.title.level*.properties here
	         runs the risk that someone will set something other than
	         font-size there... -->
	    <xsl:choose>
	      <xsl:when test="$level=2">
	        <fo:block xsl:use-attribute-sets="section.title.level2.properties">
	          <xsl:copy-of select="$title"/>
	        </fo:block>
	      </xsl:when>
	      <xsl:when test="$level=3">
	        <fo:block xsl:use-attribute-sets="section.title.level3.properties">
	          <xsl:copy-of select="$title"/>
	        </fo:block>
	      </xsl:when>
	      <xsl:when test="$level=4">
	        <fo:block xsl:use-attribute-sets="section.title.level4.properties">
	          <xsl:copy-of select="$title"/>
	        </fo:block>
	      </xsl:when>
	      <xsl:when test="$level=5">
	        <fo:block xsl:use-attribute-sets="section.title.level5.properties">
	          <xsl:copy-of select="$title"/>
	        </fo:block>
	      </xsl:when>
	      <xsl:when test="$level=6">
	        <fo:block xsl:use-attribute-sets="section.title.level6.properties">
	          <xsl:copy-of select="$title"/>
	        </fo:block>
	      </xsl:when>
	      <xsl:otherwise>
	        <!-- not in a section: do nothing special -->
	        <xsl:copy-of select="$title"/>
	      </xsl:otherwise>
	    </xsl:choose>
	  </fo:block>
	</xsl:template>

	<!-- ######################################################################################## -->
	<!-- # General rendering settings                                                             -->
	<!-- ######################################################################################## -->
	
	<!-- Hide URL in links -->
	<xsl:param name="ulink.show" select="0"/>
	
	<xsl:attribute-set name="section.title.properties">
	  <xsl:attribute name="font-family">
	    <xsl:value-of select="$title.font.family"></xsl:value-of>
	  </xsl:attribute>
	  <xsl:attribute name="font-weight">normal</xsl:attribute>
	  <!-- font size is calculated dynamically by section.heading template -->
	  <xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
	  <xsl:attribute name="space-before.minimum">0.8em</xsl:attribute>
	  <xsl:attribute name="space-before.optimum">1.0em</xsl:attribute>
	  <xsl:attribute name="space-before.maximum">1.2em</xsl:attribute>
	  <xsl:attribute name="text-align">start</xsl:attribute>
	  <xsl:attribute name="start-indent"><xsl:value-of select="$title.margin.left"></xsl:value-of></xsl:attribute>
	</xsl:attribute-set>

</xsl:stylesheet>