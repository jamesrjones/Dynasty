package com.lunasoft.dynasty.tools.mapeditor;

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class TileData implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum ReliefType {
		WATER(VegetationType.NONE),
		PLAINS(VegetationType.DESERT, VegetationType.FLOOD_PLAIN, VegetationType.FOREST,
				VegetationType.GRASSLAND, VegetationType.MARSH),
		HILLS(VegetationType.DESERT, VegetationType.FOREST, VegetationType.GRASSLAND),
		MOUNTAINS(VegetationType.NONE);

		private Set<VegetationType> validVegetationTypes;

		private ReliefType(VegetationType... validVegetationTypes) {
			this.validVegetationTypes = ImmutableSet.copyOf(validVegetationTypes);
		}

		public Set<VegetationType> getValidVegetationTypes() {
			return validVegetationTypes;
		}
	}

	public enum VegetationType {
		DESERT,
		FLOOD_PLAIN,
		FOREST,
		GRASSLAND,
		MARSH,
		NONE
	}

	private final ReliefType reliefType;
	private final VegetationType vegetationType;
	private final StrongholdData stronghold;
	private final boolean developed;

	public TileData(ReliefType reliefType, VegetationType vegetationType,
			StrongholdData stronghold, boolean developed) {
		this.reliefType = reliefType;
		this.vegetationType = vegetationType;
		this.stronghold = stronghold;
		this.developed = developed;
	}

	public TileData(ReliefType reliefType, VegetationType vegetationType) {
		this(reliefType, vegetationType, null, false);
	}

	public ReliefType getReliefType() {
		return reliefType;
	}

	public VegetationType getVegetationType() {
		return vegetationType;
	}

	public StrongholdData getStronghold() {
		return stronghold;
	}

	public boolean isDeveloped() {
		return developed;
	}
}
