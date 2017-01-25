package me.roan.kps.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import javax.swing.JOptionPane;

public class Launcher{
	
	public static void main(String[] args){
		try {
			//no support for mouse keys for now
			String libname = "keyboardhook" + "-" + getOperatingSystemName() + "-" + getOperatingSystemArchitecture() + ".dll";
			Path tmp = Files.createTempDirectory("kps");
			copyResource(tmp, libname, ClassLoader.getSystemResourceAsStream(libname));
			copyResource(tmp, "KPSCore.jar", ClassLoader.getSystemResourceAsStream("KPSCore.jar"));
			
			String javaexe = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe";
			ProcessBuilder proc = new ProcessBuilder();
			proc.command(javaexe, "-Djava.library.path=" + tmp.toAbsolutePath().toString(), "-jar", new File(tmp.toFile(), "KPSCore.jar").toPath().toAbsolutePath().toString());
			Process p = proc.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while(p.isAlive()){
				while(in.ready()){
					System.out.println(in.readLine());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			for(File file : tmp.toFile().listFiles()){
				file.deleteOnExit();
				file.delete();
			}
			tmp.toFile().deleteOnExit();
			tmp.toFile().delete();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error whilst launching application >.<", "Keys per second", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private static final void copyResource(Path tmp, String name, InputStream res) throws IOException{
		File lib = new File(tmp.toFile(), name);
		lib.createNewFile();
		FileOutputStream out = new FileOutputStream(lib);
		byte[] buffer = new byte[1024];
		int len;
		while((len = res.read(buffer)) != -1){
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		res.close();
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
