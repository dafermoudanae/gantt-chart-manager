package dom.gantt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopLevelTask extends TaskAbstract {
	
	private int taskStart;
	private int taskEnd;
	private double cost;
	private double effort;
	private List<TaskAbstract> subTasks = new ArrayList<TaskAbstract>();
	
	public TopLevelTask(int taskId, String taskText, int containerTaskId) {
		super(taskId, taskText, containerTaskId);
	}
	
	
	public void addSubTask(TaskAbstract subTask) {
		subTasks.add(subTask);
		recalculateProperties();
		Collections.sort(subTasks);
		//edw to allo sort gia ta subtasks
	}
	
	public void recalculateProperties() {
		if (subTasks.isEmpty()) {
			taskStart = 0;
			taskEnd = 0;
			cost = 0;
			effort = 0;
			return;
		}
		
		//Initialize with the values of the first subtask
		taskStart = subTasks.get(0).getTaskStart();
		taskEnd = subTasks.get(0).getTaskEnd();
		cost = 0;
		effort = 0;
		
		for (TaskAbstract task : subTasks) {
			if (task.getTaskStart() < taskStart) {
				taskStart = task.getTaskStart();
			}
			
			if (task.getTaskEnd() > taskEnd) {
				taskEnd = task.getTaskEnd();
			}
			
			cost += task.getCost();
			effort += task.getEffort();
			
		}
	}
	
	public  List<TaskAbstract> getSubtasks(){
		return subTasks;
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
		return false;
	}
	
	
}
