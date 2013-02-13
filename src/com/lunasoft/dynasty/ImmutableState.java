package com.lunasoft.dynasty;

import org.joda.time.LocalDate;

/**
 * Holds state that is immutable over the course of the game, e.g. certain map details.
 */
public interface ImmutableState {

	public LocalDate getDateAtStartOfTurn(int turn);
	public Integer getTurnNumberOnDate(LocalDate date);
}
