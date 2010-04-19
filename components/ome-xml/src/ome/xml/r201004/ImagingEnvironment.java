/*
 * ome.xml.r201004.ImagingEnvironment
 *
 *-----------------------------------------------------------------------------
 *
 *  Copyright (C) 2010 Open Microscopy Environment
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
 * Created by callan via xsd-fu on 2010-04-19 19:23:58+0100
 *
 *-----------------------------------------------------------------------------
 */

package ome.xml.r201004;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ome.xml.r201004.enums.*;

public class ImagingEnvironment extends Object
{
	// -- Instance variables --

	// Property
	private Double co2percent;

	// Property
	private Double temperature;

	// Property
	private Double airPressure;

	// Property
	private Double humidity;

	// -- Constructors --

	/** Constructs a ImagingEnvironment. */
	public ImagingEnvironment()
	{
	}

	// -- ImagingEnvironment API methods --

	// Property
	public Double getCO2Percent()
	{
		return co2percent;
	}

	public void setCO2Percent(Double co2percent)
	{
		this.co2percent = co2percent;
	}

	// Property
	public Double getTemperature()
	{
		return temperature;
	}

	public void setTemperature(Double temperature)
	{
		this.temperature = temperature;
	}

	// Property
	public Double getAirPressure()
	{
		return airPressure;
	}

	public void setAirPressure(Double airPressure)
	{
		this.airPressure = airPressure;
	}

	// Property
	public Double getHumidity()
	{
		return humidity;
	}

	public void setHumidity(Double humidity)
	{
		this.humidity = humidity;
	}

	public Element asXMLElement(Document document)
	{
		// Creating XML block for ImagingEnvironment
		Element ImagingEnvironment_element = document.createElement("ImagingEnvironment");
		if (co2percent != null)
		{
			// Attribute property CO2Percent
			ImagingEnvironment_element.setAttribute("CO2Percent", co2percent.toString());
		}
		if (temperature != null)
		{
			// Attribute property Temperature
			ImagingEnvironment_element.setAttribute("Temperature", temperature.toString());
		}
		if (airPressure != null)
		{
			// Attribute property AirPressure
			ImagingEnvironment_element.setAttribute("AirPressure", airPressure.toString());
		}
		if (humidity != null)
		{
			// Attribute property Humidity
			ImagingEnvironment_element.setAttribute("Humidity", humidity.toString());
		}
		return ImagingEnvironment_element;
	}
}