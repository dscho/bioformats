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
import java.nio.ByteOrder;

import loci.common.ByteArrayHandle;
import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;

/**
 * InCell3000Reader is the file format reader for InCell 3000.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/in/InCell3000Reader.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/in/InCell3000Reader.java;hb=HEAD">Gitweb</a></dd></dl>
 */
public class InCell3000Reader extends FormatReader {

  // -- Fields --

  private long pixelsOffset;

  // -- Constructor --

  /** Constructs a new InCell 3000 reader. */
  public InCell3000Reader() {
    super("InCell 3000", "frm");
    domains = new String[] {FormatTools.UNKNOWN_DOMAIN};
  }

  // -- IFormatReader API methods --

  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    return false;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);

    in.seek(pixelsOffset);

    ByteArrayHandle pixels = new ByteArrayHandle();
    pixels.setOrder(ByteOrder.LITTLE_ENDIAN);

    int count = 0;
    int startValue = 0;

    int totalElements = getSizeX() * getSizeY() * 2;
    while (pixels.length() < totalElements) {
      short pixel = in.readShort();
      if ((pixel & 0xffff) > 32768) {
        count = (pixel & 0xffff) - 32768;
        startValue = in.readShort() & 0xffff;
        long fp = in.getFilePointer();
        for (int i=0; i<count; i++) {
          in.seek(fp + 2 * (i / 3));
          int intOfs = in.readShort() & 0xffff;
          if ((i % 3) != 0) {
            intOfs = (intOfs >> 5);
          }
          int tempVal = startValue + (intOfs & 31);
          pixels.writeShort((short) tempVal);
        }
        in.seek(fp + 2 * (int) Math.ceil((double) count / 3));
      }
      else {
        pixels.writeShort((short) (pixel & 0xffff));
      }
    }
    pixels.seek(0);
    RandomAccessInputStream pix = new RandomAccessInputStream(pixels);
    pix.order(isLittleEndian());
    readPlane(pix, x, y, w, h, buf);
    pix.close();
    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      pixelsOffset = 0;
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);

    core[0].littleEndian = true;
    in.order(isLittleEndian());

    pixelsOffset = in.readShort();
    core[0].sizeX = in.readShort();

    int nLines = in.readShort();
    int numPlanes = nLines % 32;
    core[0].sizeY = (nLines - numPlanes) / numPlanes;

    int componentType = in.read();
    int reserved = in.read();
    int timestamp = in.readInt();
    int auxiliary = in.readInt();
    int relativeTimestamp = in.readInt();
    float zSection = in.readFloat();
    int componentBytes = in.readInt();
    int zero = in.read();

    core[0].sizeZ = 1;
    core[0].sizeC = 1;
    core[0].sizeT = 1;
    core[0].imageCount = 1;
    core[0].pixelType = FormatTools.UINT16;
    core[0].dimensionOrder = "XYCZT";
    core[0].rgb = false;

    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
  }

}
