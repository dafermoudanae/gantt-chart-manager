# gantt-chart-manager

Gantt chart manager in Java: loads project tasks from TSV, XLS and XLSX files, computes composite tasks (dates, cost, effort), filters and sorts them, and exports formatted Gantt charts to Excel with Apache POI. Tested with JUnit.

Student team project – Software Development course, University of Ioannina, 2024.

## Requirements
- Java (JDK)
- Apache POI 5.2.3 (`lib/poi-bin-5.2.3`)
- log4j (`lib/log4j`)
- JUnit for tests

## How to Run
1. Clone the repository: `git clone https://github.com/dafermoudanae/gantt-chart-manager.git`
2. Open the project in Eclipse or IntelliJ IDEA and add the `src` folder as a source folder.
3. Add all jar files from the Apache POI and log4j folders to the build path.
4. Run the class `[main class]`.

## Project Structure
- `src/main/java/app`: entry points and naive clients
- `src/main/java/dom/gantt`: task classes (TopLevelTask, SubTask)
- `src/main/java/load`: file loaders for TSV, XLS and XLSX
- `src/main/java/service`: main controller
- `src/test`: JUnit tests and test resources
