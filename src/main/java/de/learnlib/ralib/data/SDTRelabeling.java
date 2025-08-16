package de.learnlib.ralib.data;

import net.automatalib.data.DataValue;
import net.automatalib.data.GuardElement;
import net.automatalib.data.Mapping;

public class SDTRelabeling extends Mapping<GuardElement, GuardElement> {

    public static SDTRelabeling fromBijection(Bijection<DataValue> in) {
        SDTRelabeling ret = new SDTRelabeling();
        ret.putAll(in);
        return ret;
    }

    public static SDTRelabeling fromMapping(Mapping<GuardElement, GuardElement> mapping) {
    	SDTRelabeling ret = new SDTRelabeling();
    	ret.putAll(mapping);
    	return ret;
    }
}
