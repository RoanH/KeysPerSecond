/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		return version == null ? UNKNOWN : parse(version);
	}
	
	public static final Version parse(String version){
		version = version.trim();
		if(version.startsWith("v")){
			version = version.substring(1);
		}
		
		try{
			String[] args = version.split("\\.");
			return new Version(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			return UNKNOWN;
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
