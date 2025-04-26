package mgi.types.draw.model;


public abstract class Renderable {

	public int height;

	protected Renderable() {
		this.height = 1000; // L: 6
	} // L: 8

	protected Model getModel() {
		return null; // L: 19
	}

	void draw(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
		Model var11 = this.getModel(); // L: 11
		if (var11 != null) { // L: 12
			this.height = var11.height; // L: 13
			var11.draw(var1, var2, var3, var4, var5, var6, var7, var8, var9); // L: 14
		}

	} // L: 16
}
