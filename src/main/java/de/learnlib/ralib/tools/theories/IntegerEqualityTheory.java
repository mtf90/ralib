/*
 * Copyright (C) 2014-2015 The LearnLib Contributors
 * This file is part of LearnLib, http://www.learnlib.de/.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.learnlib.ralib.tools.theories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.learnlib.ralib.oracles.io.IOOracle;
import de.learnlib.ralib.theory.equality.EqualityTheory;
import de.learnlib.ralib.tools.classanalyzer.TypedTheory;
import net.automatalib.data.DataType;
import net.automatalib.data.DataValue;

/**
 *
 * @author falk
 */
public class IntegerEqualityTheory  extends EqualityTheory implements TypedTheory<BigDecimal> {


    private DataType<BigDecimal> type = null;

    public IntegerEqualityTheory() {
    }

    public IntegerEqualityTheory(DataType<BigDecimal> t) {
        this.type = t;
    }

    @Override
    public DataValue<BigDecimal> getFreshValue(List<DataValue<BigDecimal>> vals) {
        BigDecimal dv = new BigDecimal("-1");
        for (DataValue<BigDecimal> d : vals) {
            dv = dv.max(d.getValue());
        }

        return new DataValue(type, BigDecimal.ONE.add(dv));
    }

    @Override
    public void setType(DataType<BigDecimal> type) {
        this.type = type;
    }

    @Override
    public void setUseSuffixOpt(boolean useit) {
        this.useNonFreeOptimization = useit;
    }

    @Override
    public void setCheckForFreshOutputs(boolean doit, IOOracle oracle) {
        super.setFreshValues(doit, oracle);
    }

    @Override
    public Collection<DataValue<BigDecimal>> getAllNextValues(List<DataValue<BigDecimal>> vals) {
        // TODO: add constants ...
        ArrayList<DataValue<BigDecimal>> ret = new ArrayList<>(vals);
        ret.add(getFreshValue(vals));
        return ret;
    }

}
