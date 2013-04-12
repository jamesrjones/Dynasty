package com.lunasoft.dynasty.constants;

import java.util.Map;

import com.google.common.collect.Maps;
import com.lunasoft.dynasty.HeritableTrait;
import com.lunasoft.dynasty.InheritanceModel;

public class InheritanceModels {

	private static final Map<HeritableTrait, InheritanceModel> MODELS = Maps.newEnumMap(
			HeritableTrait.class);

	static {
	}

	public InheritanceModel getModel(HeritableTrait trait) {
		return MODELS.get(trait);
	}
}
