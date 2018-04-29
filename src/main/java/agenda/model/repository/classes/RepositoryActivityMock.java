package agenda.model.repository.classes;


import agenda.model.base.Activity;
import agenda.model.base.Contact;
import agenda.model.repository.interfaces.RepositoryActivity;
import agenda.model.repository.interfaces.RepositoryContact;

import javax.naming.NameNotFoundException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RepositoryActivityMock implements RepositoryActivity {

    private List<Activity> activities;
    private RepositoryContact contactRep = new RepositoryContactFile();

    public RepositoryActivityMock() throws Exception {
        activities = new LinkedList<Activity>();

    }

    @Override
    public List<Activity> getActivities() {
        return new LinkedList<Activity>(activities);
    }

    @Override
    public boolean addActivity(Activity activity) throws NameNotFoundException {
        int i = 0;
        boolean conflicts = false;

        while (i < activities.size()) {
            if (activities.get(i).getStart().compareTo(activity.getDuration()) < 0 &&
                    activity.getStart().compareTo(activities.get(i).getDuration()) < 0)
                conflicts = true;
            i++;
        }

        if (activity.getContacts().size() > 0) {

            for (Contact con : activity.getContacts()) {
                if (contactRep.contactNameExists(con.getName()))
                    conflicts = false;
                else {
                    throw new NameNotFoundException(con.getName() + " - not found!");
                }
            }
        }

        if (!conflicts) {
            activities.add(activity);
            return true;
        }

        return false;
    }


    @Override
    public boolean removeActivity(Activity activity) {
        int index = activities.indexOf(activity);
        if (index < 0) return false;
        activities.remove(index);
        return true;
    }

    @Override
    public boolean saveActivities() {
        return true;
    }

    @Override
    public int count() {
        return activities.size();
    }

    @Override
    public List<Activity> activitiesByName(String name) {
        List<Activity> result = new LinkedList<Activity>();
        for (Activity a : activities)
            if (a.getName().equals(name)) result.add(a);
        return result;
    }

    @Override
    public List<Activity> activitiesOnDate(String name, Date d) {
        List<Activity> result = new LinkedList<Activity>();
        for (Activity a : activities)
            if (a.getName().equals(name))
                if (a.getStart().compareTo(d) <= 0 && d.compareTo(a.getDuration()) <= 0) result.add(a);
        return result;
    }

    @Override
    public boolean removeLastAddedActivity() {
        return false;
    }

}
