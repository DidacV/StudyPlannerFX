package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

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

    // Foreign key code module
    //private String moduleCode;

    //TODO: Be able to define study milestones which must be attached to coursework or exams
    //TODO: Be able to define study tasks contributing towards specific coursework or exams

    // EMPTY CONSTRUCTOR FOR TESTING!!!!!!!!!!!!!!!!!!!!!!!
    public Assessment() {}

    public Assessment(String title, Type type, int weight, Date deadline, Double completion) {
        this.id = null;
        this.title = title;
        this.type = type;
        this.weight = weight;
        this.deadline = deadline;
        this.completion.set(completion);
        //this.moduleCode = moduleCode;
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

    public void addMilestone(Milestone milestone) {
        if (!milestones.containsKey(milestone))
            milestones.put(milestone, milestone);
    }

    public void deleteMilestone(Milestone milestone) { milestones.remove(milestone); }

    //public void addTasks(Map<Integer, Task> tasks) { this.tasks = tasks; }

    //public void addAllTasks(ArrayList<Task> tasks) { this.tasks = tasks; }


    public boolean deleteTask(Task task) {
        for (HashMap.Entry entry : tasks.entrySet()) {
            if (!(entry.getValue()).equals(task)) {
                for (HashMap.Entry dependency : ((Task) entry.getValue()).getDependencies().entrySet()) {
                    if (dependency.getValue().equals(task)) { return false; }
                }
            }
        }
        //if (!task.getDependencies().isEmpty()) return false;
        tasks.remove(task);
        calculateCompletion();
        return true;
    }

    // Methods ---------------------------------------------------------------------------------------------

    public DoubleProperty completionProperty() {
        return completion;
    }

    public void calculateCompletion(){
        double count = 0;
        if (tasks.isEmpty()) completion.setValue(0);
        for(HashMap.Entry entry : tasks.entrySet()) {
            Task task = (Task) entry.getValue();
            //task.isComplete();
            count += task.getProgress();
        }
        count = count / tasks.entrySet().size();
        completion.setValue(count);
    }

    public void addTask(Task task) {
        if (!tasks.containsKey(task))
            tasks.put(task, task);
        calculateCompletion();
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
