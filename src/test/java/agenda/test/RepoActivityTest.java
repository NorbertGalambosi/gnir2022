package agenda.test.func2;

import agenda.model.base.Activity;
import agenda.model.base.Contact;
import agenda.model.repository.classes.RepositoryActivityFile;
import agenda.model.repository.classes.RepositoryContactFile;
import agenda.model.repository.interfaces.RepositoryActivity;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NameNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RepoActivityTest {

    private Activity act;
    private RepositoryActivity repositoryActivity;

    @Before
    public void setUp() throws Exception {
        repositoryActivity = new RepositoryActivityFile(new RepositoryContactFile());
    }

    @Test
    public void addActivityWithSuccessNoContacts() {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {

            int repoSizeBeforeAdd = repositoryActivity.count();
            act = new Activity("name1",
                    df.parse("03/20/2013 12:00"),
                    df.parse("03/20/2013 13:00"),
                    null,
                    "Lunch break");
            repositoryActivity.addActivity(act);

            int itemsAdded = repositoryActivity.count();
//            assertTrue(itemsAdded == repoSizeBeforeAdd + 1);
            assertTrue(repositoryActivity.removeLastAddedActivity());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addActivityWithSuccess() {

        List<Contact> contactList = new ArrayList<Contact>();
        try {
            contactList.add(new Contact("Ion", "empty", "0", "empty@empty.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {

            int repoSizeBeforeAdd = repositoryActivity.count();

            act = new Activity("name1",
                    df.parse("03/20/2013 12:00"),
                    df.parse("03/20/2013 13:00"),
                    contactList,
                    "Cumparam mobila", "me",
                    "Casa Rusu");

            repositoryActivity.addActivity(act);

            int itemsAdded = repositoryActivity.count();
            assertTrue(itemsAdded == repoSizeBeforeAdd + 1);
            assertTrue(repositoryActivity.removeLastAddedActivity());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addActivityWithContactNameError() {

        List<Contact> contactList = new ArrayList<Contact>();
        try {
            contactList.add(new Contact("Ionut", "empty", "0", "empty@empty.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            int repoSizeBeforeAdd = repositoryActivity.count();

            act = new Activity("name1",
                    df.parse("03/20/2013 12:00"),
                    df.parse("03/20/2013 13:00"),
                    contactList,
                    "Lunch break", "me",
                    "La casa");

            repositoryActivity.addActivity(act);

            int itemsAdded = repositoryActivity.count();
            assertTrue(itemsAdded == repoSizeBeforeAdd);
            assertTrue(repositoryActivity.removeLastAddedActivity());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addActivityWithoutSuccess() {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {

            int repoSizeBeforeAdd = repositoryActivity.count();
            act = new Activity("name1",
                    df.parse("04/20/2013 12:00"),
                    df.parse("04/20/2013 13:00"),
                    null,
                    "Lunch break");
            repositoryActivity.addActivity(act);

            int itemsAdded = repositoryActivity.count();
//            assertTrue(itemsAdded == repoSizeBeforeAdd);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getActivitesByDateWithSuccess() {

        List<Contact> contactList = new ArrayList<Contact>();
        try {
            contactList.add(new Contact("Ion", "empty", "0", "empty@empty.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            act = new Activity("Ion",
                    df.parse("03/20/2013 12:00"),
                    df.parse("03/20/2013 13:00"),
                    contactList,
                    "Cumparam mobila", "me",
                    "Casa Rusu");

            DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
            repositoryActivity.addActivity(act);
            assertTrue("1 Activity found ", 1 ==
                    repositoryActivity.activitiesOnDate("me", date.parse("03/20/2013")).size());
            repositoryActivity.removeLastAddedActivity();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getActivitesByDateWithFailureNoValidUsername() {

        List<Contact> contactList = new ArrayList<Contact>();
        try {
            contactList.add(new Contact("Ion", "empty", "0", "empty@empty.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            act = new Activity("Ion",
                    df.parse("03/20/2013 12:00"),
                    df.parse("03/20/2013 13:00"),
                    contactList,
                    "Cumparam mobila", "gigel",
                    "Casa Rusu");

            DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
            repositoryActivity.addActivity(act);
            assertFalse("1 Activity found ", 1 ==
                    repositoryActivity.activitiesOnDate("me", date.parse("03/20/2013")).size());
            repositoryActivity.removeLastAddedActivity();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
