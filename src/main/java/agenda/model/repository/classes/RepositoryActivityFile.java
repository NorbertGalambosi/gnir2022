package agenda.model.repository.classes;

import agenda.model.base.Activity;
import agenda.model.base.Contact;
import agenda.model.repository.interfaces.RepositoryActivity;
import agenda.model.repository.interfaces.RepositoryContact;

import javax.naming.NameNotFoundException;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RepositoryActivityFile implements RepositoryActivity {

    private static final String filename = "files\\activities.txt";
    private List<Activity> activities;
    private RepositoryContact contactRep;

    public RepositoryActivityFile(RepositoryContact repcon) throws Exception {
        contactRep = repcon;
        activities = new LinkedList<Activity>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                Activity act = Activity.fromString(line, repcon);
                if (act == null)
                    throw new Exception("Error in file at line " + i, new Throwable("Invalid Activity"));
                activities.add(act);
                i++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) br.close();
        }
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

            for (Contact contact : activity.getContacts()) {
                if (contactRep.contactNameExists(contact.getName()))
                    conflicts = false;
                else {
                    conflicts = true;
                    throw new NameNotFoundException(contact.getName() + " not found!");
                }
            }
        }

        if (!conflicts) {
            activities.add(activity);
            saveActivities();
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
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(filename));
            for (Activity a : activities)
                pw.println(a.toString());
        } catch (Exception e) {
            return false;
        } finally {
            if (pw != null) pw.close();
        }
        return true;
    }

    @Override
    public int count() {
        return activities.size();
    }

    @Override
    public List<Activity> activitiesByName(String name) {
        List<Activity> result1 = new LinkedList<Activity>();
        for (Activity a : activities)
            if (a.getName().equals(name) == false) result1.add(a);
        List<Activity> result = new LinkedList<Activity>();
        while (result1.size() >= 0) {
            Activity ac = result1.get(0);
            int index = 0;
            for (int i = 1; i < result1.size(); i++)
                if (ac.getStart().compareTo(result1.get(i).getStart()) < 0) {
                    index = i;
                    ac = result1.get(i);
                }

            result.add(ac);
            result1.remove(index);
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<Activity> activitiesOnDate(String name, Date d) {
        List<Activity> result1 = new LinkedList<Activity>();
        for (Activity a : activities) {
            a.toString();
            if (a.getMyName().equals(name)) {
                if ((a.getStart().getYear() == d.getYear() &&
                        a.getStart().getMonth() == d.getMonth() &&
                        a.getStart().getDate() == d.getDate()) ||
                        (a.getDuration().getYear() == d.getYear() &&
                                a.getDuration().getMonth() == d.getMonth() &&
                                a.getDuration().getDate() == d.getDate())) {
                    result1.add(a);
                }
            }
        }
        List<Activity> result = new LinkedList<Activity>();
        while (result1.size() > 0) {
            Activity ac = result1.get(0);
            int index = 0;
            for (int i = 1; i < result1.size(); i++)
                if (ac.getStart().compareTo(result1.get(i).getStart()) > 0) {
                    index = i;
                    ac = result1.get(i);
                }

            result.add(ac);
            result1.remove(index);
        }
        return result;
    }

    @Override
    public boolean removeLastAddedActivity() {

        int beforeDelete = this.count();
        activities.remove(this.count() - 1);
        this.saveActivities();
        int afterDelete = this.count();
        if (beforeDelete - afterDelete == 1)
            return true;
        return false;
    }


}
