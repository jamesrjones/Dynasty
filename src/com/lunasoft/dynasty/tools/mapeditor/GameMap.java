package com.lunasoft.dynasty.tools.mapeditor;

import java.io.Serializable;

/**
 * Represents a game map organized as a grid of hexes. The map may wrap or not.
 * If it wraps, it will wrap only in the horizontal direction.
 */
public class GameMap implements Serializable {

	private static final long serialVersionUID = 91291872L;

	private final boolean isWrapping;
	private final int width;
	private final int height;
	private final TileData[][] data;

	public GameMap(boolean isWrapping, int width, int height) {
		this.isWrapping = isWrapping;
		this.width = width;
		this.height = height;
		this.data = new TileData[width][height];
	}

	public void setTile(int i, int j, TileData tileData) {
		data[i][j] = tileData;
	}

	public boolean isWrapping() {
		return isWrapping;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public TileData getTile(int i, int j) {
		return data[i][j];
	}
}
