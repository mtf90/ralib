package de.learnlib.ralib.smt;

import java.util.*;
import java.util.Map.Entry;

import de.learnlib.ralib.data.Bijection;
import de.learnlib.ralib.data.RegisterAssignment;
import gov.nasa.jpf.constraints.api.Expression;
import gov.nasa.jpf.constraints.api.Valuation;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.constraints.expressions.Constant;
import gov.nasa.jpf.constraints.expressions.NumericBooleanExpression;
import gov.nasa.jpf.constraints.expressions.NumericComparator;
import gov.nasa.jpf.constraints.types.BuiltinTypes;
import gov.nasa.jpf.constraints.util.ExpressionUtil;
import net.automatalib.automaton.ra.Util;
import net.automatalib.data.DataValue;
import net.automatalib.data.Mapping;
import net.automatalib.data.SymbolicDataValue;
import net.automatalib.data.VarMapping;

public class SMTUtil {

    public static Valuation compose(net.automatalib.data.Valuation<? extends SymbolicDataValue<?>, DataValue<?>>... varVals) {
        return Util.compose(varVals);
    }

    public static Constant constantFor(DataValue sv) {
        return new Constant(BuiltinTypes.DECIMAL, sv.getValue());
    }

    public static Collection<SymbolicDataValue> getSymbolicDataValues(Expression<Boolean> expr) {
        ArrayList<SymbolicDataValue> list = new ArrayList<>();
        for (Variable v : ExpressionUtil.freeVariables(expr)) {
            list.add((SymbolicDataValue) v);
        }
        return list;
    }

    public static Expression<Boolean> renameVars(Expression<Boolean> expr,
	    final VarMapping<? extends SymbolicDataValue, ? extends SymbolicDataValue> relabelling) {
        final ReplacingVarsVisitor replacer = new ReplacingVarsVisitor();
        return replacer.apply(expr, relabelling);
    }

    public static Expression<Boolean> renameVals(Expression<Boolean> expr, Bijection<DataValue> renaming) {
        final ReplacingValuesVisitor replacer = new ReplacingValuesVisitor();
        Mapping<DataValue<?>, DataValue<?>> map = new Mapping<>();
        for (Entry<DataValue, DataValue> e : renaming.entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
        return replacer.apply(expr, map);
    }

    public static Expression<Boolean> valsToRegisters(Expression<Boolean> expr, RegisterAssignment ra) {
        final ReplacingValuesVisitor replacer = new ReplacingValuesVisitor();
        RegisterAssignment map = new RegisterAssignment();
        map.putAll(ra);
        return replacer.apply(expr, map);
    }

    public static Expression<Boolean> toExpression(Expression<Boolean> expr, Mapping<SymbolicDataValue, DataValue> val) {
        Map<SymbolicDataValue, Variable> map = new HashMap<>();
        Expression<Boolean> valExpr = toExpression(val, map);
        return ExpressionUtil.and(expr, valExpr);
    }

    public static Expression<Boolean> toExpression(Mapping<SymbolicDataValue, DataValue> val, Map<SymbolicDataValue, Variable> map) {
        Expression<Boolean>[] elems = new Expression[val.size()];
        int i = 0;
        for (Map.Entry<SymbolicDataValue, DataValue> entry : val.entrySet()) {
            elems[i++] = new NumericBooleanExpression(getOrCreate(entry.getKey(), map), NumericComparator.EQ, toConstant(entry.getValue()));
        }
        return ExpressionUtil.and(elems);
    }

    private static Variable getOrCreate(SymbolicDataValue dv, Map<SymbolicDataValue, Variable> map) {
        Variable ret = map.get(dv);
        if (ret == null) {
            // FIXME: superfluous!
            ret = dv;
            map.put(dv, ret);
        }
        return ret;
    }

    public static Constant toConstant(DataValue v) {
        return new Constant( BuiltinTypes.DECIMAL, (v.getValue()));
    }

    public static Variable toVariable(DataValue v) {
        return new Variable(BuiltinTypes.DECIMAL, v.toString());
    }
}
