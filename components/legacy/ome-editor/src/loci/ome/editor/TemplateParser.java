/*
 * #%L
 * OME Metadata Editor application for exploration and editing of OME-XML and
 * OME-TIFF metadata.
 * %%
 * Copyright (C) 2006 - 2012 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package loci.ome.editor;

import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A simple xml parser to help set up the MetadataEditor's gui
 * based on the commonly used data specified in Template.xml
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/legacy/ome-editor/src/loci/ome/editor/TemplateParser.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/legacy/ome-editor/src/loci/ome/editor/TemplateParser.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @author Christopher Peterson crpeterson2 at wisc.edu
 */
public class TemplateParser {

  // -- Static fields --

  /** Factory for generating document builders. */
  public static final DocumentBuilderFactory DOC_FACT =
    DocumentBuilderFactory.newInstance();

  /** A list of filenames where semantic type defs are found. */
  public static final String[] TYPE_DEF_LIST = {
    "Experiment.ome","Experimenter.ome","Group.ome","Image.ome",
    "Instrument.ome","Plate.ome","Screen.ome","OMEIS/Repository.ome",
    "OMEIS/OriginalFile.ome","LOCIDefs.ome"
  };

  /** A list of regular (non "home-baked") type defs to cruise. */
  public static final String[] OLD_DEF_LIST = {"CA.xsd"};

  /** The filename of the folder the semantic type defs are in. */
  public static final String TYPE_DEF_FOLDER = "TypeDefs/";

  // -- Fields --

  /** Holds a list of the tabs necessary gathered from Template.xml. */
  protected Element[] tabList;

  /** Document generated by the DOM's parse. */
  protected Document templateDoc;

  /** The root xml Element in the Document. */
  protected Element root;

  // -- Constructor --

  /**
   * Parses the template document designated by the file argument,
   * creating a list of "top-level" Elements which I define as those
   * elements that have either an OMENode as a parent or a
   * CustomAttributesNode as a parent that has an OMENode as a parent
   * these top-level elements correspond to the tabs formed in the
   * MetadataPane
   */
  public TemplateParser(String filename) {
    // Parse the specified xml file (should be Template.xml) using the DOM
    try {
      DocumentBuilder db = DOC_FACT.newDocumentBuilder();
      templateDoc = db.parse(getClass().getResourceAsStream(filename));
    }
    catch (Exception e) {
      System.out.println("Some exception occured: " + e.toString());
      e.printStackTrace();
    }

    root = templateDoc.getDocumentElement();
    NodeList nList = root.getChildNodes();
    int nLength = nList.getLength();
    Vector v = new Vector(nLength);
    for (int i = 0;i < nLength;i++) {
      Node node = nList.item(i);
      if (node instanceof Element) {
        Element thisElement = (Element) node;
        if ( "OMEElement".equals(thisElement.getTagName()) ) v.add(thisElement);
      }
    }
    tabList = new Element[v.size()];
    for (int i = 0;i < v.size();i++) {
      tabList[i] = (Element) v.elementAt(i);
    }
  }

  public Element getRoot() {
    return root;
  }

  /** Returns an array of Elements representing the tabs in the editor. */
  public Element[] getTabs() {
    return tabList;
  }

  /**
   * Returns a hashtable to store the types associated with a reference element
   * will be implemented as a Hashtable of Hashtables first key corresponds
   * to the OMEElement, second (nested) key corresponds to OMEAttribute,
   * and finally the value corresponds to what type the OMEAttribute should
   * have.
   */
  public static Hashtable getRefHash() {
    //setup Hashtables for getting reference types
    Hashtable refHash = new Hashtable();
    //check each different semantic type definition file for definitions
    //if definitions overlap, they should be the same anyway, so the last
    //one encountered is the one used
    for (int i = 0;i<TYPE_DEF_LIST.length;i++) {
      Document templateDoc = null;
      try {
        DocumentBuilder db = DOC_FACT.newDocumentBuilder();
        templateDoc = db.parse(TemplateParser.class.getResourceAsStream(
          TYPE_DEF_FOLDER + TYPE_DEF_LIST[i]));
      }
      catch (Exception e) {
        System.out.println("Some exception occured: " + e.toString());
        e.printStackTrace();
      }

      //surfing node-trees... looking at the "home-baked" semantic
      //type definitions at this point...    .ome type files
      Element thisRoot = templateDoc.getDocumentElement();
      NodeList nl = thisRoot.getChildNodes();
      for (int j = 0;j < nl.getLength();j++) {
        Node node = nl.item(j);
        if (!(node instanceof Element)) continue;
        Element someE = (Element) node;
        if (!someE.getTagName().equals("SemanticTypeDefinitions")) continue;
        NodeList omeEleList = node.getChildNodes();
        for (int k = 0;k < omeEleList.getLength();k++) {
          node = omeEleList.item(k);
          if (!(node instanceof Element)) continue;
          Element omeEle = (Element) node;
          if (!omeEle.getTagName().equals("SemanticType")) continue;
          NodeList omeAttrList = node.getChildNodes();
          Hashtable thisHash = new Hashtable(10);
          for (int l = 0;l < omeAttrList.getLength();l++) {
            node = omeAttrList.item(l);
            if (!(node instanceof Element)) continue;
            Element omeAttr = (Element) node;
            if (!omeAttr.getTagName().equals("Element")) continue;
            if (!omeAttr.hasAttribute("DataType")) continue;
            String dType = omeAttr.getAttribute("DataType");
            if (!dType.equals("reference")) continue;
            String attrName = omeAttr.getAttribute("Name");
            String refType = omeAttr.getAttribute("RefersTo");
            thisHash.put(attrName,refType);
          }
          refHash.put(omeEle.getAttribute("Name"), thisHash);
        }
      }
    }
    //need to parse differently for  each .xsd type semantic def file
    for (int i = 0;i<OLD_DEF_LIST.length;i++) {
      Document templateDoc = null;
      try {
        DocumentBuilder db = DOC_FACT.newDocumentBuilder();
        templateDoc = db.parse(TemplateParser.class.getResourceAsStream(
          TYPE_DEF_FOLDER + OLD_DEF_LIST[i]));
      }
      catch (Exception e) {
        System.out.println("Some exception occured: " + e.toString());
        e.printStackTrace();
      }

      //surfing node-trees...
      Element thisRoot = templateDoc.getDocumentElement();
      NodeList eleList = thisRoot.getChildNodes();

      for (int j = 0;j < eleList.getLength();j++) {
        Node node = eleList.item(j);
        if (!(node instanceof Element)) continue;
        Element eleE = (Element) node;
        if (!eleE.getTagName().equals("xsd:element")) continue;
        NodeList wrapperList = eleE.getChildNodes();
        Hashtable attrHash = new Hashtable(10);
        for (int k = 0;k < wrapperList.getLength();k++) {
          node = (Node) wrapperList.item(k);
          if (!(node instanceof Element)) continue;
          Element anotherE = (Element) node;
          if (!anotherE.getTagName().equals("xsd:complexType")) continue;
          NodeList attrList = anotherE.getChildNodes();
          for (int l = 0;l < attrList.getLength();l++) {
            node = (Node) attrList.item(l);
            if (!(node instanceof Element)) continue;
            Element attrE = (Element) node;
            if (!attrE.getTagName().equals("xsd:attribute") ||
              !attrE.hasAttribute("type"))
            {
              continue;
            }
            String type = attrE.getAttribute("type");
            if (!type.startsWith("OME:") || !type.endsWith("ID")) continue;
            type = type.substring(4, type.length() - 2);
            String attrName = attrE.getAttribute("name");
            attrHash.put(attrName,type);
          }
        }
        refHash.put(eleE.getAttribute("name"), attrHash);
      }
    }
    return refHash;
  }

  /** Returns the version information from the template. */
  public String getVersion() {
    return root.getAttribute("Version");
  }

  /** Used for debugging. */
  public static void main(String[] args) {
    TemplateParser tp = new TemplateParser("Template.xml");
    Element[] eList = tp.getTabs();
    for (int i = 0;i<eList.length;i++) {
      System.out.println(eList[i].getAttribute("XMLName"));
    }
    Hashtable mmmHash = TemplateParser.getRefHash();
    System.out.println(mmmHash);
  }

}
