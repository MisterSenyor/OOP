import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;


public class Main {

    /*DO NOT CHANGE!!!*/
    public static void main(String[] args) {
        Child[] children = {
                new Child("Edan", "04-02-2018", "Soccer"),
                new Child("Esther", "07-10-2018", "Frisbee"),
                new Child("Ori", "13-12-2018", "Acting"),
                new Child("Noa", "01-01-2018", "Math"),
                new Child("Lior", "07-07-2018", "Soccer"),
                new Child("Erel", "08-08-2018", "Computer Games"),
                new Child("Eldar", "09-09-2018", "Baseball"),
                new Child("Omri", "10-10-2018", "Painting"),
                new Child("Rachel", "11-11-2018", "Lego"),
                new Child("Dan", "12-12-2018", "Dancing"),
                new Child("Roy", "02-02-2018", "Reading"),
        };
        KindergartenTeacher teacher = new KindergartenTeacher(children.length);

        Kindergarten kindergarten=new Kindergarten(children,teacher);

        List<LocalDate> listOfDates = getListOfDates();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for(var date: listOfDates){
            System.out.println(date);
            teacher.isItTimeToCelebrate(formatter.format(date));
        }
    }

    private static List<LocalDate> getListOfDates() {
        // Our code is using some built-in java library, can you figure out what it does even without reading the documentation?
        // Food for thought: What kinds of decisions go into naming variables and methods so that they are easy to understand
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.with(firstDayOfYear());
        LocalDate endDate = now.with(lastDayOfYear());
        List<LocalDate> listOfDates = startDate.datesUntil(endDate).collect(Collectors.toList());
        return listOfDates;
    }
}


/*Implement all your classes below this line*/


class Child {
    private String name, date, activity;
    public Child(String name, String date, String activity) {
        this.name = name;
        this.date = date;
        this.activity = activity;
    }
    public String getName() {
        return this.name;
    }
    public String getDate() {
        return this.date;
    }
    public String getActivity() {
        return this.activity;
    }
}

class BirthdayEvent {
    private String name, date, activity;

    public BirthdayEvent(Child c) {
        this.name = c.getName();
        this.date = c.getDate();
        this.activity = c.getActivity();
    }
    public String getName() {
        return this.name;
    }
    public String getDate() {
        return this.date;
    }
    public String getActivity() {
        return this.activity;
    }
}

class Calendar {
    private int maxSize, idx = 0;
    private BirthdayEvent[] events;

    public Calendar(int maxSize) {
        this.maxSize = maxSize;
        this.events = new BirthdayEvent[maxSize];
    }
    public void add(BirthdayEvent event) {
        if (idx < maxSize) {
            this.events[idx++] = event;
        }
    }
    public BirthdayEvent getBirthday(String date) {
        for (int i = 0; i <= idx && i < maxSize; i++) {
            System.out.println(events[i].getDate() + " --- " + date);
            if (events[i].getDate().equals(date)) {
                return events[i];
            }
        }
        return null;
    }
}

class KindergartenTeacher {
    private int maxChildren;
    private Calendar calendar;
    public KindergartenTeacher(int maxChildren) {
        this.maxChildren = maxChildren;
        calendar = new Calendar(maxChildren);
    }
    public void add(BirthdayEvent event) {
        this.calendar.add(event);
    }
    public void isItTimeToCelebrate(String date) {
        BirthdayEvent e = this.calendar.getBirthday(date);
        if (e != null) {
            System.out.println("<For " + e.getName() + "'s Birthday party I need to prepare his favorite activity: " + e.getActivity());
        }
    }
}

class Kindergarten {
    private KindergartenTeacher teacher;
    private Child[] children;
    public Kindergarten(Child[] children, KindergartenTeacher teacher) {
        this.teacher = teacher;
        this.children = children;
        for (int i = 0; i < children.length; i++) {
            teacher.add(new BirthdayEvent(children[i]));
        }
    }
}