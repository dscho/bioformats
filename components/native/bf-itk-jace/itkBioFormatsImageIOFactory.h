/*
 * #%L
 * Bio-Formats plugin for the Insight Toolkit.
 * %%
 * Copyright (C) 2009 - 2012 Open Microscopy Environment:
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
 * 
 * ----------------------------------------------------------------
 * Adapted from the Slicer3 project: http://www.slicer.org/
 * http://viewvc.slicer.org/viewcvs.cgi/trunk/Libs/MGHImageIO/
 * 
 * See slicer-license.txt for Slicer3's licensing information.
 * 
 * For more information about the ITK Plugin IO mechanism, see:
 * http://www.itk.org/Wiki/Plugin_IO_mechanisms
 * #L%
 */

#ifndef H_ITK_BIO_FORMATS_IMAGE_IO_FACTORY_H
#define H_ITK_BIO_FORMATS_IMAGE_IO_FACTORY_H

#include "itkObjectFactoryBase.h"
#include "itkImageIOBase.h"

#include "itkBioFormatsIOWin32Header.h"

namespace itk
{

  class BioFormatsImageIO_EXPORT BioFormatsImageIOFactory : public ObjectFactoryBase
  {
  public:
    /** Standard class typedefs **/
    typedef BioFormatsImageIOFactory  Self;
    typedef ObjectFactoryBase         Superclass;
    typedef SmartPointer<Self>        Pointer;
    typedef SmartPointer<const Self>  ConstPointer;

    /** Class methods used to interface with the registered factories **/
    virtual const char* GetITKSourceVersion(void) const;
    virtual const char* GetDescription(void) const;

    /** Method for class instantiation **/
    itkFactorylessNewMacro(Self);

    /** RTTI (and related methods) **/
    itkTypeMacro(BioFormatsImageIOFactory, ObjectFactoryBase);

    /** Register one factory of this type **/
    static void RegisterOneFactory(void)
    {
      BioFormatsImageIOFactory::Pointer BioFormatsFactory =
        BioFormatsImageIOFactory::New();
      ObjectFactoryBase::RegisterFactory(BioFormatsFactory);
    }

  protected:
    BioFormatsImageIOFactory();
    ~BioFormatsImageIOFactory();

  private:
    BioFormatsImageIOFactory(const Self&); // purposely not implemented
    void operator=(const Self&); // purposely not implemented

  }; // end class BioFormatsImageIOFactory

} // end namespace itk

#endif // H_ITK_BIO_FORMATS_IMAGE_IO_FACTORY_H
