package agenda.test.func3;

import agenda.model.base.Activity;
import agenda.model.base.Contact;
import agenda.model.repository.classes.RepositoryActivityFile;
import agenda.model.repository.classes.RepositoryContactFile;
import agenda.model.repository.interfaces.RepositoryActivity;
import agenda.model.repository.interfaces.RepositoryContact;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NameNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TopDownIntegration {

    private Activity activity;
    private Contact contact;
    private RepositoryActivity repositoryActivity;
    private RepositoryContact repositoryContact;

    @Before
    public void setUp() throws Exception {
        repositoryContact = new RepositoryContactFile();
        repositoryActivity = new RepositoryActivityFile(repositoryContact);
    }

    @Test
    public void addContactAllInputTypesAreValid() {

        try {
            contact = new Contact("Name", "address1", "+4071122334455", "email@dom.com");
        } catch (Exception e) {
            assertTrue(true);
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 == repositoryContact.count());
    }

    @Test
    public void integrationAddActivity() {

        //test add contact ( I )
        try {
            contact = new Contact("TopDownTestContact", "address1", "+4071122334455", "email@dom.com");
        } catch (Exception e) {
            assertTrue(true);
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 == repositoryContact.count());

//test add activity ( II )
        List<Contact> contactList = new ArrayList<Contact>();
        contactList.add(contact);
        repositoryContact.saveContracts();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {

            repoSizeBeforeAdd = repositoryActivity.count();

            activity = new Activity("nameul",
                    df.parse("03/20/2013 12:00"),
                    df.parse("03/20/2013 13:00"),
                    contactList,
                    "Cumparam mobila", "TopDownTestActivity",
                    "Casa Rusu");

            repositoryActivity.addActivity(activity);

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
    public void integrationSearchActivites() {

        //test add contact ( I )
        try {
            contact = new Contact("TopDownTestContact", "address1", "+4071122334455", "email@dom.com");
        } catch (Exception e) {
            assertTrue(true);
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 == repositoryContact.count());

//test add activity ( II )
        List<Contact> contactList = new ArrayList<Contact>();
        contactList.add(contact);
        repositoryContact.saveContracts();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {

            repoSizeBeforeAdd = repositoryActivity.count();

            activity = new Activity("nameul",
                    df.parse("03/20/2013 12:00"),
                    df.parse("03/20/2013 13:00"),
                    contactList,
                    "Cumparam mobila", "TopDownTestActivity",
                    "Casa Rusu");

            repositoryActivity.addActivity(activity);

            int itemsAdded = repositoryActivity.count();
            assertTrue(itemsAdded == repoSizeBeforeAdd + 1);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//test search for activities ( III )

        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");

        try {
            assertTrue("1 Activity found ", 1 ==
                    repositoryActivity.activitiesOnDate("TopDownTestActivity", date.parse("03/20/2013")).size());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertTrue(repositoryActivity.removeLastAddedActivity());
        repositoryContact.removeContact(contact);
    }
}
