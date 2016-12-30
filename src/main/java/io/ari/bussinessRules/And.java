package io.ari.bussinessRules;


import java.util.Collection;

import static com.google.common.collect.ImmutableSet.of;

public class And<T> implements BusinessRule<T> {

	@Override
	public Collection<Violation> isSatisfied(T entity, Object... additionalData) {
		boolean anyViolation = rules.stream().anyMatch(rule -> !rule.isSatisfied(entity).isEmpty());
		if (anyViolation) {
			return of(new Violation(this.code, this.message));
		} 
		
		return of();
		
	}

	private Collection<BusinessRule<T>> rules;
	private String code;
	private String message;

	public And(Collection<BusinessRule<T>> rules, String code, String message) {
		this.rules = rules;
		this.code = code;
		this.message = message;
	}
}
