package de.learnlib.ralib.theory;

import java.util.ArrayList;
import java.util.List;

import de.learnlib.ralib.data.SuffixValue;
import de.learnlib.ralib.data.WordValuation;
import de.learnlib.ralib.data.util.SuffixValueGenerator;
import de.learnlib.ralib.smt.SMTUtil;
import de.learnlib.ralib.words.PSymbolInstance;
import de.learnlib.ralib.words.ParameterizedSymbol;
import gov.nasa.jpf.constraints.api.Expression;
import net.automatalib.data.DataType;
import net.automatalib.data.DataValue;
import net.automatalib.data.Mapping;
import net.automatalib.data.SymbolicDataValue;
import net.automatalib.data.SymbolicDataValue.Parameter;
import net.automatalib.data.SymbolicDataValueGenerator.ParameterGenerator;
import net.automatalib.data.Valuation;
import net.automatalib.word.Word;

public class EquivalenceClassFilter {

	private final List<DataValue> equivClasses;
	private final boolean useOptimization;

	public EquivalenceClassFilter(List<DataValue> equivClasses, boolean useOptimization) {
		this.equivClasses = equivClasses;
		this.useOptimization = useOptimization;
	}

	public List<DataValue> toList(SuffixValueRestriction restr,
			Word<PSymbolInstance> prefix, Word<ParameterizedSymbol> suffix, WordValuation valuation) {

		if (!useOptimization) {
			return equivClasses;
		}

		List<DataValue> filtered = new ArrayList<>();

		ParameterGenerator pgen = new ParameterGenerator();
		SuffixValueGenerator svgen = new SuffixValueGenerator();
		Mapping<SymbolicDataValue<?>, DataValue<?>> mapping = new Mapping<>();
		for (PSymbolInstance psi : prefix) {
			DataType[] dts = psi.getBaseSymbol().getPtypes();
			DataValue[] dvs = psi.getParameterValues();
			for (int i = 0; i < dvs.length; i++) {
				Parameter p = pgen.next(dts[i]);
				if (restr.parameter.getDataType().equals(dts[i])) {
					mapping.put(p, dvs[i]);
				}
			}
		}
		for (ParameterizedSymbol ps : suffix) {
			DataType[] dts = ps.getPtypes();
			for (int i = 0; i < dts.length; i++) {
				SuffixValue sv = svgen.next(dts[i]);
				DataValue val = valuation.get(sv.getId());
				if (val != null && val.getDataType().equals(restr.parameter.getDataType())) {
					mapping.put(sv, val);
				}
			}
		}

		Expression<Boolean> expr = restr.toGuardExpression(mapping.keySet());
		for (DataValue ec : equivClasses) {
			Valuation<SymbolicDataValue<?>, DataValue<?>> ecMapping = new Valuation<>();
			ecMapping.putAll(mapping);
			ecMapping.put(restr.getParameter(), ec);
			//System.out.println(" -- " + expr + "  - " + ecMapping);
			if (expr.evaluateSMT(SMTUtil.compose(ecMapping))) {
				filtered.add(ec);
			}
		}
		return filtered;
	}
}
