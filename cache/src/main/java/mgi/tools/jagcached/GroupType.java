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
 * @author Tommeh | 6 aug. 2018 | 13:32:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum GroupType {

	UNDERLAY(1),
	IDENTKIT(3),
	OVERLAY(4),
	INV(5),
	OBJECT(6),
	ENUM(8),
	NPC(9),
	ITEM(10),
	PARAMS(11),
	SEQUENCE(12),
	SPOTANIM(13),
	VARBIT(14),
	VARCLIENTSTRING(15),
	VARPLAYER(16),
	VARCLIENT(19),
	HITMARK(32),
	HITBAR(33),
	STRUCT(34),
	MAP_LABELS(35),
	DBROW(38),
	DBTABLE(39);

	GroupType(int id) {
		this.id = id;
	}

	private final int id;
	
	public int getId() {
	    return id;
	}
}
