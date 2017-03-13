package com.framgia.attendance.value.object;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PeriodTest {

    @Test
    public void testEquals() {
        final Period that = new Period(2010, Term.QT1);
        final Period other1 = new Period(2010, Term.QT1);
        final Period other2 = new Period(2010, Term.HF1);
        final Period other3 = new Period(2010, Term.QT3);
        final Period other4 = new Period(2010, Term.HF2);
        final Period other5 = new Period(2009, Term.QT1);
        final Period other6 = new Period(2011, Term.QT1);

        assertThat(that, is(other1));
        assertThat(that, is(not(other2)));
        assertThat(that, is(not(other3)));
        assertThat(that, is(not(other4)));
        assertThat(that, is(not(other5)));
        assertThat(that, is(not(other6)));
    }

    @Test
    public void testAfter() {
        final Period that = new Period(2010, Term.HF1);
        final Period other1 = new Period(2010, Term.QT1);
        final Period other2 = new Period(2010, Term.HF1);
        final Period other3 = new Period(2010, Term.QT3);
        final Period other4 = new Period(2010, Term.HF2);
        final Period other5 = new Period(2009, Term.QT1);
        final Period other6 = new Period(2011, Term.QT1);

        assertThat(that.after(other1), is(true));
        assertThat(that.after(other2), is(false));
        assertThat(that.after(other3), is(false));
        assertThat(that.after(other4), is(false));
        assertThat(that.after(other5), is(true));
        assertThat(that.after(other6), is(false));
    }

    @Test
    public void testBefore() {
        final Period that = new Period(2010, Term.HF1);
        final Period other1 = new Period(2010, Term.QT1);
        final Period other2 = new Period(2010, Term.HF1);
        final Period other3 = new Period(2010, Term.QT3);
        final Period other4 = new Period(2010, Term.HF2);
        final Period other5 = new Period(2009, Term.QT1);
        final Period other6 = new Period(2011, Term.QT1);

        assertThat(that.before(other1), is(false));
        assertThat(that.before(other2), is(false));
        assertThat(that.before(other3), is(true));
        assertThat(that.before(other4), is(true));
        assertThat(that.before(other5), is(false));
        assertThat(that.before(other6), is(true));
    }

    @Test
    public void testNext() {
        final Period that = new Period(2010, Term.QT1);

        assertThat(that.next(), is(new Period(2010, Term.HF1)));
        assertThat(that.next().next(), is(new Period(2010, Term.QT3)));
        assertThat(that.next().next().next(), is(new Period(2010, Term.HF2)));
        assertThat(that.next().next().next().next(), is(new Period(2011, Term.QT1)));
    }

    @Test
    public void testPrevious() {
        final Period that = new Period(2010, Term.QT1);

        assertThat(that.previous(), is(new Period(2009, Term.HF2)));
        assertThat(that.previous().previous(), is(new Period(2009, Term.QT3)));
        assertThat(that.previous().previous().previous(), is(new Period(2009, Term.HF1)));
        assertThat(that.previous().previous().previous().previous(), is(new Period(2009, Term.QT1)));
    }
}
