package mgi.types.draw.font;


public class FontName {

	public static final FontName FontName_plain11;
	public static final FontName FontName_plain12;
	public static final FontName FontName_bold12;
	public static final FontName FontName_verdana11;
	public static final FontName FontName_verdana13;
	public static final FontName FontName_verdana15;
	String name;

	static {
		FontName_plain11 = new FontName("p11_full"); // L: 7
		FontName_plain12 = new FontName("p12_full"); // L: 8
		FontName_bold12 = new FontName("b12_full"); // L: 9
		FontName_verdana11 = new FontName("verdana_11pt_regular"); // L: 10
		FontName_verdana13 = new FontName("verdana_13pt_regular"); // L: 11
		FontName_verdana15 = new FontName("verdana_15pt_regular"); // L: 12
	}

	FontName(String var1) {
		this.name = var1; // L: 20
	} // L: 21

    public String getName() {
        return name;
    }

    public static FontName[] all() {
		return new FontName[]{FontName_plain11, FontName_verdana13, FontName_bold12, FontName_verdana15, FontName_verdana11, FontName_plain12}; // L: 16
	}
}
