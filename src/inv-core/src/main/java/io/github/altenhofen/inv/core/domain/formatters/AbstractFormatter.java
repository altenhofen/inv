package io.github.altenhofen.inv.core.domain.formatters;


public abstract class AbstractFormatter<T> {
    public abstract String format(T rawValue);
}