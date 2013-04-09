package com.lunasoft.dynasty;

import java.util.Set;

public interface MutableState {

	public Set<Person> getPersons();
	public Set<Person> getLivingPersons();
	public Set<Relationship> getRelationships();
	public Integer getCurrentTurn();

	public IdGenerator getIdGenerator();
}
