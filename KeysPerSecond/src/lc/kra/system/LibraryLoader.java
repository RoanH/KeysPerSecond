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
import java.util.Locale;

import me.roan.kps.Main;

public class LibraryLoader {
		
	/**
	 * Tries to load the library with the given name
	 * 
	 * @param name The name of the library to load
	 * @throws UnsatisfiedLinkError Thrown in case loading the library fails
	 */
	public static void loadLibrary(String name) throws UnsatisfiedLinkError {
		System.load(Main.dir + File.separator + name + "-" + getOperatingSystemName() + "-" + getOperatingSystemArchitecture() + ".dll");
	}
	
	private static String getOperatingSystemName() {
		String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		if(osName.startsWith("windows")){
			return "windows";
		}else if(osName.startsWith("linux")){
			return "linux";
		}else if(osName.startsWith("mac os")){
			return "darwin";
		}else if(osName.startsWith("sunos") || osName.startsWith("solaris")){
			return "solaris";
		}else{
			return osName;
		}
	}

	private static String getOperatingSystemArchitecture() {
		String osArch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
		if((osArch.startsWith("i") || osArch.startsWith("x")) && osArch.endsWith("86")){
			return "x86";
		}else if((osArch.equals("i86") || osArch.startsWith("amd")) && osArch.endsWith("64")){
			return "amd64";
		}else if(osArch.startsWith("arm")){
			return "arm";
		}else if(osArch.startsWith("sparc")){
			return !osArch.endsWith("64") ? "sparc" : "sparc64";
		}else if(osArch.startsWith("ppc")){
			return !osArch.endsWith("64") ? "ppc" : "ppc64";
		}else return osArch;
	}
}