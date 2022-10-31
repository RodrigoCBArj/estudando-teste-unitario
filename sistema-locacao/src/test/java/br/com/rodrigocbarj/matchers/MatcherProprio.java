package br.com.rodrigocbarj.matchers;

import java.util.Calendar;

public class MatcherProprio {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DataDiferencialDiasMatcher hojeComAdicaoDias(Integer qtdDias) {
        return new DataDiferencialDiasMatcher(qtdDias);
    }

    public static DataDiferencialDiasMatcher ehHoje() {
        return new DataDiferencialDiasMatcher(0);
    }
}
