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

/**
 * Class representing program versions.
 * @author Roan
 */
public class Version implements Comparable<Version>{
	/**
	 * Constant used for unknown versions.
	 */
	public static final Version UNKNOWN = new Version();
	/**
	 * The major version number.
	 */
	private final int major;
	/**
	 * The minor version number.
	 */
	private final int minor;
	
	/**
	 * Constructs a new version with the given major and minor version.
	 * @param major The major version number.
	 * @param minor The minor version number.
	 */
	public Version(int major, int minor){
		this.major = major;
		this.minor = minor;
	}
	
	/**
	 * Construct for {@link #UNKNOWN}.
	 */
	private Version(){
		this(-1, -1);
	}
	
	/**
	 * Tests if this version is strictly before the given other version.
	 * @param major The major version number of the other version.
	 * @param minor The minor version number of the other version.
	 * @return True if this version is strictly before the given version.
	 */
	public boolean isBefore(int major, int minor){
		return this.major == major ? (this.minor < minor) : (this.major < major);
	}
	
	/**
	 * Tests if this version is strictly before the given other version.
	 * @param version The other version to test against.
	 * @return True if this version is strictly before the given version.
	 */
	public boolean isBefore(Version version){
		return this.compareTo(version) < 0;
	}
	
	/**
	 * Tests if this version represents the unknown version.
	 * @return True if this version is 'unknown'.
	 * @see #UNKNOWN
	 */
	public boolean isUnknown(){
		return this == UNKNOWN;
	}
	
	public boolean equals(String other){
		return Version.parse(other).equals(this);
	}
	
	/**
	 * Reads the current KeysPerSecond version from the JAR.
	 * @return The current KeysPerSecond version.
	 */
	public static final Version readVersion(){
		String version = Util.readArtifactVersion("dev.roanh.kps", "keyspersecond");
		return version == null ? UNKNOWN : parse(version);
	}
	
	/**
	 * Parses the given string encoded version number in
	 * 'v8.0' or '8.0' format.
	 * @param version The version to parse.
	 * @return The parsed version or {@link #UNKNOWN} if parsing failed.
	 */
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
