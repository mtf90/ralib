package de.learnlib.ralib.data;

import gov.nasa.jpf.constraints.api.Expression;
import net.automatalib.data.DataType;
import net.automatalib.data.GuardElement;
import net.automatalib.data.SymbolicDataValue;

public final class SuffixValue<T> extends SymbolicDataValue<T> implements GuardElement {

    public SuffixValue(DataType<T> dataType, int id) {
        super(dataType, "s", id);
    }


    @Override
    public SymbolicDataValue<T> copy() {
        return new SuffixValue<>(getDataType(), getId());
    }

    @Override
    public Expression<T> asExpression() {
        return this;
    }
}
