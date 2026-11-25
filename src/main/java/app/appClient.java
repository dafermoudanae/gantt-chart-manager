package app;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.IndexedColors;

import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class appClient {

	public static void main(String[] args) {
		ApplicationController applicationController = new ApplicationController();
		Scanner input = new Scanner(System.in);
		
		
		
		System.out.println("Dwste to path tou arxeiou eisodou: ");
		String inputPath = input.nextLine();
		
		String inputFileTypeStr[] = inputPath.split("\\.");
		
		System.out.println("Dwste to path tou arxeiou eksodou: ");
		String outputPath = input.nextLine();
		
		String outputFileTypeStr[] = outputPath.split("\\.");


		
		FileTypes inputFileType = null;
		FileTypes outputFileType = null;
		
		if (inputFileTypeStr[inputFileTypeStr.length -1 ].equals("xls")) {
        	inputFileType = FileTypes.XLS;
        } else if (inputFileTypeStr[inputFileTypeStr.length -1 ].equals("xlsx")) {
        	inputFileType = FileTypes.XLSX;
        } else if (inputFileTypeStr[inputFileTypeStr.length -1 ].equals("csv")) {
        	inputFileType = FileTypes.CSV;
        } else if (inputFileTypeStr[inputFileTypeStr.length -1 ].equals("tsv")) {
        	inputFileType = FileTypes.TSV;
        } else {
        	System.out.println("Lathos typos arxeiou gia to arxeio eidosou!\nExodos...\n");
        	System.exit(0);
        }
		
		if (outputFileTypeStr[outputFileTypeStr.length -1].equals("xls")) {
			outputFileType = FileTypes.XLS;
        } else if (outputFileTypeStr[outputFileTypeStr.length -1].equals("xlsx")) {
        	outputFileType = FileTypes.XLSX;
        } else {
        	System.out.println("Lathos typos arxeiou gia to arxeio eksodou!\nExodos...\n");
        	System.exit(0);
        }
		
		//elegxos gia yparksi tou arxeiou gia ttyxon antikatastasi
		
		File checkForExistance = new File(outputPath);
		if (checkForExistance.exists() && checkForExistance.isFile())
		{
			System.out.print("To arxeio yparxei, na [a]ntikatastathei i na ginei [p]rosthiki? (a/p) (default prosthiki): ");
			String epilogi = input.nextLine();
			if (epilogi.equals("a") || epilogi.equals("A")) {
				checkForExistance.delete();
				System.out.println("Epityxis diagrafi, dimiourgia neou arxeiou!");
			}
			else {
				System.out.println("Anoigma arxeiou gia prosthiki fyllwn!");
			}
		}
		
		List<String> tasksAsStrings = applicationController.load(inputPath, inputFileType);
		if (tasksAsStrings == null) {
        	System.out.println("Sfalma sto anoigma arxeiou eisodou, to arxeio den yparxei i den periexei dedomena project!\nExodos...\n");
        	System.exit(0);
		}
		
		System.out.println("----------");
		System.out.println("Fortwthikan ta eksis dedomena apo to arxeio:");
		System.out.println("----------");
		for (String s: tasksAsStrings)
			System.out.println(s);


		ProjectInfo prjInfo = applicationController.prepareTargetWorkbook(outputFileType, outputPath);
		System.out.println("----------");
		System.out.println("\n" + prjInfo);
		System.out.println("----------");
		
		System.out.println("Dimiourgia endiamesis anaparastasis gia ola ta tasks...");
		
		boolean successfulRawWrite = applicationController.rawWriteToExcelFile(applicationController.getAllTasks());
		
		
		if (successfulRawWrite) {
			System.out.println("Epityxis eggrafi");
		} else {
			System.out.println("Sfalma stin eggrafi endiamesis anaparastasis!");
		}
		String menu = "";
		do {
			System.out.print("[D]imiourgia neou fyllou\n[P]rostiki neou style\n[E]xodos\n\nEpilekste (D/P/E): ");
			menu = input.nextLine();
			
			switch (menu) {
				case "D":
				case "d":
					System.out.print("Dwste onoma gia to neo fyllo: ");
					String sheetName = input.nextLine();
					System.out.print("Epilekste filtro gia ta dedomena:\n[O]la ta tasks\nMono ta [T]op tasks\nTasks entos [E]urous\n\nEpilekste (Default: Ola ta tasks) : ");
					String epilogi = input.nextLine();
					List <TaskAbstract> filteredTasks = null;
					if (epilogi.equals("T") || epilogi.equals("t")) {
						filteredTasks = applicationController.getTopLevelTasksOnly();
					} else if (epilogi.equals("E") || epilogi.equals("e")) {
						try {
							System.out.print("Dwste arxiko taskId: ");
							int start = input.nextInt();
							System.out.print("Dwste teliko taskId: ");
							int end = input.nextInt();
							input.nextLine(); // gia na katanalwsei to \n
							filteredTasks = applicationController.getTasksInRange(start, end);
						}
						catch (Exception e){
							System.out.println("Mi egkyri eisodos, fortwnontai ola ta tasks!");
							filteredTasks = applicationController.getAllTasks();
						}
					}
					else {
						filteredTasks = applicationController.getAllTasks();
					}
					
					System.out.print("Thelete na xrisimopoiisete ta [D]efault styles i [C]ustom styles? \n(prepei prwta na exete dimiourgisei kapoio style apo to proigoumeno menou gia custom xrisi): ");
					epilogi = input.nextLine();
					boolean successfulWrite = false;
					if (epilogi.equals("C") || epilogi.equals("c")) {
						System.out.print("Dwste header style (an den yparxei tha mpei to default): ");
						String headerStyle = input.nextLine();
						System.out.print("Dwste top tasks bar style (an den yparxei tha mpei to default): ");
						String topBarStyle = input.nextLine();
						System.out.print("Dwste top task data style (an den yparxei tha mpei to default): ");
						String topDataStyle = input.nextLine();
						System.out.print("Dwste non-top task bar style (an den yparxei tha mpei to default): ");
						String nonTopBarStyle = input.nextLine();
						System.out.print("Dwste non-top task data style (an den yparxei tha mpei to default): ");
						String nonTopDataStyle = input.nextLine();
						System.out.print("Dwste style gia ta kena kelia (an den yparxei tha mpei to default): ");
						String normal = input.nextLine();
						
						successfulWrite = applicationController.createNewSheet(sheetName, filteredTasks, 
								headerStyle, topBarStyle, topDataStyle, nonTopBarStyle, nonTopDataStyle, normal);
					} else {
						successfulWrite = applicationController.createNewSheet(sheetName, filteredTasks, 
								"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal");
					} 
					
					if (successfulWrite) {
						System.out.println("Epityxis eggrafi sto fyllo");
					} else {
						System.out.println("Apotyxia eggrafis sto fyllo");
					}
					break;
				case "P":
				case "p":
					try {
						System.out.print("Dwste to onoma tou neou style: ");
						String styleName = input.nextLine();
						System.out.print("Dialekste ena xrwma gia ti grammatoseira: ");
						String fontColor = input.nextLine();
						fontColor = fontColor.toUpperCase(); // ta xreiazomaste kefalaia
						
						System.out.print("Dialekste megethos grammatoseiras: ");
						int size = input.nextInt(); 
						input.nextLine(); // gia na katanalwsei to \n
						System.out.print("Dialekste grammatoseira: ");
						String font = input.nextLine();
						
						System.out.print("Thelete bold style? (default no) (y/n) : ");
						String option = input.nextLine();
						boolean bold = false;
						if (option.equals("yes") || option.equals("y")) {
							bold = true;
						}
						
						System.out.print("Thelete italic style? (default no) (y/n) : ");
						option = input.nextLine();
						boolean italic = false;
						if (option.equals("yes") || option.equals("y")) {
							italic = true;
						}
						
						System.out.print("Thelete strikeout style? (default no) (y/n) : ");
						option = input.nextLine();
						boolean strikeout = false;
						if (option.equals("yes") || option.equals("y")) {
							strikeout = true;
						}
						
						
						System.out.print("Dialekste ena xrwma gia to keli: ");

						String cellColor = input.nextLine();
						cellColor = cellColor.toUpperCase();
						
								
						System.out.print("Stoixisi keimenou [L]eft/[R]ight/[C]enter, epilekste: (Default:Left) : ");
						String allignment  = input.nextLine();
						
						if (allignment.equals("r") || allignment.equals("R")) {
							allignment = "Right";
						} else if (allignment.equals("c") || allignment.equals("C")) {
							allignment = "Center";
						} else {
							allignment = "Left";
						}
						
						System.out.print("Thelete [a]nadiplwmeno keimeno sto keli? (a gia anadyplwmeno, otidipote gia oxi anadiplwmeno: ");
						option = input.nextLine();
						
						boolean wrap = false;
						if (option.equals("a") || option.equals("A")) {
							wrap = true;
						}	
						 String newStyle = applicationController.addFontedStyle(styleName, IndexedColors.valueOf(fontColor).getIndex(), (short) size, font, 
									bold, italic, strikeout, IndexedColors.valueOf(cellColor).getIndex(), "Solid", allignment, wrap);
						 System.out.println("Dimiourgithike to style "+newStyle);
						 break;
					}
					catch (Exception e) {
						 System.out.println("Sfalma! Den dimiourgithike to style, lathos eisodos xaraktiristikwn!");
						 break;
					}
				case "E":
				case "e":
					System.out.println("Exodos tou programmatos...");
					System.exit(0);
				default:
					break;
			}
			
		} while (true);
		
	}

}

