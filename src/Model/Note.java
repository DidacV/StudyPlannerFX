package Model;

import java.util.Date;

/**
 * Model representation of a note
 * Created by Didac on 02/05/2017.
 */
public class Note {

    // Properties ------------------------------------------------------------------------------------------------------

    private String title;
    private String text;
    private Date date;

    // Constructor -----------------------------------------------------------------------------------------------------

    public Note(String title, String text, Date date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        this.date = new Date();
    }

    // Getters and setters ---------------------------------------------------------------------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        String str = "";
        str += title + "\n"
                + text + "\n" + date;
        return str;

    }

}
