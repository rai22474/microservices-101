package io.ari.bussinessRules;

import java.util.Collection;

public interface BusinessRule<T> {

	Collection<Violation> isSatisfied(T entity, Object... additionalData);

}
