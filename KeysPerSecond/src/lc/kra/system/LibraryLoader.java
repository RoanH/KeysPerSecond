/**
 * Copyright (c) 2016 Kristian Kraljic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package lc.kra.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class LibraryLoader {
	
	private static Path tempdir;
	
	/**
	 * Tries to laod the library with the given name
	 * 
	 * @param name The name of the library to load
	 * @throws UnsatisfiedLinkError Thrown in case loading the library fails
	 */
	public static void loadLibrary(String name) throws UnsatisfiedLinkError {
		try{
			String os = getOperatingSystemName();
			String arch = getOperatingSystemArchitecture();
			if(!os.equals("windows") || !(arch.equals("amd64") || arch.equals("x86"))){
				System.out.println("Unsupported operating system or architecture >.<");
				System.exit(0);
			}
			if(tempdir == null){
				tempdir = Files.createTempDirectory("kps");
				tempdir.toFile().deleteOnExit();
			}
			String libname = name + "-" + os + "-" + arch + ".dll";
			File lib = new File(tempdir.toFile(), libname);
			InputStream in = ClassLoader.getSystemResourceAsStream(libname);
			OutputStream out = new FileOutputStream(lib);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = in.read(buffer)) != -1){
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
			System.load(lib.getAbsolutePath());
		}catch(IOException | NullPointerException e){
			System.out.println("Failed to load native library!");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static String getOperatingSystemName() {
		String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		if(osName.startsWith("windows"))
			return "windows";
		else if(osName.startsWith("linux"))
			return "linux";
		else if(osName.startsWith("mac os"))
			return "darwin";
		else if(osName.startsWith("sunos")||osName.startsWith("solaris"))
			return "solaris";
		else return osName;
	}
	private static String getOperatingSystemArchitecture() {
		String osArch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
		if((osArch.startsWith("i")||osArch.startsWith("x"))&&osArch.endsWith("86"))
			return "x86";
		else if((osArch.equals("i86")||osArch.startsWith("amd"))&&osArch.endsWith("64"))
			return "amd64";
		else if(osArch.startsWith("arm"))
			return "arm";
		else if(osArch.startsWith("sparc"))
			return !osArch.endsWith("64")?"sparc":"sparc64";
		else if(osArch.startsWith("ppc"))
			return !osArch.endsWith("64")?"ppc":"ppc64";
		else return osArch;
	}
}