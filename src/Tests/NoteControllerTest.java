package Tests;

import Controller.ActivityController;
import Model.Activity;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Didac on 09/05/2017.
 */
public class NoteControllerTest {
    ArrayList<Activity> activities;

    @Before
    public void setUp() throws Exception {
        ActivityController controller = new ActivityController();
        activities = controller.findAll(2);
    }

    @Test
    public void findTaskNotes() throws Exception {
    }

    @Test
    public void findActivityNotes() throws Exception {
        //ArrayList<ActivityNote> activityNotes = NoteController.findActivityNotes(activities.get(0).getActivityId());
        //activities.get(0).setNotes(activityNotes);
        //System.out.println(activityNotes.get(0));
    }

}