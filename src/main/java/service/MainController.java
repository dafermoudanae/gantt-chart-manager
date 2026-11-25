package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dom.gantt.SubTask;
import dom.gantt.TaskAbstract;
import dom.gantt.TopLevelTask;
import util.FileTypes;
import util.ProjectInfo;
import load.FileLoaderFactory;
import load.IFileLoader;

//import org.apache.logging.log4j.core.tools.picocli.CommandLine.Help.TextTable.Cell;
import org.apache.poi.hpsf.Date;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.nio.file.Path;
import java.nio.file.Paths;



public class MainController implements IMainController{
	
	
	List<String> tasksAsStrings = new ArrayList<String>();
	
	List<TaskAbstract> tasks;
	Workbook workbook = null;
	String sourcePath;
	String targetPath;
	Map<String, CellStyle> styles = new HashMap<String, CellStyle>() ;
	
	public MainController() {
		tasks = new ArrayList<TaskAbstract>();
	}
	
	public List<String> load(String sourcePath, FileTypes filetype){
		FileLoaderFactory factoryLoader =new FileLoaderFactory();
		IFileLoader fileLoader = factoryLoader.createLoader(filetype);
		tasksAsStrings = fileLoader.load(sourcePath, filetype);
		tasks = taskLoader(tasksAsStrings);
		if (tasks.isEmpty()) { // an einai adeio exoume valei lathos dedomena apo to arxeio eisodou, exei provlima to arxeio diladi
			return new ArrayList<String>(); //opote epistrefoume k emeis ena adeio arrayList
		}
		this.sourcePath = sourcePath;
		return tasksAsStrings;
	}
	/*
	private List<TaskAbstact> taskToString(tasksAsStrings){
		return tasksAsStrings;
	}
	*/
	private List<TaskAbstract> taskLoader(List<String> tasksAsStrings){
		List<TaskAbstract> taskList = new ArrayList<TaskAbstract>();
		//prepei to kathe String na spasei me bash to \t se pinaka me Strings estw array
		// array[0] einai to taskId to array[1] to text to array[2] to mamaId
		//An to 2 dld to mamaId ==0 tote ftiaxnoume topTask alliws subTask
		try {
			 for (String taskAsTextLine: tasksAsStrings){
				String array[] = taskAsTextLine.split("\t");
				int taskId = (int)Double.parseDouble(array[0]);
				String text = array[1];
				int mamaId = (int)Double.parseDouble(array[2]);
				
				if (mamaId == 0) {
					taskList.add(new TopLevelTask(taskId, text, mamaId));
				}else {
					int taskStart = (int)Double.parseDouble(array[3]); 
					int taskEnd = (int)Double.parseDouble(array[4]) ;
					double cost = Double.parseDouble(array[5]) ;
					double effort = Double.parseDouble(array[6]);
					taskList.add(new SubTask(taskId, text, mamaId, taskStart, taskEnd, cost,  effort));
					
				}
			 }
		} catch (Exception e) {
			return new ArrayList<TaskAbstract>(); //epistrefoume ena adeio arraylist afou den mporesame na tou valoume dedomena
		}
		 
		 ///prwta prepei na einai fortwmena ola ta tasks kai meta fortwnoyme ola ta subtasks sta topleveltasks
		for (TaskAbstract t : taskList) {
				if(!t.isSimple()) {
					for (TaskAbstract st : taskList) {
						if(t.getTaskId() == st.getContainerTaskId()) {
							t.addSubTask(st); //to ArrayList tasks (me ola ta tasks) kalei thn get me to teleytaio stoixeio pou einai to neo subtask pou diavasame
						}
					}				
				}
		}
		
		// exei thema i sort, den kanei swsti tasksinomisi
		Collections.sort(taskList);
		 // edw kanoume sort to tasks giati den vazei ta simple tasks katw apo ta top tasks pou anoikoun panta
		
		List<TaskAbstract> sortedTasks = new ArrayList<TaskAbstract>();
		for (TaskAbstract t : taskList) {
			if(!t.isSimple()) {
				sortedTasks.add(t);
				for (TaskAbstract st : t.getSubtasks()) {
					sortedTasks.add(st);
				}				
			}
		}
		 return sortedTasks;
	}

	public ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath){
		
		ProjectInfo projectInfo = null;
		this.targetPath=targetPath;
		try {
			//elegxos an yparxei to arxeio 
			File check = new File(targetPath);
			if (check.exists()) {
				try (InputStream fileIn = new FileInputStream(targetPath)){
					if(fileType == FileTypes.XLS) {
						workbook = new HSSFWorkbook(fileIn);
					}else {
						workbook = new XSSFWorkbook(fileIn);
					}
				} catch (Exception e) {
					return null;
				}
			}else {
				if(fileType == FileTypes.XLS) {
					workbook = new HSSFWorkbook();
				}else {
					workbook = new XSSFWorkbook();
				}
			}
			Path path = Paths.get(targetPath);
	        String projectName = path.getFileName().toString();
			
			projectInfo = new ProjectInfo(projectName,sourcePath,targetPath,getAllTasks().size(), getTopLevelTasksOnly().size());
			
			//ftiaxnoume ta default newstyles edw gia to neo workbook, nwritera den exoume workbook, argotera se alli methodo tha ektelountai se kathe klisi k den theloume na ginei auto
			addFontedStyle("Normal", IndexedColors.LAVENDER.getIndex(), (short) 10,  "Times New Roman",
					false, false, false, IndexedColors.LAVENDER.getIndex(),
					"Solid", "Left", false);
			
			addFontedStyle("DefaultHeaderStyle",IndexedColors.PINK.getIndex(), (short) 10, "Times New Roman",
					false, false, false, IndexedColors.VIOLET.getIndex(),
					"Solid", "Left", false);
			
			addFontedStyle("TopTask_bar_style",IndexedColors.PINK.getIndex(), (short) 10, "Times New Roman",
					false, false, false, IndexedColors.PINK.getIndex(),
					"Solid", "Left", false);
			
			addFontedStyle("TopTask_data_style", IndexedColors.WHITE.getIndex(), (short) 11, "Times New Roman",
					true, false, false, IndexedColors.ORCHID.getIndex(),
					"Solid", "Left", false);
			
			addFontedStyle("NonTopTask_bar_style", IndexedColors.MAROON.getIndex(), (short) 10, "Times New Roman",
					false, false,false, IndexedColors.MAROON.getIndex(),
					"Solid", "Left", false);
			
			addFontedStyle("NonTopTask_data_style",IndexedColors.WHITE.getIndex(), (short) 10, "Times New Roman",
					false, false, false, IndexedColors.PINK1.getIndex(),
					"Solid", "Left", false);
			
			return projectInfo;
		
		
		
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<TaskAbstract> getAllTasks(){
		try { 
			if (tasks.isEmpty()) {
				return new ArrayList<>();
			}
			return new ArrayList<>(tasks);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<TaskAbstract> getTopLevelTasksOnly(){
		try { 
			if (tasks.isEmpty()) {
				return new ArrayList<>();
			}
			
			List<TaskAbstract> topLevelTasks = new ArrayList<>();
			
			for (TaskAbstract task : tasks) {
				if(task instanceof TopLevelTask) {
					topLevelTasks.add(task);
				}
			}
			
			return topLevelTasks;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<TaskAbstract> getTasksInRange(int firstIncluded, int lastIncluded){
		List<TaskAbstract> filteredTasks = new ArrayList<>();
		try {			
			for (TaskAbstract task : tasks) {
				int taskId = task.getTaskId();
				if (taskId >= firstIncluded && taskId <= lastIncluded) {
					filteredTasks.add(task);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return filteredTasks;
		
		
	}
	
	
	public boolean rawWriteToExcelFile(List<TaskAbstract> tasks){
		try {
			String sheetName =  java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss").format(java.time.LocalDateTime.now());
			
			// an yparxei to ypologistiko fyllo me to idio onoma prepei na ginei overwrite
	        int ind = workbook.getSheetIndex(sheetName);
	        if (ind >= 0) {
	            workbook.removeSheetAt(ind);
	        }
			int maxEnd = tasks.get(tasks.size()-1).getTaskEnd();
			Sheet sheet = workbook.createSheet(sheetName);
			
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("");
			headerRow.createCell(1).setCellValue("Level");
			headerRow.createCell(2).setCellValue("Id");
			headerRow.createCell(3).setCellValue("Description");
			headerRow.createCell(4).setCellValue("Cost");
			headerRow.createCell(5).setCellValue("Effort");
			for (int i=6; i < maxEnd+6 ; i++) {
				headerRow.createCell(i).setCellValue(i-5); // gia gemisia apo times 1 ews max taskEnd
			}
			
			int rowNumber=1;
			for(TaskAbstract task : tasks) {
				Row row = sheet.createRow(rowNumber++);
			
				if(!task.isSimple()) {
					row.createCell(1).setCellValue("Top");
				}else {
					row.createCell(1).setCellValue("");
				}
				
				row.createCell(2).setCellValue(task.getTaskId());
				row.createCell(3).setCellValue(task.getTaskText());
				row.createCell(4).setCellValue(task.getCost());
				row.createCell(5).setCellValue(task.getEffort());
				for (int i=6; i < maxEnd +6 ; i++) {
					if (i >= task.getTaskStart() + 6 && i <= task.getTaskEnd()+6) { //an to i einai anamesa apo start kai end bazei x alliws afhnei keno gia na mporei na to morfopoihsei meta
						row.createCell(i).setCellValue("x");
					}
					else {
						row.createCell(i);
					}
				}
			}
			
			//grafoume to workbook sto arxeio
			try (OutputStream fileOut = new FileOutputStream(targetPath)) {
			    workbook.write(fileOut);
			}catch(Exception e){
				return false;
			}
			
			
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	
	public String addFontedStyle(String styleName, short styleFontColor, short styleFontHeightInPoints, String styleFontName,
			boolean styleFontBold, boolean styleFontItalic, boolean styleFontStrikeout, short styleFillForegroundColor,
			String styleFillPatternString, String HorizontalAlignmentString, boolean styleWrapText){
	
		
		try {
			CellStyle cellStyle = workbook.createCellStyle();

			Font font = workbook.createFont();
			
			font.setColor(styleFontColor);
			font.setFontHeightInPoints(styleFontHeightInPoints);
			font.setFontName(styleFontName);
			font.setBold(styleFontBold);
			font.setItalic(styleFontItalic);
			font.setStrikeout(styleFontStrikeout);
			cellStyle.setFont(font);
			
			cellStyle.setFillForegroundColor(styleFillForegroundColor);
			if (styleFillPatternString.equals("Solid") || styleFillPatternString.equals("SOLID") || styleFillPatternString.equals("solid")) {
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			} else {
				cellStyle.setFillPattern(FillPatternType.NO_FILL);
			} 
			
			// apo default vazei to left
			if (HorizontalAlignmentString.equals("center") || HorizontalAlignmentString.equals("Center") || HorizontalAlignmentString.equals("CENTER")) {
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
			} else if (HorizontalAlignmentString.equals("right") || HorizontalAlignmentString.equals("Right") || HorizontalAlignmentString.equals("RIGHT")) {
				cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			} else {
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
			}
			cellStyle.setWrapText(styleWrapText);
			
			styles.put(styleName, cellStyle);
			return styleName;
		}catch(Exception e) {
			return "Normal";
		}
		
		
		
			
			}
	
	
	public boolean createNewSheet(String sheetName, List<TaskAbstract> tasks, String headerStyleName, String topBarStyleName,
			String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName, String normalStyleName){
		try {
			// oi default times, an kapoio apo ta orismata vrethei sto hashmap tha to antikatastisoume meta
			CellStyle header = styles.get("DefaultHeaderStyle");
			CellStyle normal = styles.get("Normal");
			CellStyle topTaskBar = styles.get("TopTask_bar_style");
			CellStyle topTaskData = styles.get("TopTask_data_style");
			CellStyle nonTopTaskBar = styles.get("NonTopTask_bar_style");
			CellStyle nonTopTask = styles.get("NonTopTask_data_style");
			
			if (styles.containsKey(headerStyleName)) {
				header = styles.get(headerStyleName);
			}
			if (styles.containsKey(topBarStyleName)) {
				topTaskBar = styles.get(topBarStyleName);
			}
			if (styles.containsKey(topDataStyleName)) {
				topTaskData = styles.get(topDataStyleName);
			}
			if (styles.containsKey(nonTopBarStyleName)) {
				nonTopTaskBar = styles.get(nonTopBarStyleName);
			}
			if (styles.containsKey(nonTopDataStyleName)) {
				nonTopTask = styles.get(nonTopDataStyleName);
			}
			if (styles.containsKey(normalStyleName)) {
				normal = styles.get(normalStyleName);
			}
			
						
			// an yparxei to ypologistiko fyllo me to idio onoma prepei na ginei overwrite
	        int ind = workbook.getSheetIndex(sheetName);
	        if (ind >= 0) {
	            workbook.removeSheetAt(ind);
	        }
			int maxEnd = tasks.get(tasks.size()-1).getTaskEnd();
			Sheet sheet = workbook.createSheet(sheetName);
			
			Row headerRow = sheet.createRow(0);
			Cell cell1 = headerRow.createCell(0);
			cell1.setCellValue("");
			cell1.setCellStyle(header);
			Cell cell2 = headerRow.createCell(1);
			cell2.setCellValue("Level");
			cell2.setCellStyle(header);

			Cell cell3 =headerRow.createCell(2);
			cell3.setCellValue("Id");
			cell3.setCellStyle(header);

			Cell cell4 = headerRow.createCell(3);
			cell4.setCellValue("Description");
			cell4.setCellStyle(header);

			Cell cell5 = headerRow.createCell(4);
			cell5.setCellValue("Cost");
			cell5.setCellStyle(header);

			Cell cell6 = headerRow.createCell(5);
			cell6.setCellValue("Effort");
			cell6.setCellStyle(header);

			for (int i=6; i < maxEnd+6 ; i++) {
				Cell newCell = headerRow.createCell(i);
				newCell.setCellValue(i-5); // gia gemisia apo times 1 ews max taskEnd
				newCell.setCellStyle(header);
			}
			
			int rowNumber=1;
			for(TaskAbstract task : tasks) {
				Row row = sheet.createRow(rowNumber);
				
				
				Cell newCell = row.createCell(0);
				newCell.setCellStyle(normal);

				if(!task.isSimple()) { // theloume diaforetika styles gia tis top kai tis subtasks
					newCell = row.createCell(1);
					newCell.setCellValue("TOP");
					newCell.setCellStyle(topTaskData);
					
					
					newCell = row.createCell(2);
					newCell.setCellValue(task.getTaskId());
					newCell.setCellStyle(topTaskData);

					newCell = row.createCell(3);
					newCell.setCellValue(task.getTaskText());
					newCell.setCellStyle(topTaskData);
					
					newCell = row.createCell(4);
					newCell.setCellValue(task.getCost());
					newCell.setCellStyle(topTaskData);
					
					newCell = row.createCell(5);
					newCell.setCellValue(task.getEffort());
					newCell.setCellStyle(topTaskData);
					
					for (int i=6; i < maxEnd +6 ; i++) {
						
						newCell = row.createCell(i);
						
						if (i >= task.getTaskStart() + 6 && i <= task.getTaskEnd()+6) { //an to i einai anamesa apo start kai end bazei x alliws afhnei keno gia na mporei na to morfopoihsei meta
							newCell.setCellStyle(topTaskBar);
						}
						else {
							newCell.setCellStyle(normal);
						}
					}
					
				}else {
					newCell = row.createCell(1);
					newCell.setCellValue("");
					newCell.setCellStyle(normal);
					
					
					newCell = row.createCell(2);
					newCell.setCellValue(task.getTaskId());
					newCell.setCellStyle(nonTopTask);

					newCell = row.createCell(3);
					newCell.setCellValue(task.getTaskText());
					newCell.setCellStyle(nonTopTask);
					
					newCell = row.createCell(4);
					newCell.setCellValue(task.getCost());
					newCell.setCellStyle(nonTopTask);
					
					newCell = row.createCell(5);
					newCell.setCellValue(task.getEffort());
					newCell.setCellStyle(nonTopTask);
					
					for (int i=6; i < maxEnd +6 ; i++) {
						
						newCell = row.createCell(i);
						
						if (i >= task.getTaskStart() + 6 && i <= task.getTaskEnd()+6) { //an to i einai anamesa apo start kai end bazei x alliws afhnei keno gia na mporei na to morfopoihsei meta
							newCell.setCellStyle(nonTopTaskBar);
						}
						else {
							newCell.setCellStyle(normal);
						}
					}
				}
				

				rowNumber++;
			}
			
			//grafoume to workbook sto arxeio
			try (OutputStream fileOut = new FileOutputStream(targetPath)) {
			    workbook.write(fileOut);
			}catch(Exception e){
				return false;
			}
			
			
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

	
	
}



