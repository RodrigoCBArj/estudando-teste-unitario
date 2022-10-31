package br.com.rodrigocbarj.matchers;

import java.util.Calendar;

public class MatcherProprio {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }
}
