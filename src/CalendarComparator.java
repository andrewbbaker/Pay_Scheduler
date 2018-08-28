import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by andre on 3/26/2017.
 */
public class CalendarComparator{
    public static int compare(Calendar o1, Calendar o2) {
        if (o1.get(Calendar.YEAR) != o2.get(Calendar.YEAR)) {
            return o1.get(Calendar.YEAR) - o2.get(Calendar.YEAR);
        }
        return o1.get(Calendar.DAY_OF_YEAR) - o2.get(Calendar.DAY_OF_YEAR);
    }
}
