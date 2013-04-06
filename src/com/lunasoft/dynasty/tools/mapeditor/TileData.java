package com.lunasoft.dynasty.tools.mapeditor;

import java.io.Serializable;

public class TileData implements Serializable {

	private static final long serialVersionUID = 1287409128L;

	public enum TerrainType {
		OCEAN,
		PLAINS,
		HILLS,
		MOUNTAINS;
	}

	private final TileData.TerrainType terrainType;

	public TileData(TileData.TerrainType terrainType) {
		this.terrainType = terrainType;
	}

	public TerrainType getTerrainType() {
		return terrainType;
	}
}