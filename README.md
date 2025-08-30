## Getting Started

### AEU MSIT Programming Principles


## Folder Structure

The workspace contains three folders by default, where:

- `src`: the folder to maintain sources
- `doc`: the folder to document sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

#### 2025-08-09 Program concepts Vs Programming Principles

-> Lab activity Contact Manger 
- Use HashMap<String,String> for Name -> Phone 
- Add, Search, Remove, And Display contacts 
- Use lambda with forEach for display 
- Principle tie-in : Each Feature in its own method(modularity)

-> 16-08-2025: Lab Activity 
## Create a Product Class
- Private fields: name, price, quantity
- Constructor to initialize all fields
- Public methods: displayInfo(), updatePrice(), isInStock()
- Getter methods for accessing private fields
- Create 3 product objects and test all methods
- Focus: Apply encapsulation and observe how it improves data protection

## -> 30-08-2025: Lab Activity 
### Create a Student Grade Management System
Build a robust student grade management system
- Read student grades from csv file (student.csv)
- Calculate average grade for each student
- write results to output file(result.txt)
- handle all possible exceptions gracefully
- Create log file for all operations

## 1. Stage your changes (select files to commit)
`git add Product.java ProductTest.java` or `git add . `

## 2. Commit your changes to local history with a message
`git commit -m "Complete Product class lab activity"`

## 3. Commit your changes to local history with a message
`git status`
## 4. Push your local commits to GitHub
`git push origin develop`

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
