/**
* Copyright (c) Kyle Fricilone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mgi.tools.jagcached;

/**
 * @author Tommeh | 6 aug. 2018 | 13:32:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum ArchiveType {

	FRAMES(0),
	BASES(1),
	CONFIGS(2),
	INTERFACES(3),
	SYNTHS(4),
	MAPS(5),
	MUSIC(6),
	MODELS(7),
	SPRITES(8),
	TEXTURES(9),
	BINARY(10),
	JINGLES(11),
	CLIENTSCRIPTS(12),
	FONTMETRICS(13),
    VORBIS(14),
    INSTRUMENTS(15),
    WORLDMAPDATA_LEGACY(16),
    DEFAULTS(17),
    WORLDMAPGEOGRAPHY(18),
    WORLDMAPDATA(19),
    WORLDMAPGROUND(20),
    DBTABLEINDEX(21),
    REFERENCE(255);

    private final int id;

    ArchiveType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
