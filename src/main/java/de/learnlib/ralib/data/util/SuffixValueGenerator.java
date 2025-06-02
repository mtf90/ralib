package de.learnlib.ralib.data.util;

import de.learnlib.ralib.data.SuffixValue;
import net.automatalib.data.DataType;
import net.automatalib.data.SymbolicDataValueGenerator;

public final class SuffixValueGenerator extends SymbolicDataValueGenerator {

    @Override
    public <T> SuffixValue<T> next(DataType<T> type) {
        return new SuffixValue<>(type, id++);
    }
}
