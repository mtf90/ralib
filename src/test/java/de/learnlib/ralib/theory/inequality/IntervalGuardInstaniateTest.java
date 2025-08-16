package de.learnlib.ralib.theory.inequality;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.learnlib.ralib.RaLibTestSuite;
import de.learnlib.ralib.data.SuffixValue;
import de.learnlib.ralib.theory.SDTGuard;
import de.learnlib.ralib.theory.Theory;
import de.learnlib.ralib.tools.theories.DoubleInequalityTheory;
import gov.nasa.jpf.constraints.api.Valuation;
import gov.nasa.jpf.constraints.types.BuiltinTypes;
import net.automatalib.data.Constants;
import net.automatalib.data.DataType;
import net.automatalib.data.DataValue;
import net.automatalib.data.SymbolicDataValue.Register;

public class IntervalGuardInstaniateTest extends RaLibTestSuite {

    @Test
    public void testInstantiateInterval() {

        final DataType<BigDecimal> D_TYPE = new DataType<>("double", BuiltinTypes.DECIMAL);

        final Map<DataType, Theory> teachers = new LinkedHashMap<>();
        DoubleInequalityTheory dit = new DoubleInequalityTheory(D_TYPE);
        teachers.put(D_TYPE, dit);

        SuffixValue<BigDecimal> s1 = new SuffixValue<>(D_TYPE, 1);
        Register<BigDecimal> r1 = new Register<>(D_TYPE, 1);
        Register<BigDecimal> r2 = new Register<>(D_TYPE, 2);

        DataValue<BigDecimal> dv0 = new DataValue<>(D_TYPE, BigDecimal.ZERO);
        DataValue<BigDecimal> dv1 = new DataValue<>(D_TYPE, BigDecimal.ONE);
        DataValue<BigDecimal> dv2 = new DataValue<>(D_TYPE, BigDecimal.valueOf(2));
        DataValue<BigDecimal> dv3 = new DataValue<>(D_TYPE, BigDecimal.valueOf(3));
        DataValue<BigDecimal> dv4 = new DataValue<>(D_TYPE, BigDecimal.valueOf(4));

        Valuation val = new Valuation();
        val.setValue(r1 , dv1.getValue());
        val.setValue(r2, dv2.getValue());

        Collection<DataValue<BigDecimal>> alreadyUsed = new ArrayList<>();
        alreadyUsed.add(dv1);
        alreadyUsed.add(dv2);

        Constants consts = new Constants();

        SDTGuard.IntervalGuard lg = SDTGuard.IntervalGuard.lessGuard(s1, dv1);
        SDTGuard.IntervalGuard leg = SDTGuard.IntervalGuard.lessOrEqualGuard(s1, dv1);
        SDTGuard.IntervalGuard rg = SDTGuard.IntervalGuard.greaterGuard(s1, dv1);
        SDTGuard.IntervalGuard reg = SDTGuard.IntervalGuard.greaterOrEqualGuard(s1, dv1);
        SDTGuard.IntervalGuard ig = new SDTGuard.IntervalGuard(s1, dv1, dv2);
        SDTGuard.IntervalGuard igc = new SDTGuard.IntervalGuard(s1, dv1, dv2, true, true);

        DataValue<BigDecimal> dvl = dit.instantiate(lg, val, consts, alreadyUsed);
        DataValue<BigDecimal> dvle = dit.instantiate(leg, val, consts, alreadyUsed);
        DataValue<BigDecimal> dvr = dit.instantiate(rg, val, consts, alreadyUsed);
        DataValue<BigDecimal> dvre = dit.instantiate(reg, val, consts, alreadyUsed);
        DataValue<BigDecimal> dvi = dit.instantiate(ig, val, consts, alreadyUsed);
        DataValue<BigDecimal> dvic = dit.instantiate(igc, val, consts, alreadyUsed);

        Assert.assertEquals(dvl.getValue().compareTo(dv1.getValue()), -1);
        Assert.assertNotEquals(dvle.getValue().compareTo(dv1.getValue()), 1);
        Assert.assertEquals(dvr.getValue().compareTo(dv1.getValue()), 1);
        Assert.assertNotEquals(dvre.getValue().compareTo(dv1.getValue()), -1);
        Assert.assertTrue(dvi.getValue().compareTo(dv1.getValue()) == 1 && dvi.getValue().compareTo(dv3.getValue()) == -1);
        Assert.assertFalse(dvic.getValue().compareTo(dv1.getValue()) == -1 && dvic.getValue().compareTo(dv3.getValue()) == 1);
    }
}
