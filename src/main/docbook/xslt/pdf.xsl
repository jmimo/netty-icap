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
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">

  <xsl:import href="classpath:/xslt/org/jboss/pdf.xsl" />
  <xsl:import href="classpath:/xslt/org/jboss/xslt/fonts/pdf/fonts.xsl" />

  <!-- Hide URL in links -->
  <xsl:param name="ulink.show" select="0"/>

</xsl:stylesheet>