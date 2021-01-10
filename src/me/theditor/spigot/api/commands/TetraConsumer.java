package me.theditor.spigot.api.commands;

import java.util.Objects;

@FunctionalInterface
public interface TetraConsumer<I, U, T, L> {

    void accept(I i, U u, T t, L l);

    default TetraConsumer<I, U, T, L> andThen(TetraConsumer<? super I, ? super U, ? super T, ? super L> after) {
        Objects.requireNonNull(after);

        return (i, u, t, l) -> {
            accept(i, u, t, l);
            after.accept(i, u, t, l);
        };
    }
}
