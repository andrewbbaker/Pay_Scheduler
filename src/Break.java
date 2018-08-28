import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by andre on 3/19/2017.
 */
public class Break {
    Calendar startDate;
    Calendar endDate;

    public Break(Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Break(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        startDate = new GregorianCalendar();
        startDate.set(Calendar.YEAR, startYear);
        startDate.set(Calendar.MONTH, startMonth);
        startDate.set(Calendar.DAY_OF_MONTH, startDay);

        endDate = new GregorianCalendar();
        endDate.set(Calendar.YEAR, endYear);
        endDate.set(Calendar.MONTH, endMonth);
        endDate.set(Calendar.DAY_OF_MONTH, endDay);
    }

    /*****************************************************************
     * Getter and setter methods
     *****************************************************************/

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate.set(Calendar.DATE, startDate);
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate.set(Calendar.DATE, endDate);
    }

    public int getNumberOfDays() {
        return endDate.get(Calendar.DATE) - startDate.get(Calendar.DATE);
    }

    public void splitWeek(Week week) {
        if (CalendarComparator.compare(week.getStartDate(), getStartDate()) < 0) {
            if (CalendarComparator.compare(week.getEndDate(), getEndDate()) > 0) {
                Week newWeek = null;
                if (week.getBreakApplied() != null) {
                    newWeek = new Week((GregorianCalendar) getEndDate(), week.getBreakApplied());
                } else {
                    newWeek = new Week((GregorianCalendar) getEndDate());
                }
                newWeek.getStartDate().add(Calendar.DATE, 1);
                week.getPrimaryEndDate().set(Calendar.DAY_OF_YEAR, getStartDate().get(Calendar.DAY_OF_YEAR));
                week.getPrimaryEndDate().set(Calendar.YEAR, getStartDate().get(Calendar.YEAR));
                if (week.getSplitWeek()!=null) {
                    newWeek.setSplitWeek(week.getSplitWeek());
                    newWeek.setBreakApplied(week.getBreakApplied());
                }
                week.setSplitWeek(newWeek);
                if (week.getBreakApplied() != null) {
                    newWeek.setBreakApplied(week.getBreakApplied());
                }
                week.setBreakApplied(this);

            }
        }
    }

    public void applyBreak(Week week) {
        if (CalendarComparator.compare(week.getStartDate(), getStartDate()) >= 0) {
            if (CalendarComparator.compare(week.getStartDate(), getEndDate()) <= 0) {
                week.getStartDate().set(Calendar.DAY_OF_YEAR, getEndDate().get(Calendar.DAY_OF_YEAR));
                week.getStartDate().set(Calendar.YEAR, getEndDate().get(Calendar.YEAR));
                week.getStartDate().add(Calendar.DATE, 1);
            }
        }
        if (CalendarComparator.compare(week.getEndDate(), getStartDate()) >= 0) {
            if (CalendarComparator.compare(week.getEndDate(), getEndDate()) <= 0) {
                week.getEndDate().set(Calendar.DAY_OF_YEAR, getStartDate().get(Calendar.DAY_OF_YEAR));
                week.getEndDate().set(Calendar.YEAR, getStartDate().get(Calendar.YEAR));
            }
        }
        if (week.getSplitWeek()!=null) {
            applyBreak(week.getSplitWeek());
        }
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        return "Starts " + dateFormat.format(startDate.getTime()) + ", ends " + dateFormat.format(endDate.getTime());
    }
}
