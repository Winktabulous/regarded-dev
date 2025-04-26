package mgi.tools.jagcached.cache;

import mgi.tools.jagcached.Helper;
import mgi.utilities.ByteBuffer;

import java.util.Arrays;

public class File {

	/**
	 * Contains file ID.
	 */
	private int id;

	/**
	 * Contains name hash of this file.
	 */
	private int name;

	/**
	 * Contains file data if loaded.
	 */
	private ByteBuffer data;

	/**
	 * Whether index info about this file
	 * changed in any way.
	 */
	private boolean indexChanged;

	/**
	 * Whether file data was changed.
	 */
	private boolean dataChanged;
	
	/**
	 * This constructor can be used only by archive
	 * loader.
	 */
	public File(int fileID, int name) {
		this(fileID,name,null);
	}

	/**
	 * Constructs new file with autoassigned ID.
	 */
	public File(String name,ByteBuffer data) {
		this(-1,Helper.strToI(name),data);
	}
	
	/**
	 * Constructs new file with autoassigned ID.
	 */
	public File(ByteBuffer data) {
		this(-1,-1,data);
	}

	public File(int filID, ByteBuffer data) {
		this(filID,-1,data);
	}
	
	public File(int fileID,String name,ByteBuffer data) {
		this(fileID,Helper.strToI(name),data);
	}
	
	public File(int fileID,int name, ByteBuffer data) {
		this.id = fileID;
		this.name = name;
		this.data = data;
	}
	
	/**
	 * Copies this file, including the 
	 * buffer if it's present.
	 */
	public File copy() {
		File f = new File(id,name);
		if (data != null) {
			f.data = new ByteBuffer(data.toArray(0, data.getBuffer().length),data.getPosition());
		}
		return f;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file.");
		if (this.id != id) {
			this.id = id;
			indexChanged = true;
		}
	}
	
	public int getName() {
		return name;
	}
	
	public void setName(String name) {
		setName(Helper.strToI(name));
	}
	
	public void setName(int name) {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file.");
		
		if (this.name != name) {
			indexChanged = true;
			this.name = name;
		}
	}
	
	public ByteBuffer getData() {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file.");
		return data;
	}
	
	/**
	 * Loads this file.
	 */
	public void load(ByteBuffer data) {
		if (isLoaded())
			throw new RuntimeException("Already loaded.");
		this.data = data;
	}
	
	/**
	 * Sets file data.
	 */
	public void setData(ByteBuffer data) {
		if (!isLoaded()) {
			throw new RuntimeException("Using nonloaded file.");
		}

		// only update if the two buffers don't match
		if (!Arrays.equals(this.data.getBuffer(), data.getBuffer())) {
			dataChanged = indexChanged = true;
			this.data = data;
		}
	}
	
	/**
	 * Whether this file is loaded.
	 */
	public boolean isLoaded() {
		return data != null;
	}
	
	/**
	 * Unloads this file , can be called
	 * only by folder unload() method.
	 */
	public void unload() {
		if (!isLoaded())
			throw new RuntimeException("Using nonloaded file.");
		data = null;
	}
	
	/**
	 * Whether index info about this file changed.
	 */
	public boolean isIndexInfoChanged() {
		return indexChanged;
	}
	
	/**
	 * Whether data was changed.
	 */
	public boolean isDataChanged() {
		return dataChanged;
	}
	
	/**
	 * Marks this file as not changed.
	 */
	public void markIndexAsNotChanged() {
		indexChanged = false;
	}
	
	/**
	 * Marks data as not changed.
	 */
	public void markDataAsNotChanged() {
		dataChanged = false;
	}
	
}
