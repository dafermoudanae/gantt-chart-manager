package dom.gantt;

import java.util.List;

public class SubTask extends TaskAbstract {
	
	private int taskStart;
	private int taskEnd;
	private double cost;
	private double effort;
	
	public SubTask(int taskId, String taskText, int containerTaskId, int taskStart, int taskEnd, double cost, double effort) {
		super(taskId, taskText, containerTaskId);
		this.taskStart = taskStart;
		this.taskEnd = taskEnd;
		this.cost = cost;
		this.effort = effort;
		
	}
	
	public  int getTaskStart() {
		return taskStart;
	}
	public  int getTaskEnd() {
		return taskEnd;
	}
	public  double getCost() {
		return cost;
	}
	public  double getEffort() {
		return effort;
	}
	public  boolean isSimple() {
		return true;
	}
	
	public  List<TaskAbstract> getSubtasks(){
		return null;
	}
	
	public void addSubTask(TaskAbstract subTask) {
		return;
	}
}
