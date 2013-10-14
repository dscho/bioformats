/*
 * #%L
 * OME SCIFIO package for reading and converting scientific file formats.
 * %%
 * Copyright (C) 2005 - 2013 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package loci.formats;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import loci.common.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassList is a list of classes for use with ImageReader or ImageWriter,
 * parsed from a configuration file such as readers.txt or writers.txt.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/ClassList.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/ClassList.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @author Curtis Rueden ctrueden at wisc.edu
 */
public class ClassList<T> {

  // -- Constants --

  private static final Logger LOGGER = //LoggerFactory.getLogger(ClassList.class);
    new Logger() {
    public String getName(){return "Dummy";}
    public boolean isTraceEnabled(){return false;}
    public void trace(String s){}
    public void trace(String s, java.lang.Object o){}
    public void trace(String s, java.lang.Object o0, java.lang.Object o){}
    public void trace(String s, java.lang.Object[] os){}
    public void trace(String s, java.lang.Throwable t){}
    public boolean isTraceEnabled(org.slf4j.Marker m){return false;}
    public void trace(org.slf4j.Marker m, String s){}
    public void trace(org.slf4j.Marker m, String s, java.lang.Object o){}
    public void trace(org.slf4j.Marker m, String s, java.lang.Object o0, java.lang.Object o){}
    public void trace(org.slf4j.Marker m, String s, java.lang.Object[] os){}
    public void trace(org.slf4j.Marker m, String s, java.lang.Throwable t){}
/*
    public boolean isDebugEnabled(){return false;}
    public void debug(String s){}
    public void debug(String s, java.lang.Object o){}
    public void debug(String s, java.lang.Object o0, java.lang.Object o){}
    public void debug(String s, java.lang.Object[] os){}
    public void debug(String s, java.lang.Throwable t){}
    public boolean isDebugEnabled(org.slf4j.Marker m){return false;}
    public void debug(org.slf4j.Marker m, String s){}
    public void debug(org.slf4j.Marker m, String s, java.lang.Object o){}
    public void debug(org.slf4j.Marker m, String s, java.lang.Object o0, java.lang.Object o){}
    public void debug(org.slf4j.Marker m, String s, java.lang.Object[] os){}
    public void debug(org.slf4j.Marker m, String s, java.lang.Throwable t){}
*/
private void log(String prefix, String s, Object[] os, Throwable t) {
if (s != null) System.err.println(prefix + s + (os == null ? "" : java.util.Arrays.toString(os)));
if (t != null) t.printStackTrace();
}

    public boolean isDebugEnabled(){return true;}
    public void debug(String s){log("[DEBUG] ", s, null, null);}
    public void debug(String s, java.lang.Object o){log("[DEBUG] ", s, new Object[] { o }, null);}
    public void debug(String s, java.lang.Object o0, java.lang.Object o){log("[DEBUG] ", s, new Object[] { o0, o }, null);}
    public void debug(String s, java.lang.Object[] os){log("[DEBUG] ", s, os, null);}
    public void debug(String s, java.lang.Throwable t){log("[DEBUG] ", s, null, t);}
    public boolean isDebugEnabled(org.slf4j.Marker m){return true;}
    public void debug(org.slf4j.Marker m, String s){log("[DEBUG] ", s, null, null);}
    public void debug(org.slf4j.Marker m, String s, java.lang.Object o){log("[DEBUG] ", s, new Object[] { o }, null);}
    public void debug(org.slf4j.Marker m, String s, java.lang.Object o1, java.lang.Object o2){log("[DEBUG] ", s, new Object[] { o1, o2 }, null);}
    public void debug(org.slf4j.Marker m, String s, java.lang.Object[] o){log("[DEBUG] ", s, o, null);}
    public void debug(org.slf4j.Marker m, String s, java.lang.Throwable t){log("[DEBUG] ", s, null, t);}

    public boolean isInfoEnabled(){return true;}
    public void info(String s){log("[INFO] ", s, null, null);}
    public void info(String s, java.lang.Object o){log("[INFO] ", s, new Object[] { o }, null);}
    public void info(String s, java.lang.Object o0, java.lang.Object o){log("[INFO] ", s, new Object[] { o0, o }, null);}
    public void info(String s, java.lang.Object[] os){log("[INFO] ", s, os, null);}
    public void info(String s, java.lang.Throwable t){log("[INFO] ", s, null, t);}
    public boolean isInfoEnabled(org.slf4j.Marker m){return true;}
    public void info(org.slf4j.Marker m, String s){log("[INFO] ", s, null, null);}
    public void info(org.slf4j.Marker m, String s, java.lang.Object o){log("[INFO] ", s, new Object[] { o }, null);}
    public void info(org.slf4j.Marker m, String s, java.lang.Object o1, java.lang.Object o2){log("[INFO] ", s, new Object[] { o1, o2 }, null);}
    public void info(org.slf4j.Marker m, String s, java.lang.Object[] o){log("[INFO] ", s, o, null);}
    public void info(org.slf4j.Marker m, String s, java.lang.Throwable t){log("[INFO] ", s, null, t);}

    public boolean isWarnEnabled(){return true;}
    public void warn(String s){log("[WARN] ", s, null, null);}
    public void warn(String s, java.lang.Object o){log("[WARN] ", s, new Object[] { o }, null);}
    public void warn(String s, java.lang.Object o0, java.lang.Object o){log("[WARN] ", s, new Object[] { o0, o }, null);}
    public void warn(String s, java.lang.Object[] os){log("[WARN] ", s, os, null);}
    public void warn(String s, java.lang.Throwable t){log("[WARN] ", s, null, t);}
    public boolean isWarnEnabled(org.slf4j.Marker m){return true;}
    public void warn(org.slf4j.Marker m, String s){log("[WARN] ", s, null, null);}
    public void warn(org.slf4j.Marker m, String s, java.lang.Object o){log("[WARN] ", s, new Object[] { o }, null);}
    public void warn(org.slf4j.Marker m, String s, java.lang.Object o1, java.lang.Object o2){log("[WARN] ", s, new Object[] { o1, o2 }, null);}
    public void warn(org.slf4j.Marker m, String s, java.lang.Object[] o){log("[WARN] ", s, o, null);}
    public void warn(org.slf4j.Marker m, String s, java.lang.Throwable t){log("[WARN] ", s, null, t);}

    public boolean isErrorEnabled(){return true;}
    public void error(String s){log("[ERROR] ", s, null, null);}
    public void error(String s, java.lang.Object o){log("[ERROR] ", s, new Object[] { o }, null);}
    public void error(String s, java.lang.Object o0, java.lang.Object o){log("[ERROR] ", s, new Object[] { o0, o }, null);}
    public void error(String s, java.lang.Object[] os){log("[ERROR] ", s, os, null);}
    public void error(String s, java.lang.Throwable t){log("[ERROR] ", s, null, t);}
    public boolean isErrorEnabled(org.slf4j.Marker m){return true;}
    public void error(org.slf4j.Marker m, String s){log("[ERROR] ", s, null, null);}
    public void error(org.slf4j.Marker m, String s, java.lang.Object o){log("[ERROR] ", s, new Object[] { o }, null);}
    public void error(org.slf4j.Marker m, String s, java.lang.Object o1, java.lang.Object o2){log("[ERROR] ", s, new Object[] { o1, o2 }, null);}
    public void error(org.slf4j.Marker m, String s, java.lang.Object[] o){log("[ERROR] ", s, o, null);}
    public void error(org.slf4j.Marker m, String s, java.lang.Throwable t){log("[ERROR] ", s, null, t);}
    };


  // -- Fields --

  /** Base class to which all classes are assignable. */
  private Class<T> base;

  /** List of classes. */
  private List<Class<? extends T>> classes;

  // -- Constructor --

  /**
   * Constructs a list of classes, initially empty.
   * @param base Base class to which all classes are assignable.
   */
  public ClassList(Class<T> base) {
    this.base = base;
    classes = new ArrayList<Class<? extends T>>();
  }

  /**
   * Constructs a list of classes from the given configuration file.
   * @param file Configuration file containing the list of classes.
   * @param base Base class to which all classes are assignable.
   * @throws IOException if the file cannot be read.
   */
  public ClassList(String file, Class<T> base) throws IOException {
    this(file, base, ClassList.class);
  }

  /**
   * Constructs a list of classes from the given configuration file.
   * @param file Configuration file containing the list of classes.
   * @param base Base class to which all classes are assignable.
   * @param location Class indicating which package to search for the file.
   *  If null, 'file' is interpreted as an absolute path name.
   * @throws IOException if the file cannot be read.
   */
  public ClassList(String file, Class<T> base, Class<?> location)
    throws IOException
  {
    this.base = base;
    classes = new ArrayList<Class<? extends T>>();
System.err.println("file: " + file);
    if (file == null) return;

    // read classes from file
    BufferedReader in = null;
    if (location == null) {
      in = new BufferedReader(new InputStreamReader(
        new FileInputStream(file), Constants.ENCODING));
    }
    else {
      in = new BufferedReader(new InputStreamReader(
        location.getResourceAsStream(file), Constants.ENCODING));
    }
System.err.println("opened file " + file);
    while (true) {
      String line = null;
      line = in.readLine();
      if (line == null) break;
System.err.println("line: " + line);

      // ignore characters following # sign (comments)
      int ndx = line.indexOf("#");
      if (ndx >= 0) line = line.substring(0, ndx);
      line = line.trim();
      if (line.equals("")) continue;

      // load class
      Class<? extends T> c = null;
      try {
        Class<?> rawClass = Class.forName(line);
        c = cast(rawClass);
      }
      catch (ClassNotFoundException exc) {
        LOGGER.debug("Could not find {}", line, exc);
      }
      catch (NoClassDefFoundError err) {
        LOGGER.debug("Could not find {}", line, err);
      }
      catch (ExceptionInInitializerError err) {
        LOGGER.debug("Failed to create an instance of {}", line, err);
      }
      catch (RuntimeException exc) {
        // HACK: workaround for bug in Apache Axis2
        String msg = exc.getMessage();
        if (msg != null && msg.indexOf("ClassNotFound") < 0) throw exc;
        LOGGER.debug("", exc);
      }
      if (c == null) {
        LOGGER.error("\"{}\" is not valid.", line);
        continue;
      }
      classes.add(c);
    }
    in.close();
  }

  // -- ClassList API methods --

  /**
   * Adds the given class, which must be assignable
   * to the base class, to the list.
   */
  public void addClass(Class<? extends T> c) {
    classes.add(c);
  }

  /** Removes the given class from the list. */
  public void removeClass(Class<? extends T> c) {
    classes.remove(c);
  }

  /** Gets the list of classes as an array. */
  @SuppressWarnings("unchecked")
  public Class<? extends T>[] getClasses() {
    return classes.toArray(new Class[0]);
  }

  // -- Helper methods --

  @SuppressWarnings("unchecked")
  private Class<? extends T> cast(Class<?> rawClass) {
    if (!base.isAssignableFrom(rawClass)) return null;
    return (Class<? extends T>) rawClass;
  }

}
