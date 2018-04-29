package agenda.model.base;

import agenda.model.repository.interfaces.RepositoryContact;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Activity {
    private String name;
    private Date start;
    private Date duration;
    private List<Contact> contacts;
    private String description;
    private String place;
    private String myName;

    public Activity(String name, Date start, Date end, List<Contact> contacts,
                    String description) {
        this.name = name;
        this.description = description;
        if (contacts == null)
            this.contacts = new LinkedList<Contact>();
        else
            this.contacts = new LinkedList<Contact>(contacts);

        this.start = new Date();
        this.start.setTime(start.getTime());
        this.duration = new Date();
        this.duration.setTime(end.getTime());
    }

    public Activity(String name, Date start, Date duration, List<Contact> contacts, String description, String myName, String place) {
        this.name = name;
        this.start = start;
        this.duration = duration;
        this.contacts = contacts;
        this.description = description;
        this.myName = myName;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Activity))
            return false;
        Activity act = (Activity) obj;
        if (act.description.equals(description) && start.equals(act.start)
                && duration.equals(act.duration) && name.equals(act.name))
            return true;
        return false;
    }

    public boolean intersect(Activity act) {
        if (start.compareTo(act.duration) < 0
                && act.start.compareTo(duration) < 0)
            return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("#");
        sb.append(start.getTime());
        sb.append("#");
        sb.append(duration.getTime());
        sb.append("#");
        sb.append(description);
        sb.append("#");
        sb.append(place);
        sb.append("#");
        sb.append(myName);
        if (contacts.size() == 0)
            sb.append("#");
        else
            for (Contact c : contacts) {
                sb.append("#");
                sb.append(c.getName());
            }
        return sb.toString();
    }

    public static Activity fromString(String line, RepositoryContact repcon) {
        try {
            String[] str = line.split("#");
            String name = str[0];
            Date start = new Date(Long.parseLong(str[1]));
            Date duration = new Date(Long.parseLong(str[2]));
            String description = str[3];
            String location = str[4];
            String me = str[5];
            List<Contact> conts = new LinkedList<Contact>();
            for (int i = 6; i < str.length; i++) {
                conts.add(repcon.getByName(str[i]));
            }
            return new Activity(name, start, duration, conts, description,me,location);
        } catch (Exception e) {
            return null;
        }
    }
}
