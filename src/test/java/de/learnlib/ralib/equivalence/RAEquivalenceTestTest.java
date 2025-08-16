package de.learnlib.ralib.equivalence;

import static de.learnlib.ralib.example.stack.StackAutomatonExample.AUTOMATON;
import static de.learnlib.ralib.example.stack.StackAutomatonExample.I_POP;
import static de.learnlib.ralib.example.stack.StackAutomatonExample.I_PUSH;
import static de.learnlib.ralib.example.stack.StackAutomatonExample.T_INT;

import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.learnlib.query.DefaultQuery;
import de.learnlib.ralib.automata.InputTransition;
import de.learnlib.ralib.automata.MutableRegisterAutomaton;
import de.learnlib.ralib.automata.RALocation;
import de.learnlib.ralib.automata.RegisterAutomaton;
import de.learnlib.ralib.theory.Theory;
import de.learnlib.ralib.tools.theories.IntegerEqualityTheory;
import de.learnlib.ralib.words.PSymbolInstance;
import gov.nasa.jpf.constraints.api.Expression;
import gov.nasa.jpf.constraints.expressions.NumericBooleanExpression;
import gov.nasa.jpf.constraints.expressions.NumericComparator;
import gov.nasa.jpf.constraints.util.ExpressionUtil;
import net.automatalib.automaton.ra.Assignment;
import net.automatalib.data.Constants;
import net.automatalib.data.DataType;
import net.automatalib.data.SymbolicDataValue;
import net.automatalib.data.SymbolicDataValue.Parameter;
import net.automatalib.data.SymbolicDataValue.Register;
import net.automatalib.data.SymbolicDataValueGenerator.ParameterGenerator;
import net.automatalib.data.SymbolicDataValueGenerator.RegisterGenerator;
import net.automatalib.data.VarMapping;

public class RAEquivalenceTestTest {
	@Test
	public void findCounterExampleNoCETest() {

		Constants consts = new Constants();
		RegisterAutomaton sul = AUTOMATON;

		final Map<DataType, Theory> teachers = new LinkedHashMap<>();
		teachers.put(T_INT, new IntegerEqualityTheory(T_INT));

		RAEquivalenceTest test = new RAEquivalenceTest(sul, teachers, consts, true, I_PUSH, I_POP);

		DefaultQuery<PSymbolInstance, Boolean> ce = test.findCounterExample(sul, null);
		Assert.assertNull(ce);
	}

	@Test
	public void findCounterExampleHasCETest() {

		Constants consts = new Constants();
		RegisterAutomaton sul = AUTOMATON;

		final Map<DataType, Theory> teachers = new LinkedHashMap<>();
		teachers.put(T_INT, new IntegerEqualityTheory(T_INT));

		MutableRegisterAutomaton aut = new MutableRegisterAutomaton(consts);
		RegisterGenerator rgen = new RegisterGenerator();
		Register rVal = rgen.next(T_INT);
		ParameterGenerator pgen = new ParameterGenerator();
		Parameter pVal = pgen.next(T_INT);

		Expression<Boolean> eqGuard = new NumericBooleanExpression(rVal, NumericComparator.EQ, pVal);
		Expression<Boolean> neqGuard = new NumericBooleanExpression(rVal, NumericComparator.NE, pVal);
		Expression<Boolean> trueGuard = ExpressionUtil.TRUE;

		// assignments
		VarMapping<Register<?>, SymbolicDataValue<?>> storeMapping = new VarMapping<>();
		storeMapping.put(rVal, pVal);


        VarMapping<Register<?>, SymbolicDataValue<?>> copyMapping = new VarMapping<>();
        copyMapping.put(rVal, rVal);

		Assignment storeAssign = new Assignment(storeMapping);
		Assignment copyAssign   = new Assignment(copyMapping);
		Assignment noAssign = new Assignment(new VarMapping<>());

		RAEquivalenceTest test = new RAEquivalenceTest(sul, teachers, consts, true, I_PUSH, I_POP);

		RALocation l0 = aut.addInitialState(true);
		RALocation l1 = aut.addState(true);
		RALocation ls = aut.addState(false);

		aut.addTransition(l0, I_PUSH, new InputTransition(trueGuard, I_PUSH, l0, l1, storeAssign));
		aut.addTransition(l0, I_POP, new InputTransition(trueGuard, I_POP, l0, ls, noAssign));

		aut.addTransition(l1, I_PUSH, new InputTransition(trueGuard, I_PUSH, l1, l1, copyAssign));
		aut.addTransition(l1, I_POP, new InputTransition(eqGuard, I_POP, l1, l0, noAssign));
		aut.addTransition(l1, I_POP, new InputTransition(neqGuard, I_POP, l1, ls, noAssign));

		aut.addTransition(ls, I_PUSH, new InputTransition(trueGuard, I_PUSH, ls, ls, noAssign));
		aut.addTransition(ls, I_POP, new InputTransition(trueGuard, I_POP, ls, ls, noAssign));

		DefaultQuery<PSymbolInstance, Boolean> ce = test.findCounterExample(aut, null);
		Assert.assertEquals(sul.accepts(ce.getInput()), !aut.accepts(ce.getInput()) );
	}
}
