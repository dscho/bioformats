/*
 * ome.xml.model.Rectangle
 *
 *-----------------------------------------------------------------------------
 *
 *  Copyright (C) @year@ Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee,
 *      University of Wisconsin-Madison
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *-----------------------------------------------------------------------------
 */

/*-----------------------------------------------------------------------------
 *
 * THIS IS AUTOMATICALLY GENERATED CODE.  DO NOT MODIFY.
 * Created by melissa via xsd-fu on 2012-09-10 13:40:21-0400
 *
 *-----------------------------------------------------------------------------
 */

package ome.xml.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ome.xml.model.enums.*;
import ome.xml.model.primitives.*;

public class Rectangle extends Shape
{
	// Base:  -- Name: Rectangle -- Type: Rectangle -- javaBase: AbstractOMEModelObject -- javaType: Object

	// -- Constants --

	public static final String NAMESPACE = "http://www.openmicroscopy.org/Schemas/ROI/2012-06";

	/** Logger for this class. */
	private static final Logger LOGGER =
		LoggerFactory.getLogger(Rectangle.class);

	// -- Instance variables --


	// Property
	private Double y;

	// Property
	private Double x;

	// Property
	private Double height;

	// Property
	private Double width;

	// -- Constructors --

	/** Default constructor. */
	public Rectangle()
	{
		super();
	}

	/** 
	 * Constructs Rectangle recursively from an XML DOM tree.
	 * @param element Root of the XML DOM tree to construct a model object
	 * graph from.
	 * @param model Handler for the OME model which keeps track of instances
	 * and references seen during object population.
	 * @throws EnumerationException If there is an error instantiating an
	 * enumeration during model object creation.
	 */
	public Rectangle(Element element, OMEModel model)
	    throws EnumerationException
	{
		update(element, model);
	}

	// -- Custom content from Rectangle specific template --


	// -- OMEModelObject API methods --

	/** 
	 * Updates Rectangle recursively from an XML DOM tree. <b>NOTE:</b> No
	 * properties are removed, only added or updated.
	 * @param element Root of the XML DOM tree to construct a model object
	 * graph from.
	 * @param model Handler for the OME model which keeps track of instances
	 * and references seen during object population.
	 * @throws EnumerationException If there is an error instantiating an
	 * enumeration during model object creation.
	 */
	public void update(Element element, OMEModel model)
	    throws EnumerationException
	{
		super.update(element, model);
		String tagName = element.getTagName();
		if (!"Rectangle".equals(tagName))
		{
			LOGGER.debug("Expecting node name of Rectangle got {}", tagName);
		}
		if (element.hasAttribute("Y"))
		{
			// Attribute property Y
			setY(Double.valueOf(
					element.getAttribute("Y")));
		}
		if (element.hasAttribute("X"))
		{
			// Attribute property X
			setX(Double.valueOf(
					element.getAttribute("X")));
		}
		if (element.hasAttribute("Height"))
		{
			// Attribute property Height
			setHeight(Double.valueOf(
					element.getAttribute("Height")));
		}
		if (element.hasAttribute("Width"))
		{
			// Attribute property Width
			setWidth(Double.valueOf(
					element.getAttribute("Width")));
		}
	}

	// -- Rectangle API methods --

	public boolean link(Reference reference, OMEModelObject o)
	{
		boolean wasHandledBySuperClass = super.link(reference, o);
		if (wasHandledBySuperClass)
		{
			return true;
		}
		LOGGER.debug("Unable to handle reference of type: {}", reference.getClass());
		return false;
	}


	// Property
	public Double getY()
	{
		return y;
	}

	public void setY(Double y)
	{
		this.y = y;
	}

	// Property
	public Double getX()
	{
		return x;
	}

	public void setX(Double x)
	{
		this.x = x;
	}

	// Property
	public Double getHeight()
	{
		return height;
	}

	public void setHeight(Double height)
	{
		this.height = height;
	}

	// Property
	public Double getWidth()
	{
		return width;
	}

	public void setWidth(Double width)
	{
		this.width = width;
	}

	public Element asXMLElement(Document document)
	{
		return asXMLElement(document, null);
	}

	protected Element asXMLElement(Document document, Element Rectangle_element)
	{
		// Creating XML block for Rectangle

		if (Rectangle_element == null)
		{
			Rectangle_element =
					document.createElementNS(NAMESPACE, "Rectangle");
		}

		if (y != null)
		{
			// Attribute property Y
			Rectangle_element.setAttribute("Y", y.toString());
		}
		if (x != null)
		{
			// Attribute property X
			Rectangle_element.setAttribute("X", x.toString());
		}
		if (height != null)
		{
			// Attribute property Height
			Rectangle_element.setAttribute("Height", height.toString());
		}
		if (width != null)
		{
			// Attribute property Width
			Rectangle_element.setAttribute("Width", width.toString());
		}
		return super.asXMLElement(document, Rectangle_element);
	}
}
