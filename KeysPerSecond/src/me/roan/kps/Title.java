package me.roan.kps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Title {

	private static Process cmd;
	private static PrintWriter out;
	private static BufferedReader in;
	private static int pid;
	
	public static void main(String[] args){
		System.load("C:\\Users\\Roan\\Java development\\KeysPerSecondNative\\me.roan.kps\\KeysPerSecond.dll");
		String str = getMSNStatus();
		System.out.println(str);
	}
	
	public static void main2(String[] args) throws IOException{
		init(OS.WINDOWS);
		pid = OS.WINDOWS.getOsuPID();
		while(true){
			System.out.println(OS.WINDOWS.getOsuWindowTitle(pid));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected static void init(OS os) throws IOException{
		cmd = Runtime.getRuntime().exec(os.shell);
		in = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
		out = new PrintWriter(cmd.getOutputStream());
		os.prepare();
	}
	
	private static enum OS{
		WINDOWS("cmd"){
			@Override
			protected int getOsuPID() throws IOException {
				out.println("tasklist /FO CSV");
				out.flush();
				String line;
				int pid = -1;
				while((line = in.readLine()) != null){
					if(line.startsWith("\"osu!.exe")){
						pid = Integer.parseInt(line.split("\",\"")[1]);
					}
					if(line.replace(" ", "").isEmpty()){
						break;
					}
				}
				return pid;
			}

			@Override
			protected void prepare() throws IOException {
				String line;
				while((line = in.readLine()) != null){
					if(line.replace(" ", "").isEmpty()){
						break;
					}
				}
			}

			@Override
			protected String getOsuWindowTitle(int pid) throws IOException {
				out.println(String.format("tasklist /V /FO CSV /FI \"PID eq %1$d\"", pid));
				out.flush();
				in.readLine();//consume command
				int n = in.readLine().split("\",\"").length;
				String title = in.readLine().split("\",\"")[n - 1];
				in.readLine();//consume empty line
				return title.substring(0, title.length() - 1);
			}
		},
		LINUX("bash"){
			@Override
			protected int getOsuPID() {
				// TODO Auto-generated method stub
				return -1;
			}

			@Override
			protected void prepare() throws IOException {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected String getOsuWindowTitle(int pid) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		},
		MAC(null){
			@Override
			protected int getOsuPID() {
				// TODO Auto-generated method stub
				return -1;
			}

			@Override
			protected void prepare() throws IOException {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected String getOsuWindowTitle(int pid) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		private final String shell;

		private OS(String shell){
			this.shell = shell;
		}
		
		protected abstract int getOsuPID() throws IOException;
		
		protected abstract void prepare() throws IOException;
		
		protected abstract String getOsuWindowTitle(int pid) throws IOException;
	}
	
	private static native final String getMSNStatus();
}
