package mgi.types.draw.model;

public class VertexNormal {

	int x;
	int y;
	int z;
	int magnitude;

	VertexNormal() {
	} // L: 9

	VertexNormal(VertexNormal var1) {
		this.x = var1.x; // L: 12
		this.y = var1.y; // L: 13
		this.z = var1.z; // L: 14
		this.magnitude = var1.magnitude; // L: 15
	} // L: 16
}
