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
package de.learnlib.ralib.tools.classanalyzer;

import de.learnlib.ralib.oracles.io.IOOracle;
import de.learnlib.ralib.theory.Theory;
import net.automatalib.data.DataType;

/**
 *
 * @author falk
 */
public interface TypedTheory<T> extends Theory<T> {

    void setType(DataType<T> type);

    void setUseSuffixOpt(boolean useit);

    void setCheckForFreshOutputs(boolean doit, IOOracle oracle);
}
