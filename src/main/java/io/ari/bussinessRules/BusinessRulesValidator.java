package io.ari.bussinessRules;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

public class BusinessRulesValidator<T> {

	public BusinessRulesValidator(Set<BusinessRule<T>> rules) {
		this.rules = rules;
	}

	BusinessRulesValidator() {
	}

	public Collection<Violation> validate(T entity, Object... additionalData) {
		Set<Violation> collect = rules.stream()
				.flatMap(rule -> rule.isSatisfied(entity, additionalData).stream())
				.collect(Collectors.toSet());
		return ImmutableSet.copyOf(collect);
	}

	public void setRules(Set<BusinessRule<T>> rules) {
		this.rules = rules;
	}

	private Set<BusinessRule<T>> rules = newHashSet();
}
