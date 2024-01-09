package dev.roanh.kps.config;

import java.util.Objects;

import dev.roanh.util.Util;

public class Version implements Comparable<Version>{
	public static final Version UNKNOWN = new Version();
	private final int major;
	private final int minor;
	
	public Version(int major, int minor){
		this.major = major;
		this.minor = minor;
	}
	
	private Version(){
		this(-1, -1);
	}
	
	public boolean isBefore(int major, int minor){
		return this.major == major ? (this.minor < minor) : (this.major < major);
	}
	
	public boolean isBefore(Version version){
		return this.compareTo(version) < 0;
	}
	
	public boolean isUnknown(){
		return this == UNKNOWN;
	}
	
	public static final Version readVersion(){
		String version = Util.readArtifactVersion("dev.roanh.kps", "keyspersecond");
		if(version != null){
			try{
				return parse(version);
			}catch(IllegalArgumentException e){
				return UNKNOWN;
			}
		}else{
			return UNKNOWN;
		}
	}
	
	public static final Version parse(String version) throws IllegalArgumentException{
		version = version.trim();
		if(version.startsWith("v")){
			version = version.substring(1);
		}
		
		try{
			String[] args = version.split("\\.");
			return new Version(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public int compareTo(Version o){
		return major == o.major ? Integer.compare(minor, o.minor) : Integer.compare(major, o.major);
	}
	
	@Override
	public String toString(){
		return isUnknown() ? "unknown" : ("v" + major + "." + minor);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Version){
			Version other = (Version)obj;
			return major == other.major && minor == other.minor;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(major, minor);
	}
}
