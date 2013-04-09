package com.lunasoft.dynasty;

/**
 * An InheritanceModel in Dynasty describes the way a multi-factor genetic trait
 * is inherited from parents to child. Each parent will have a given "trait value",
 * a double. When a child is conceived, its trait value is determined by obtaining
 * a normal variate with mean equal to (mother + father)/2 and variance given below.
 *
 * To determine expression, a second normal variate is obtained with mean equal to
 * the trait value and variance given below. The trait is expressed only if the
 * variate exceeds the threshold given below.
 */
public interface InheritanceModel {

	public double getVariance();  // the variance of the inherited trait
	public double getThreshold(); // the threshold for trait expression
}
