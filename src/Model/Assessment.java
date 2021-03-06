package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Model representation of an assessment
 * Created by Didac on 02/05/2017.
 */
public class Assessment {


    // Properties ------------------------------------------------------------------------------------------------------

    public enum Type {EXAM, ASSIGNMENT}

    private Integer id;
    private String title;
    private Type type;
    private int weight;
    private Date deadline;
    private DoubleProperty completion = new SimpleDoubleProperty(); // Percentage
    private HashMap<Task, Task> tasks = new HashMap<>();
    private HashMap<Milestone,Milestone> milestones = new HashMap<>();

    private ObservableList<Task> observableTaskList = FXCollections.observableArrayList();
    private ObservableList<Milestone> observableMilestoneList = FXCollections.observableArrayList();

    public Assessment() {}

    public Assessment(String title, Type type, int weight, Date deadline, Double completion) {
        this.id = null;
        this.title = title;
        this.type = type;
        this.weight = weight;
        this.deadline = deadline;
        this.completion.set(completion);
    }
    public Assessment(int id, String title, Type type, int weight, Date deadline, double completion) {
        this(title,type,weight,deadline,completion);
        this.id = id;
    }
    // Getters and setters ---------------------------------------------------------------------------------------------

    public int getId() { return id; }

    public Type getType() { return type; }

    public String getTitle() {
        return title;
    }

    public int getWeight() {
        return weight;
    }

    public Date getDeadLine() {
        return deadline;
    }

    public double getCompletion() {
        return completion.get();
    }

    public HashMap<Milestone,Milestone> getMilestones() { return new HashMap<>(milestones);}

    public void setId(int id) { this.id = id;}

    public void setType(Type type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setCompletion(double completion) {
        this.completion.set(completion);
    }

    public Map<Task, Task> getTasks() { return new HashMap<>(tasks); }

    /** Function used to add a milestone
     *  to this assessment if not already present
     *
     * @param milestone
     */
    public boolean addMilestone(Milestone milestone) {
        if (!milestones.containsKey(milestone)){
            milestones.put(milestone, milestone);
            return true;
        }
        return false;
    }

    /** Function used to delete a
     *  milestone from this assessment
     *
     * @param milestone
     */
    public void deleteMilestone(Milestone milestone) { milestones.remove(milestone); }

    /** Function used to delete
     *  a task from this assessment
     *
     * @param passedTask
     * @return true if deleted / false otherwise
     */
    public boolean deleteTask(Task passedTask) {
        for (Task task : tasks.values()) {
            if (!task.equals(passedTask)) {
                if (task.getDependency() != null) {
                    if (task.getDependency().equals(passedTask)) return false;
                }
            }
        }
        tasks.remove(passedTask);
        updateCompletion();
        return true;
    }

    // Methods ---------------------------------------------------------------------------------------------

    public DoubleProperty completionProperty() {
        return completion;
    }

    /** Function used to update assessment completion based on tasks
     *  which in turn are based on activities
     *
     */
    public void updateCompletion() {
        double count = 0;
        if (tasks.isEmpty()) {
            completion.setValue(0);
            return;
        }

        for (Task task : tasks.values()) {
            count += task.getProgress();
        }

        completion.set(count / tasks.size());
    }

    /** Function used to add
     *  a task to this assessment
     *
     * @param task
     */
    public boolean addTask(Task task) {
        if (!tasks.containsKey(task)){
            tasks.put(task, task);
            updateCompletion();
            return  true;
        }
        return false;
    }

    public ObservableList<Task> getObservableTaskList() {
        for (Task task : tasks.values()) {
            if (!observableTaskList.contains(task)) {
                observableTaskList.add(task);
            }
        }
        return observableTaskList;
    }

    public ObservableList<Milestone> getObservableMilestoneList() {
        for (Milestone milestone : milestones.values()) {
            if (!observableMilestoneList.contains(milestone)) {
                observableMilestoneList.add(milestone);
            }
        }
        return observableMilestoneList;
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    // TODO: Rewrite the tostring
    @Override
    public String toString() {
        String str="";
        str += title + "\n" + type + "\n" + "Weight :" + weight +"%";
        return str;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        if (getClass() != obj.getClass())
            return false;

        if (id == null) return false;

        Assessment assessment = (Assessment) obj;
        return this.id.equals(assessment.id);
    }

    @Override
    public int hashCode() {
        if (id == null)
            return title.hashCode();

        return id.hashCode();
    }
}
