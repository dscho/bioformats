//
// bioFormatsJNI.cpp
//

/*
OME Bio-Formats C++ bindings for native access to Bio-Formats Java library.
Copyright (C) 2008-@year@ UW-Madison LOCI and Glencoe Software, Inc.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

// An example of how to call Bio-Formats from C++ using raw JNI
// (i.e., without the C++ bindings).

#include <jni.h>

#include <iostream>
using namespace std;

int main(int argc, char* argv[]) {
  JavaVM *jvm;
  JNIEnv *env;

  // NB: This program requires loci_tools.jar in the same directory.
  string classpath = "-Djava.class.path";
  const int numJars = 1;
  string jars[numJars] = {
    "loci_tools.jar"
  };
  for (int i=0; i<numJars; i++) {
    classpath += i == 0 ? "=" : ":";
    classpath += jars[i];
  }
  cout << "Classpath = " + classpath << endl;

  // get the default initialization arguments and set the class path
  JavaVMInitArgs vm_args;
  JNI_GetDefaultJavaVMInitArgs(&vm_args);
  JavaVMOption options[1];
  options[0].optionString = (char*) classpath.c_str();
  //options[1].optionString = "-verbose:jni";
  vm_args.version = 0x00010004; // VM version 1.4
  vm_args.options = options;
  vm_args.nOptions = 2;

  // load and initialize a Java VM, return a JNI interface pointer in env
  JNI_CreateJavaVM(&jvm, (void**) &env, &vm_args);

  // invoke the ImageInfo.main(String[]) method using the JNI
  jclass imageInfoClass = env->FindClass("loci/formats/tools/ImageInfo");
  cout << "Got ImageInfo class: " << imageInfoClass << endl;
  jmethodID mid = env->GetStaticMethodID(imageInfoClass,
    "main", "([Ljava/lang/String;)V");
  cout << "Got main method: " << mid << endl;
  jclass stringClass = env->FindClass("java/lang/String");
  cout << "Got String class: " << stringClass << endl;

  jobjectArray args = env->NewObjectArray(argc - 1, stringClass, 0);
  for (int i=1; i<argc; i++) {
    jstring arg = env->NewStringUTF(argv[i]);
    env->SetObjectArrayElement(args, i - 1, arg);
  }
  cout << "Got object array: " << args << endl;

  env->CallStaticVoidMethod(imageInfoClass, mid, args);

  jvm->DestroyJavaVM();
}
