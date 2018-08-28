import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by andre on 3/19/2017.
 */
public class Week implements Comparable{
    Calendar startDate;
    Calendar endDate;
    List<Week> weeks;
    Week splitWeek = null;
    Break breakApplied = null;

    public Week (Calendar startDate) {
        this.startDate = new GregorianCalendar();
        this.startDate.set(Calendar.DAY_OF_YEAR, startDate.get(Calendar.DAY_OF_YEAR));
        this.startDate.set(Calendar.YEAR, startDate.get(Calendar.YEAR));

        if (this.startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            this.startDate.add(Calendar.DATE, 1);
        }

        if (this.startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            this.startDate.add(Calendar.DATE, 2);
        }

        endDate = new GregorianCalendar();
        endDate.set(Calendar.DAY_OF_YEAR, this.startDate.get(Calendar.DAY_OF_YEAR));
        endDate.set(Calendar.YEAR, this.startDate.get(Calendar.YEAR));
        endDate.add(Calendar.DATE, Calendar.SATURDAY - this.startDate.get(Calendar.DAY_OF_WEEK));
    }

    public Week (GregorianCalendar startDate, Break breakApplied) {
        this(startDate);
        if (CalendarComparator.compare(getEndDate(), breakApplied.getEndDate()) >= 0) {
            this.getEndDate().set(Calendar.DAY_OF_YEAR, breakApplied.getStartDate().get(Calendar.DAY_OF_YEAR));
            this.getEndDate().set(Calendar.YEAR, breakApplied.getStartDate().get(Calendar.YEAR));
        }
    }

    public Week (int startDate) {
        this.startDate = new GregorianCalendar();
        this.startDate.set(Calendar.DATE, startDate);

        if (this.startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            this.startDate.add(Calendar.DATE, 1);
        }

        if (this.startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            this.startDate.add(Calendar.DATE, 2);
        }

        endDate = new GregorianCalendar();
        endDate.set(Calendar.DATE, this.startDate.get(Calendar.DATE) + Calendar.SATURDAY - this.startDate.get(Calendar.DAY_OF_WEEK));
    }

    public Week(int startDay, int startMonth, int startYear) {
        startDate = new GregorianCalendar();
        startDate.set(Calendar.DAY_OF_MONTH, startDay);
        startDate.set(Calendar.MONTH, startMonth);
        startDate.set(Calendar.YEAR, startYear);

        endDate = new GregorianCalendar();
        endDate.set(Calendar.DATE, this.startDate.get(Calendar.DATE) + Calendar.SATURDAY - this.startDate.get(Calendar.DAY_OF_WEEK));
    }

    public static void main(String args[]) {
        Week week = new Week(new GregorianCalendar());
        System.out.println(week);
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

    public Calendar getPrimaryEndDate() {
        return endDate;
    }

    public Calendar getEndDate() {
        if (splitWeek != null) {
            return splitWeek.getEndDate();
        }
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate.set(Calendar.DATE, endDate);
    }

    public Week getSplitWeek() {
        return splitWeek;
    }

    public void setSplitWeek(Week splitWeek) {
        this.splitWeek = splitWeek;
    }

    public Break getBreakApplied() {
        return breakApplied;
    }

    public void setBreakApplied(Break breakApplied) {
        this.breakApplied = breakApplied;
    }

    public int getNumberOfDays() {
        int splitWeekDays = 0;
        if (splitWeek != null) {
            splitWeekDays = splitWeek.getNumberOfDays();
        }
        if (CalendarComparator.compare(startDate, endDate) >= 0) {
            return splitWeekDays;
        }

        return endDate.get(Calendar.DAY_OF_WEEK) - startDate.get(Calendar.DAY_OF_WEEK) + splitWeekDays;
    }

    public int getDaysBefore(Calendar calendar) {
        if (CalendarComparator.compare(endDate, calendar) < 0) {
            return endDate.get(Calendar.DAY_OF_WEEK) - startDate.get(Calendar.DAY_OF_WEEK);
        } else if (splitWeek!=null) {
            return endDate.get(Calendar.DAY_OF_WEEK) - startDate.get(Calendar.DAY_OF_WEEK) + splitWeek.getDaysBefore(calendar);
        }
        else return calendar.get(Calendar.DAY_OF_WEEK) - startDate.get(Calendar.DAY_OF_WEEK) + 1;
    }

    public boolean isFullWeek() {
        return getEndDate().get(Calendar.DAY_OF_WEEK) - getStartDate().get(Calendar.DAY_OF_WEEK) == 5;
    }

    @Override
    public String toString() {
        String splitWeekString = "";
        if (splitWeek != null) {
            if (splitWeek.getNumberOfDays()>0) {
                splitWeekString = ", ";
            }
            splitWeekString = splitWeekString + splitWeek.toString();
        }

        if (CalendarComparator.compare(startDate, endDate) >= 0) {
            return splitWeekString;
        }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        Calendar tempEnd = (Calendar)endDate.clone();
        tempEnd.add(Calendar.DATE, -1);

        return "Starts " + dateFormat.format(startDate.getTime()) + ", ends " + dateFormat.format(tempEnd.getTime()) + splitWeekString;
    }

    @Override
    public int compareTo(Object o) {
        if (getStartDate().get(Calendar.YEAR) != ((Week) o).getStartDate().get(Calendar.YEAR)) {
            return getStartDate().get(Calendar.YEAR) - ((Week) o).getStartDate().get(Calendar.YEAR);
        }
        if (getStartDate().get(Calendar.DAY_OF_YEAR) != ((Week) o).getStartDate().get(Calendar.DAY_OF_YEAR)) {
            return getStartDate().get(Calendar.DAY_OF_YEAR) - ((Week) o).getStartDate().get(Calendar.DAY_OF_YEAR);
        }
        return getNumberOfDays() - ((Week)o).getNumberOfDays();
    }
}
