package br.com.rodrigocbarj.matchers;

import br.com.rodrigocbarj.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataDiferencialDiasMatcher extends TypeSafeMatcher<Date> {

    private Integer qtdDias;

    public DataDiferencialDiasMatcher(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(qtdDias));
    }

    @Override
    public void describeTo(Description description) {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_WEEK, qtdDias);
        String dataExtenso = data.getDisplayName(
                Calendar.DAY_OF_WEEK,
                Calendar.LONG,
                new Locale("pt", "BR")
        );
        description.appendText(dataExtenso);
    }
}
