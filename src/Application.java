import java.text.*;
import java.util.*;

/**
 * Created by andre on 3/20/2017.
 */
public class Application {
    public static final Format dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
    public static final Format moneyFormat = new DecimalFormat("0.00");

    public static void main(String[] args) throws Exception {
        new EasyAccountantFrame().eula();
    }

    public static String getInfo(Calendar start, Calendar end, int contractValue, List<Break> breaks, Calendar endLowPay, int lowPayRate) {
        setFirstWorkDay(start);
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.DAY_OF_YEAR, start.get(Calendar.DAY_OF_YEAR));
        currentDate.set(Calendar.YEAR, start.get(Calendar.YEAR));
        List<Week> weeks = new ArrayList<>();

        while (CalendarComparator.compare(currentDate, end) < 0) {
            Week week = new Week(currentDate);
            applyBreaks(breaks, weeks, week);
            currentDate.add(Calendar.DATE, 9-currentDate.get(Calendar.DAY_OF_WEEK));
        }

        if (weeks.size() > 0 && CalendarComparator.compare(weeks.get(weeks.size()-1).getEndDate(), end) < 0) {
            weeks.get(weeks.size()-1).getEndDate().add(Calendar.DATE, end.get(Calendar.DAY_OF_WEEK) - weeks.get(weeks.size()-1).getEndDate().get(Calendar.DAY_OF_WEEK) + 1);
        }

        int totalDays = 0;
        for (Week week : weeks) {
            totalDays += week.getNumberOfDays();
        }

        int lowPayDays = 0;
        for (Week week : weeks) {
            if (CalendarComparator.compare(week.getStartDate(), endLowPay) <=0) {
                if (CalendarComparator.compare(week.getEndDate(), endLowPay) <=0) {
                    lowPayDays += week.getNumberOfDays();
                } else {
                    lowPayDays += week.getDaysBefore(endLowPay);
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("<html><p>Start date is " + dateFormat.format(start.getTime()) + "</p>");
        stringBuilder.append("<p>end date is " + dateFormat.format(end.getTime()) + "</p><br>");

        stringBuilder.append("<p>Breaks:</p>");
        for (Break aBreak : breaks) {
            stringBuilder.append("<p>" + aBreak + "</p>");
        }
        System.out.println();

        int dayValue = 0;
        if (totalDays > 0) {
            dayValue = contractValue/totalDays;
        }
        int leftOver = contractValue - dayValue*totalDays;

        stringBuilder.append("<p>Total contract value: " + moneyFormat.format(((double)contractValue)/100) + "</p>");
        stringBuilder.append("<p>5 day week value: " + moneyFormat.format(((double)dayValue)/100*5) + "</p>");
        stringBuilder.append("<p>4 day week value: " + moneyFormat.format(((double)dayValue)/100*4) + "</p>");
        stringBuilder.append("<p>3 day week value: " + moneyFormat.format(((double)dayValue)/100*3) + "</p>");
        stringBuilder.append("<p>2 day week value: " + moneyFormat.format(((double)dayValue)/100*2) + "</p>");
        stringBuilder.append("<p>1 day week value: " + moneyFormat.format(((double)dayValue)/100*1) + "</p>");
        stringBuilder.append("<p>Left over money: " + moneyFormat.format(((double)leftOver)/100) + "</p>");

        int lumpSumLowPay = dayValue*lowPayDays - lowPayRate*lowPayDays;

        stringBuilder.append("<br><p>Days of low pay: " + lowPayDays + "</p>");
        stringBuilder.append("<p>Lump sum for low pay: " + moneyFormat.format(((double)lumpSumLowPay)/100) + "</p>");

        return stringBuilder.toString();
    }

    public static void applyBreaks(List<Break> breaks, List<Week> weeks, Week week) {
        Iterator<Break> breakIterator = breaks.iterator();
        while (breakIterator.hasNext()) {
            Break b = breakIterator.next();
            b.splitWeek(week);
            b.applyBreak(week);
        }
        weeks.add(week);

    }

    public static void setFirstWorkDay(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        }
    }

    public static int getNextMonday(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return calendar.get(Calendar.DATE) + 7;
        }

        return calendar.get(Calendar.DATE) + 8 - calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getLines() {
        return 14;
    }
}
