package de.learnlib.ralib.data;

import gov.nasa.jpf.constraints.api.Expression;
import net.automatalib.data.DataType;
import net.automatalib.data.SymbolicDataValue;

public final class SuffixValue<T> extends SymbolicDataValue<T> implements SDTGuardElement {

    public SuffixValue(DataType<T> dataType, int id) {
        super(dataType, "s" + id, id);
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

