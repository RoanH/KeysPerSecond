package dev.roanh.kps.config.setting;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.ThemeColor;

public class ColorSetting extends Setting<ThemeColor>{
	//rgb = group 1-2-3
	private static final Pattern LEGACY_COLOR_REGEX = Pattern.compile("\\[r=(\\d{1,3}),g=(\\d{1,3}),b=(\\d{1,3})]");
	//rgb = group 1, alpha = group 2 if exists
	private static final Pattern COLOR_REGEX = Pattern.compile("#([A-F0-9]{6})([A-F0-9]{2})?");
	
	
	public ColorSetting(String key, ThemeColor defaultValue){
		super(key, defaultValue);
	}
	
	@Override
	public boolean parse(String data){
		Matcher m = LEGACY_COLOR_REGEX.matcher(data);
		if(m.matches()){
			int r = Integer.parseInt(m.group(1));
			int g = Integer.parseInt(m.group(2));
			int b = Integer.parseInt(m.group(3));
			if(r > 255 || g > 255 || b > 255){
				reset();
				return true;
			}else{
				update(new ThemeColor(r, g, b, value.getAlpha()));
				return false;
			}
		}else{
			data = data.toUpperCase(Locale.ROOT);
			m = COLOR_REGEX.matcher(data);
			if(m.matches()){
				int rgb = Integer.parseUnsignedInt(m.group(1), 16);
				String alphaStr = m.group(2);
				int alpha = alphaStr == null ? 0xFF : Integer.parseUnsignedInt(alphaStr, 16);
				update(new ThemeColor(rgb, alpha));
				return false;
			}else{
				reset();
				return true;
			}
		}
	}
	
	@Override
	public void write(IndentWriter out){
		out.println(key + ": #" + value.toHex());
	}
}
