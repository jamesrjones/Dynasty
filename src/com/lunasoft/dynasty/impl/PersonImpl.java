package com.lunasoft.dynasty.impl;

import org.joda.time.LocalDate;

import com.google.common.base.Objects;
import com.lunasoft.dynasty.GameState;
import com.lunasoft.dynasty.Person;
import com.lunasoft.dynasty.Sex;

public class PersonImpl implements Person {

	private final Long uniqueId;
	private final String name;
	private final Sex sex;
	private final LocalDate birthDate;
	private final LocalDate deathDate;

	public PersonImpl(Long uniqueId, String name, Sex sex, LocalDate birthDate, LocalDate deathDate) {
		this.uniqueId = uniqueId;
		this.name = name;
		this.sex = sex;
		this.birthDate = birthDate;
		this.deathDate = deathDate;
	}

	@Override
	public Long getUniqueId() {
		return uniqueId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Sex getSex() {
		return sex;
	}

	@Override
	public LocalDate getBirthDate() {
		return birthDate;
	}

	@Override
	public LocalDate getDeathDate() {
		return deathDate;
	}

	@Override
	public Person step(GameState gameState) {
		checkDeathFromNaturalCauses(gameState);
		return null;
	}

	private void checkDeathFromNaturalCauses(GameState gameState) {
		
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(uniqueId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PersonImpl other = (PersonImpl) obj;
		return Objects.equal(uniqueId, other.uniqueId);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("uniqueId", uniqueId)
				.add("name", name)
				.add("sex", sex)
				.add("birthDate", birthDate)
				.add("deathDate", deathDate)
				.toString();
	}
}