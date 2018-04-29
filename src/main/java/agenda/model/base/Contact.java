package agenda.model.base;

import agenda.exceptions.InvalidFormatException;

import java.util.regex.Pattern;

public class Contact {
    private String Name;
    private String Address;
    private String Telefon;
    private String Email;

    public Contact() {
        Name = "";
        Address = "";
        Telefon = "";
        Email = "";
    }

    public Contact(String name, String address, String telefon, String email) throws Exception, InvalidFormatException {
        if (!validTelefon(telefon)) throw new InvalidFormatException("Cannot convert", "Invalid phone number");
        if (!validName(name)) throw new Exception("Invalid name type, should be string.");
        if (!validAddress(address)) throw new InvalidFormatException("Cannot convert", "Invalid address");
        if (!validEmail(email)) throw new InvalidFormatException("Cannot convert", "Invalid email format");
        Name = name;
        Address = address;
        Telefon = telefon;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) throws InvalidFormatException {
        if (!validName(name)) throw new InvalidFormatException("Cannot convert", "Invalid name");
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) throws InvalidFormatException {
        if (!validAddress(address)) throw new InvalidFormatException("Cannot convert", "Invalid address");
        Address = address;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) throws InvalidFormatException {
        if (!validTelefon(telefon)) throw new InvalidFormatException("Cannot convert", "Invalid phone number");
        Telefon = telefon;
    }

    public static Contact fromString(String str, String delim) throws Exception {
        String[] s = str.split(delim);
        if (s.length != 4) throw new InvalidFormatException("Cannot convert", "Invalid data");
        if (!validTelefon(s[2])) throw new InvalidFormatException("Cannot convert", "Invalid phone number");
        if (!validName(s[0])) throw new InvalidFormatException("Cannot convert", "Invalid name");
        if (!validAddress(s[1])) throw new InvalidFormatException("Cannot convert", "Invalid address");

        return new Contact(s[0], s[1], s[2], s[3]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Name);
        sb.append("#");
        sb.append(Address);
        sb.append("#");
        sb.append(Telefon);
        sb.append("#");
        sb.append(Email);
        return sb.toString();
    }

    private static boolean validName(String str) {

        String[] nameArgs = str.split(" ");
        try {
            nameArgs[0].equals(Integer.parseInt(nameArgs[0]));
        } catch (NumberFormatException e) {
            return true;
        }

        if (nameArgs.length > 3 && nameArgs.length < 15) {
            return true;
        }

        return false;

    }

    private static boolean validAddress(String str) {
        return true;
    }

    private static boolean validTelefon(String tel) {

        if (tel.length() <= 0)
            return false;
        return true;
    }

    private static boolean validEmail(String email) {

        String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        boolean isMatch = Pattern.matches(pattern, email);

        if (isMatch)
            return true;

        return false;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Contact)) return false;
        Contact o = (Contact) obj;
        if (Name.equals(o.Name) && Address.equals(o.Address) &&
                Telefon.equals(o.Telefon))
            return true;
        return false;
    }

}
