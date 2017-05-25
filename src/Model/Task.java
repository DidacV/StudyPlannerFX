package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Model representation for a task
 * It holds dependencies, task notes and activities
 * Created by Didac on 02/05/2017.
 */
public class Task {

    // Properties ------------------------------------------------------------------------------------------------------

    public enum TaskType {PROGRAMMING, READING}
    private Integer id;
    private String title;
    private TaskType type;
    private int time;
    private String criterion;
    private int criterionValue;
    private int criterionSoFar;
    private double progress; // related to criterion value
    private Date date;
    // TODO :'a task cannot be started before another has been completed'
    private Task dependency;
    //private Map<TaskNote, TaskNote> notes = new HashMap<>();
    private TaskNote taskNote;
    private Map<Activity, Activity> activities = new HashMap<>();
    private ObservableList<Activity> activityObservableList = FXCollections.observableArrayList();

    // Constructor -----------------------------------------------------------------------------------------------------

    public Task() {}

    // TODO: Decide whether we need constructors
    public Task(String title, TaskType type, String criterion,
                int criterionValue, int progress, Date date) {
        // IF FIRST ADDED TO PROFILE DATE = CURRENT DATE
        this.id = UUID.randomUUID().hashCode();
        this.title = title;
        this.type = type;
        this.criterion = criterion;
        this.criterionValue = criterionValue;
        this.progress = progress;
        this.date = date;
        this.time = 0;
    }
    public Task(Integer id, String title, TaskType type, String criterion,
                int criterionValue, int progress, Date date) {
        this(title,type,criterion,criterionValue,progress,date);
        this.id = id;
    }

    // Methods ---------------------------------------------------------------------------------------------------------

//    public void addNote(TaskNote note) {
//        if (!notes.containsKey(note))
//            notes.put(note, note);
//    }

    public void setTaskNote(TaskNote taskNote) {
        this.taskNote = taskNote;
    }

    public TaskNote getTaskNote() {
        return taskNote;
    }
    // Getters and setters ---------------------------------------------------------------------------------------------

    public ObservableList<Activity> getObservableActivityList() {
        for (Activity activity : activities.values()) {
            if (!activityObservableList.contains(activity)) {
                activityObservableList.add(activity);
            }
        }
        return activityObservableList;
    }

    public Integer getId() {return id;}

    public Date getDate() { return date; }

    public void setId(Integer id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }

    public int getCriterionValue() {
        return criterionValue;
    }

    public int getCriterionSoFar() {
        return criterionSoFar;
    }

    public void setCriterionValue(int criterionValue) {
        this.criterionValue = criterionValue;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean deleteActivity(Activity activity) {
        if (!activities.containsKey(activity)) return false;
        this.criterionSoFar -= activity.getQuantity();
        this.time -= activity.getTime();
        this.progress = (criterionSoFar / criterionValue) * 100;
        activities.remove(activity);
        return true;
    }

    //public Map<Task, Task> getDependencyTasks() {return dependencies;}
    public Task getDependency() { return dependency; }

    public boolean addActivity(Activity activity) {
        if (!activities.containsKey(activity) && criterionSoFar < criterionValue) {
            activities.put(activity, activity);
            criterionSoFar += activity.getQuantity();
            progress = ((double) criterionSoFar / criterionValue) * 100;
            System.out.println(progress);
            this.time += activity.getTime();
            return true;
        }
        return false;
    }

    //public Map<Activity, Activity> getActivities() { return activities; }
    public Map<Activity, Activity> getActivities() { return new HashMap<>(activities); }

    public void addDependency(Task dependency) {
        this.dependency = dependency;
    }

    /** Function checks if task is completed based on all the related activities
     *  if complete , it returns true
     *  if not , it sets progress to the
     *
     * @return
     */
    public boolean isComplete(){
        updateProgress();
        return progress == 100;

    }

    public void updateProgress() {
        double count = 0;
        for (Activity activity : activities.values()) {
            count += activity.getQuantity();
        }

        if (progress == criterionValue) progress = 100;
        else {
            if (count != 0) progress = (criterionValue / count) * 100;
            else progress = 0;
        }
    }

//    public Map<TaskNote, TaskNote> getNotes() {
//        return notes;
//    }

    //public List<TaskNote> getNotes() { return new ArrayList<>(notes.values()); }
    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(title).append("\n")
                .append(type.toString()).append("\n")
                .append(time).append("\n")
                .append(criterion).append("\n")
                .append(criterionValue).append("\n")
                .append(progress).append("\n");

//        if (dependency.title != null) {
//            stringBuilder.append(title).append(" depends on ").append(dependency.title);
//        }

        return stringBuilder.toString();
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (getClass() != obj.getClass())
            return false;

        if (id == null) return false;


        Task task = (Task) obj;
        return this.id.equals(task.id);
    }

    @Override
    public int hashCode() {
        if (id == null)
            return title.hashCode();

        return id.hashCode();
    }
}
