package com.lunasoft.dynasty;

import java.io.Serializable;

import org.joda.time.LocalDate;

/**
 * A specific "important person" tracked separately from the rest of the rabble
 * in the game. May be player- or AI-controlled.
 */
public interface Person extends MutableEntity {

	public Long getUniqueId();
	public String getName();
	public Sex getSex();
	public LocalDate getBirthDate();
	public LocalDate getDeathDate();
	public Location getLocation();
	public Person getMother(); // may be null (otherwise the tree could potentially be infinite!)
	public Person getFather(); // may be null
	public Attribute getAttribute(Attribute.Type type);
	public Skill getSkill(Skill.Type type);
	public TraitGenotype getTraitGenotype(HeritableTrait trait);

	@Override
	public Person step(GameState gameState);
}
