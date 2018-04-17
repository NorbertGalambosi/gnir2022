package agenda.model.repository.classes;

import agenda.exceptions.InvalidFormatException;
import agenda.model.base.Contact;
import agenda.model.repository.interfaces.RepositoryContact;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class RepositoryContactFile implements RepositoryContact {

    private static final String filename = "C:\\Users\\gnorb\\Desktop\\Semestrul 2\\VVSS\\gnir2022\\ProiectAgenda\\files\\contacts.txt";
    private List<Contact> contacts;

    public RepositoryContactFile() throws Exception {
        contacts = new LinkedList<Contact>();
        BufferedReader br = null;
//		String currentDir = new File(".").getAbsolutePath();
//		System.out.println(currentDir);
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename)));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                Contact c = null;
                try {
                    c = Contact.fromString(line, "#");
                } catch (InvalidFormatException e) {
                    throw new Exception("Error in file at line " + i,
                            new Throwable(e.getCause().getMessage()));
                }
                contacts.add(c);
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
    public List<Contact> getContacts() {
        return new LinkedList<Contact>(contacts);
    }

    @Override
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    @Override
    public boolean removeContact(Contact contact) {
        int index = contacts.indexOf(contact);
        if (index < 0)
            return false;
        else
            contacts.remove(index);
        return true;
    }

    @Override
    public boolean saveContracts() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(filename));
            for (Contact c : contacts)
                pw.println(c.toString());
        } catch (Exception e) {
            return false;
        } finally {
            if (pw != null) pw.close();
        }
        return true;
    }

    @Override
    public int count() {
        return contacts.size();
    }

    @Override
    public Contact getByName(String string) {
        for (Contact c : contacts)
            if (c.getName().equals(string))
                return c;
        return null;
    }

    @Override
    public boolean contactNameExists(String fullName) {

        int countAppearances = 0;

        for (Contact contact : contacts) {
            if (contact.getName().equals(fullName))
                countAppearances++;
        }

        if (countAppearances != 0)
            return true;

        return false;
    }

}
