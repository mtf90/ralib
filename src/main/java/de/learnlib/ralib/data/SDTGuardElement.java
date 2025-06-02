package de.learnlib.ralib.data;

import gov.nasa.jpf.constraints.api.Expression;
import net.automatalib.data.DataValue;
import net.automatalib.data.SymbolicDataValue;
import net.automatalib.data.TypedValue;

public interface SDTGuardElement extends TypedValue {

    static boolean isConstant(SDTGuardElement e) {
        return e.getClass().equals(SymbolicDataValue.Constant.class);
    }

    static boolean isDataValue(SDTGuardElement e) {
        return e.getClass().equals(DataValue.class);
    }

    static boolean isSuffixValue(SDTGuardElement e) {
        return e.getClass().equals(SuffixValue.class);
    }

    static boolean isRegister(SDTGuardElement e) {
    	return e.getClass().equals(SymbolicDataValue.Register.class);
    }

    Expression<?> asExpression();

}
