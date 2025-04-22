# Fitness Tracker

A JavaFX desktop application for tracking your workouts and fitness goals.

## Features

- Log daily workouts with details like type, duration, intensity, and calories burned
- Track your fitness goals and mark them as completed
- View your workout history in a sortable table
- Visualize your progress with charts showing calories burned over time
- Data persistence using SQLite database

## Requirements

- Java 17 or higher
- Maven

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd fitness-tracker
```

2. Build the project using Maven:
```bash
mvn clean package
```

3. Run the application:
```bash
mvn javafx:run
```

## Usage

### Adding a Workout

1. Navigate to the "Add Workout" tab
2. Select the exercise type from the dropdown
3. Enter the duration in minutes
4. Select the intensity level
5. Enter the calories burned
6. Choose the date (defaults to today)
7. Click "Add Workout"

### Setting Goals

1. Navigate to the "Goals" tab
2. Enter the goal description
3. Set a target value
4. Choose the deadline
5. Click "Add Goal"

### Viewing Progress

- The Dashboard tab shows your recent workouts and a chart of your activity
- The Goals tab displays your current goals and their completion status
- All data is automatically saved to the SQLite database

## Technical Details

- Built with JavaFX 17
- Uses SQLite for data persistence
- Maven for dependency management and build automation
- MVC architecture pattern

## Database Schema

### Workouts Table
```sql
CREATE TABLE workouts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    duration INTEGER NOT NULL,
    intensity TEXT NOT NULL,
    calories_burned INTEGER NOT NULL,
    date TEXT NOT NULL
)
```

### Goals Table
```sql
CREATE TABLE goals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    description TEXT NOT NULL,
    target_value INTEGER NOT NULL,
    deadline TEXT NOT NULL,
    completed BOOLEAN DEFAULT FALSE
)
``` 