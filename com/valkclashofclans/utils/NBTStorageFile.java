package com.valkclashofclans.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class NBTStorageFile {

	// the file to use
	private final File file;
	// the TagCompound containing the files content
	private NBTTagCompound tagCompound;

	public NBTStorageFile(File file) {
		this.file = file;
	}

	// two optional constructors:
	public NBTStorageFile(String folder, String name) {
		this(new File(folder, name + ".schematic"));
	}

	public NBTStorageFile(String path) {
		this(new File(path));
	}

	/*
	 * WorldEdit Schematics have byte arrays for both
	 * "Blocks" and "Data"
	 */
	public byte[] getByteArray(String path) {
		try {
			// if the file exists we read it
			if (file.exists()) {
				FileInputStream fileinputstream = new FileInputStream(file);
				tagCompound = NBTCompressedStreamTools.a(fileinputstream);
				fileinputstream.close();
				byte[] tag = tagCompound.getByteArray(path);
				return tag;
			} else {
				// else we create an empty TagCompound
				clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * WorldEdit Schematics have shorts for
	 * "Width", "Height", "Length", "WEOffsetX", "WEOffsetY", "WEOffsetZ"
	 */
	public short getShort(String path) {
		try {
			// if the file exists we read it
			if (file.exists()) {
				FileInputStream fileinputstream = new FileInputStream(file);
				tagCompound = NBTCompressedStreamTools.a(fileinputstream);
				fileinputstream.close();
				short tag = tagCompound.getShort(path);
				return tag;
			} else {
				// else we create an empty TagCompound
				clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void clear() {
		tagCompound = new NBTTagCompound();
	}

}