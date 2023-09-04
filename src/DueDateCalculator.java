import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class DueDateCalculator {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final int WORKING_HOURS_START = 9;
    private static final int WORKING_HOURS_END = 17;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String submitDateTimeStr = promptForSubmitDateTime(scanner);
            int turnaroundHours = promptForTurnaroundHours(scanner);

            Date submitDateTime = parseDateTime(submitDateTimeStr);

            if (turnaroundHours >= 0) {
                Date dueDate = calculateDueDate(submitDateTime, turnaroundHours);
                displayDueDate(dueDate);
            } else {
                System.out.println("Turnaround time can't be negative.");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date/time format. Please use 'YYYY-MM-DD HH:mm'.");
        }
    }

    private static String promptForSubmitDateTime(Scanner scanner) {
        System.out.print("Enter submit date and time (YYYY-MM-DD HH:mm): ");
        return scanner.nextLine();
    }

    private static int promptForTurnaroundHours(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter the turnaround time (in working hours): ");
                int hours = Integer.parseInt(scanner.nextLine());
                if (hours >= 0) {
                    return hours;
                } else {
                    System.out.println("Turnaround time can't be negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer for turnaround hours.");
            }
        }
    }

    public static Date calculateDueDate(Date submitDate, int turnaroundHours) {
        if (turnaroundHours < 0) {
            throw new IllegalArgumentException("Turnaround time can't be negative.");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(submitDate);

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (!isWorkingHour(currentHour)) {
            moveToNextWorkingDay(calendar);
            calendar.set(Calendar.HOUR_OF_DAY, WORKING_HOURS_START);
        }

        while (turnaroundHours > 0) {
            int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if (isWorkingHour(currentHourOfDay)) {
                int hoursLeftToday = WORKING_HOURS_END - currentHourOfDay;
                if (turnaroundHours <= hoursLeftToday) {
                    calendar.add(Calendar.HOUR_OF_DAY, turnaroundHours);
                    turnaroundHours = 0;
                } else {
                    calendar.add(Calendar.HOUR_OF_DAY, hoursLeftToday);
                    moveToNextWorkingDay(calendar);
                    calendar.set(Calendar.HOUR_OF_DAY, WORKING_HOURS_START);
                    turnaroundHours -= hoursLeftToday;
                }
            } else {
                moveToNextWorkingDay(calendar);
            }
        }

        return calendar.getTime();
    }



    static boolean isWorkingHour(int hour) {
        return hour >= WORKING_HOURS_START && hour < WORKING_HOURS_END;
    }

    private static void moveToNextWorkingDay(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, WORKING_HOURS_START);
    }

    private static Date parseDateTime(String dateTimeStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        return dateFormat.parse(dateTimeStr);
    }

    private static void displayDueDate(Date dueDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        System.out.println("Due Date: " + dateFormat.format(dueDate));
    }
}
