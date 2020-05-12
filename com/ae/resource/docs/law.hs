<?xml version="1.0" encoding='ISO-8859-1'?>

<helpset version="1.0">

  <!-- title -->
  <title>Leyes</title>

  <!-- maps -->
  <maps>
     <homeID>home</homeID>
     <mapref location="law.jhm"/>
  </maps>

  <!-- views -->
  <view>
    <name>TOC</name>
    <label>Tabla de contenido</label>
    <type>javax.help.TOCView</type>
    <data>lawTOC.xml</data>
  </view>

  <view>
    <name>Search</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view>

</helpset>
