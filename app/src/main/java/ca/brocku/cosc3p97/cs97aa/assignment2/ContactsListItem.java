package ca.brocku.cosc3p97.cs97aa.assignment2;

import java.util.List;


/**
 * A class that defines the contents of the contact list item
 */
public class ContactsListItem {
    String name;


    /**
     * Constructor for this class
     * @param name The name of the contact
     */
    public ContactsListItem(String name) {
        this.name = name;
    }


    /**
     * Static method that converts a list of ContactsListItem into an array of String
     * @param list The list that will be converted
     * @return An array of String
     */
    public static String[] getNames(List<ContactsListItem> list) {
        String[] names = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            names[i] = list.get(i).name;
        }

        return names;
    }


    /**
     * Overriding the inherited equals method to equal to ContactListItems when
     * their respective name values are equivalent
     * @param o Object to compare to
     * @return A boolean if the two objects have the same name
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ContactsListItem) {
            return name.equals(((ContactsListItem) o).name);
        } else {
            return false;
        }
    }
}
