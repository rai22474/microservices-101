package io.ari.bussinessRules;

import java.util.Collection;

import static com.google.common.collect.ImmutableSet.of;

public class Or<T> implements BusinessRule<T>{

	@Override
	public Collection<Violation> isSatisfied(T entity, Object... additionalData) {
		boolean anyOk = rules.stream().anyMatch(rule -> rule.isSatisfied(entity).isEmpty());
		if (anyOk) {
			return of();
		}
		
		return of(new Violation(this.code, this.message));
	
	}

	private Collection<BusinessRule<T>> rules;
	private String code;
	private String message;

	public Or(Collection<BusinessRule<T>> rules, String code, String message) {
		this.rules = rules;
		this.code = code;
		this.message = message;
	}
}
