/*
 * #%L
 * OME Bio-Formats package for reading and converting biological file formats.
 * %%
 * Copyright (C) 2005 - 2012 Open Microscopy Environment:
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

package loci.formats.in;

import java.io.IOException;
import java.util.ArrayList;

import loci.common.DataTools;
import ome.scifio.io.Location;
import ome.scifio.io.RandomAccessInputStream;
import loci.formats.ChannelSeparator;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * NDPISReader is the file format reader for Hamamatsu .ndpis files.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/in/NDPISReader.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/in/NDPISReader.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @author Melissa Linkert melissa at glencoesoftware.com
 */
public class NDPISReader extends FormatReader {

  // -- Fields --

  private String[] ndpiFiles;
  private ChannelSeparator[] readers;

  // -- Constructor --

  /** Constructs a new NDPIS reader. */
  public NDPISReader() {
    super("Hamamatsu NDPIS", "ndpis");
    domains = new String[] {FormatTools.HISTOLOGY_DOMAIN};
    datasetDescription = "One .ndpis file and at least one .ndpi file";
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    return true;
  }

  /* @see loci.formats.IFormatReader#isSingleFile(String) */
  public boolean isSingleFile(String id) throws FormatException, IOException {
    return false;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    int[] zct = getZCTCoords(no);
    int channel = zct[1];
    readers[channel].setId(ndpiFiles[channel]);
    int cIndex = channel < readers[channel].getSizeC() ? channel : 0;
    int plane = readers[channel].getIndex(zct[0], cIndex, zct[2]);

    readers[channel].openBytes(plane, buf, x, y, w, h);

    return buf;
  }

  /* @see loci.formats.IFormatReader#getSeriesUsedFiles(boolean) */
  public String[] getSeriesUsedFiles(boolean noPixels) {
    if (noPixels) {
      return new String[] {currentId};
    }
    String[] files = new String[ndpiFiles.length + 1];
    files[0] = currentId;
    System.arraycopy(ndpiFiles, 0, files, 1, ndpiFiles.length);
    return files;
  }

  /* @see loci.formats.IFormatReader#fileGroupOption(String) */
  public int fileGroupOption(String id) throws FormatException, IOException {
    return FormatTools.MUST_GROUP;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      ndpiFiles = null;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);

    Location parent = new Location(id).getAbsoluteFile().getParentFile();

    String[] lines = DataTools.readFile(currentId).split("\r\n");

    for (String line : lines) {
      int eq = line.indexOf("=");
      if (eq < 0) {
        continue;
      }
      String key = line.substring(0, eq).trim();
      String value = line.substring(eq + 1).trim();

      if (key.equals("NoImages")) {
        ndpiFiles = new String[Integer.parseInt(value)];
        readers = new ChannelSeparator[ndpiFiles.length];
      }
      else if (key.startsWith("Image")) {
        int index = Integer.parseInt(key.replaceAll("Image", ""));
        ndpiFiles[index] = new Location(parent, value).getAbsolutePath();
        readers[index] = new ChannelSeparator(new NDPIReader());
      }
    }

    readers[0].setMetadataStore(getMetadataStore());
    readers[0].setId(ndpiFiles[0]);

    core = new ArrayList<CoreMetadata>(readers[0].getCoreMetadataList());
    for (int i=0; i<getSeriesCount(); i++) {
      CoreMetadata ms = core.get(i);
      ms.sizeC = readers.length;
      ms.rgb = false;
      ms.imageCount = ms.sizeC * ms.sizeZ * ms.sizeT;
      ms.cLengths = new int[] {getSizeC()};
    }

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
