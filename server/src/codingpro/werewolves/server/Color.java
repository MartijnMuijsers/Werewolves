package codingpro.werewolves.server;

public class Color {
	
	public final int red;
	public final int green;
	public final int blue;
	
	public Color(int red, int green, int blue) {
		if (red > 255) {
			red = 255;
		} else if (red < 0) {
			red = 0;
		}
		if (green > 255) {
			green = 255;
		} else if (green < 0) {
			green = 0;
		}
		if (blue > 255) {
			blue = 255;
		} else if (blue < 0) {
			blue = 0;
		}
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public Color setRed(int red) {
		return new Color(red, green, blue);
	}
	
	public Color setGreen(int green) {
		return new Color(red, green, blue);
	}
	
	public Color setBlue(int blue) {
		return new Color(red, green, blue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Color) {
			Color other = (Color) obj;
			return other.red == red && other.green == green && other.blue == blue;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int green2 = green+1747;
		int blue2 = blue+7194513;
		return red+green2*green2+blue2*blue2*blue2;
	}
	
}
