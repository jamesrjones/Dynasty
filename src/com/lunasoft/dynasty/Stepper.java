package com.lunasoft.dynasty;

import java.util.Set;

import com.google.common.collect.Sets;

public class Stepper {

	public MutableState step(GameState gameState) {
		Set<Person> persons = gameState.getMutableState().getLivingPersons();
		Set<Person> updatedPersons = Sets.newHashSet();
		for (Person person : persons) {
			// update state for person
			updatedPersons.add(person.step(gameState));
		}
		return null;
	}
}
