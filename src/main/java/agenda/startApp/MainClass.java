package agenda.startApp;

import agenda.exceptions.InvalidFormatException;
import agenda.model.base.Activity;
import agenda.model.base.Contact;
import agenda.model.base.User;
import agenda.model.repository.classes.RepositoryActivityFile;
import agenda.model.repository.classes.RepositoryContactFile;
import agenda.model.repository.classes.RepositoryUserFile;
import agenda.model.repository.interfaces.RepositoryActivity;
import agenda.model.repository.interfaces.RepositoryContact;
import agenda.model.repository.interfaces.RepositoryUser;

import javax.naming.NameNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

//functionalitati
//i.	 adaugarea de contacte (nume, adresa, numar de telefon, adresa email);
//ii.	 programarea unor activitati (denumire, descriere, data, locul, ora inceput, durata, contacte).
//iii.	 generarea unui raport cu activitatile pe care le are utilizatorul (nume, user, parola) la o anumita data, ordonate dupa ora de inceput.

public class MainClass {

    public static void main(String[] args) {
        BufferedReader in = null;
        try {
            RepositoryContact contactRep = new RepositoryContactFile();
            RepositoryUser userRep = new RepositoryUserFile();
            RepositoryActivity activityRep = new RepositoryActivityFile(
                    contactRep);

            User user = null;
            in = new BufferedReader(new InputStreamReader(System.in));
            while (user == null) {
                System.out.printf("Enter username: ");
                String u = in.readLine();
                System.out.printf("Enter password: ");
                String p = in.readLine();

                user = userRep.getByUsername(u);
                if (!user.isPassword(p))
                    user = null;
            }

            int chosen = 0;
            while (chosen != 4) {
                printMenu();
                chosen = Integer.parseInt(in.readLine());
                try {
                    switch (chosen) {
                        case 1:
                            adaugContact(contactRep, in);
                            break;
                        case 2:
                            adaugActivitate(activityRep, contactRep, in, user);
                            break;
                        case 3:
                            afisActivitate(activityRep, in, user);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {

        }
        System.out.println("Program over and out\n");
    }

    private static void afisActivitate(RepositoryActivity activityRep,
                                       BufferedReader in, User user) {
        try {
            System.out.printf("Afisare Activitate: \n");
            System.out.printf("Data(format: mm/dd/yyyy): ");
            String dateS = in.readLine();
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(dateS.split("/")[2]),
                    Integer.parseInt(dateS.split("/")[0]) - 1,
                    Integer.parseInt(dateS.split("/")[1]));
            Date d = c.getTime();

            System.out.println("Activitatile din ziua " + d.toString() + ": ");

            List<Activity> act = activityRep
                    .activitiesOnDate(user.getName(), d);
            for (Activity a : act) {
                System.out.printf("%s - %s: %s - %s with: ", a.getStart()
                        .toString(), a.getDuration().toString(), a
                        .getDescription());
                for (Contact con : a.getContacts())
                    System.out.printf("%s, ", con.getName());
                System.out.println();
            }
        } catch (IOException e) {
            System.out.printf("Eroare de citire: %s\n" + e.getMessage());
        }
    }

    private static void adaugActivitate(RepositoryActivity activityRep,
                                        RepositoryContact contactRep, BufferedReader in, User user) {
        try {
            System.out.printf("Adauga Activitate: \n");
            System.out.printf("Denumire: ");
            String denumire = in.readLine();
            System.out.printf("Descriere: ");
            String description = in.readLine();
            System.out.printf("Loc : ");
            String place = in.readLine();
            System.out.printf("Start Date(format: mm/dd/yyyy): ");
            String dateS = in.readLine();
            System.out.printf("Start Time(hh:mm): ");
            String timeS = in.readLine();
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(dateS.split("/")[2]),
                    Integer.parseInt(dateS.split("/")[0]) - 1,
                    Integer.parseInt(dateS.split("/")[1]),
                    Integer.parseInt(timeS.split(":")[0]),
                    Integer.parseInt(timeS.split(":")[1]));
            Date start = c.getTime();

            System.out.printf("End Date(format: mm/dd/yyyy): ");
            String dateE = in.readLine();
            System.out.printf("End Time(hh:mm): ");
            String timeE = in.readLine();
            System.out.println("Number of contacts :");
            Integer nrCon = Integer.parseInt(in.readLine());
            LinkedList<Contact> contacts = new LinkedList<Contact>();
            for (int i = 0; i < nrCon; i++) {
                System.out.println("Contact name : ");
                String cname = in.readLine();
                try {
                    contacts.add(new Contact(cname, "empty", "0", "empty@empty.com"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            c.set(Integer.parseInt(dateE.split("/")[2]),
                    Integer.parseInt(dateE.split("/")[0]) - 1,
                    Integer.parseInt(dateE.split("/")[1]),
                    Integer.parseInt(timeE.split(":")[0]),
                    Integer.parseInt(timeE.split(":")[1]));
            Date end = c.getTime();

            Activity act = new Activity(denumire, start, end, contacts, description, user.getName(), place);

            try {
                if (activityRep.addActivity(act))
                    System.out.printf("S-a adugat cu succes\n");
                else
                    System.out.println("Nu s'a adaugat");
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.printf("Eroare de citire: %s\n" + e.getMessage());
        }

    }

    private static void adaugContact(RepositoryContact contactRep,
                                     BufferedReader in) throws Exception {

        try {
            System.out.printf("Adauga Contact: \n");
            System.out.printf("Nume: ");
            String name = in.readLine();
            System.out.printf("Adresa: ");
            String adress = in.readLine();
            System.out.printf("Numar de telefon: ");
            String telefon = in.readLine();
            System.out.printf("Email : ");
            String email = in.readLine();

            Contact c = new Contact(name, adress, telefon, email);

            contactRep.addContact(c);
            contactRep.saveContracts();
            System.out.printf("S-a adugat cu succes\n");
        } catch (IOException e) {
            System.out.printf("Eroare de citire: %s\n" + e.getMessage());
        } catch (InvalidFormatException e) {
            if (e.getCause() != null)
                System.out.printf("Eroare: %s - %s\n" + e.getMessage(), e
                        .getCause().getMessage());
            else
                System.out.printf("Eroare: %s\n" + e.getMessage());
        }

    }

    private static void printMenu() {
        System.out.printf("Please choose option:\n");
        System.out.printf("1. Adauga contact\n");
        System.out.printf("2. Adauga activitate\n");
        System.out.printf("3. Afisare activitati din data de...\n");
        System.out.printf("4. Exit\n");
        System.out.printf("Alege: ");
    }
}
