package agenda.test.func1;

import agenda.model.base.Contact;
import agenda.model.repository.classes.RepositoryContactMock;
import agenda.model.repository.interfaces.RepositoryContact;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RepoContactTest {

    private RepositoryContact repositoryContact;
    private Contact contact;

    @Before
    public void setUp() throws Exception {

        this.repositoryContact = new RepositoryContactMock();
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
    public void addContactNameInputTypeIsInvalid() {

        try {
            contact = new Contact("1", "address1", "+4071122334455", "email@dom.com");
        } catch (Exception e) {
            assertTrue(true);
            System.out.println("Name can't be integer");
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 != repositoryContact.count());
    }

    @Test
    public void addContactNameHasValidLength() {

        try {
            contact = new Contact("Gigel", "address1", "+4071122334455", "email@dom.com");
        } catch (Exception e) {
            assertTrue(true);
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 == repositoryContact.count());
    }

    @Test
    public void addContactNameHasInvalidLength() {

        try {
            contact = new Contact("1", "address1", "+4071122334455", "email@dom.com");
        } catch (Exception e) {
            assertTrue(true);
            System.out.println("Name has invalid length");
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 != repositoryContact.count());
    }

    @Test
    public void addContactInvalidEmail() {

        try {
            contact = new Contact("Gigel", "address1", "+4071122334455", "email@d");
        } catch (Exception e) {
            assertTrue(true);
            System.out.println("Invalid email");
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 != repositoryContact.count());
    }

    @Test
    public void addContactNoPhoneNumber() {

        try {
            contact = new Contact("Gigel", "address1", "", "email@dot.com");
        } catch (Exception e) {
            assertTrue(true);
            System.out.println("Wrong phone number");
        }

        int repoSizeBeforeAdd = repositoryContact.count();
        repositoryContact.addContact(contact);
        assertTrue(repoSizeBeforeAdd + 1 != repositoryContact.count());
    }

}
