package ca.brocku.cosc3p97.cs97aa.assignment2;

import java.util.List;

public class ContactsListItem {
    String name;

    public ContactsListItem(String name) {
        this.name = name;
    }

    public static String[] getNames(List<ContactsListItem> list) {
        String[] names = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            names[i] = list.get(i).name;
        }

        return names;
    }


    @Override
    public boolean equals(Object o) {
        return name == ((ContactsListItem) o).name;
    }
}
