package io.ari.bussinessRules;


import com.google.common.collect.ImmutableSet;

import java.util.Collection;

import static com.google.common.collect.ImmutableSet.of;

public class Not<T> implements BusinessRule<T> {

	public Not(BusinessRule<T> rule, String code, String message) {
		this.rule = rule;
		this.code = code;
		this.message = message;
	}

	@Override
	public Collection<Violation> isSatisfied(T entity, Object... additionalData) {
		if (rule.isSatisfied(entity, additionalData).isEmpty()) {
			return ImmutableSet.of(getViolation());
		}

		return of();
	}

	private Violation getViolation() {
		return new Violation(code, message);
	}

	private BusinessRule<T> rule;
	private String code;
	private String message;
}
