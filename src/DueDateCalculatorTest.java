import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class DueDateCalculatorTest {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    private Date parseDateTime(String dateTimeStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return dateFormat.parse(dateTimeStr);
    }

    @Test
    void testCalculateDueDateWithinSameWorkingDay() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date submitDateTime = parseDateTime("2023-09-04 10:00");
        int turnaroundHours = 3;

        Date dueDate = DueDateCalculator.calculateDueDate(submitDateTime, turnaroundHours);

        assertEquals("2023-09-04 13:00", dateFormat.format(dueDate));
    }

    @Test
    void testCalculateDueDateWithNegativeTurnaroundHours() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date submitDateTime = null;
        try {
            submitDateTime = parseDateTime("2023-09-04 10:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int turnaroundHours = -5;

        Date finalSubmitDateTime = submitDateTime;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DueDateCalculator.calculateDueDate(finalSubmitDateTime, turnaroundHours);
        });

        assertEquals("Turnaround time can't be negative.", exception.getMessage());
    }

    @Test
    void testCalculateDueDateWithZeroTurnaroundHours() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date submitDateTime = parseDateTime("2023-09-04 10:00");
        int turnaroundHours = 0;

        Date dueDate = DueDateCalculator.calculateDueDate(submitDateTime, turnaroundHours);

        assertEquals("2023-09-04 10:00", dateFormat.format(dueDate));
    }

    @Test
    void testCalculateDueDateWithNegativeSubmitTime() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date submitDateTime = parseDateTime("2023-09-04 07:00");
        int turnaroundHours = 5;

        Date dueDate = DueDateCalculator.calculateDueDate(submitDateTime, turnaroundHours);
    }
}
