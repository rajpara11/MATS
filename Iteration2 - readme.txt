
A) Project Authors:

	Brian O'Connor
	Eugene Lee 
	Apurva Saini
	Rajkumar Parameswaran



B) Description of this Project

Unit tests + refined design and coding of the Model + initial design and
code of the Controller: write tests based on the project requirements and your design, and
provide some initial functioning code that passes the tests. The code is allowed to “smell”
at this point. To allow the user to drive the tool without a graphical view, a simple
console-based interface must be provided.


Deliverables: readme file + design + corresponding tests + code + documentation,
all in one zip file. In particular, document the changes you made to your UML
and data structures from Milestone 1 and explain why. Remember that you can
always regenerate your design by reverse engineering your code in Together!
Proper division of the project into several packages at this point is recommended.

This is the first iteration of the term projectfor SYSC 2101.
This projects name is the Multi-user ACtion Tracking System or MATS.



C) Included Files:

	01. mats_controller/MATSController.java
	02. mats_model/MATSModel.java
	03. mats_model/MATSProjectCollection.java
	04. mats_model/MATSComponent.java
	05. mats_model/MATSTask.java
	06. mats_model/MATSTaskState.java
	07. mats_model/MATSSubproject.java
	08. mats_model/MATSUserDatabase.java
	09. mats_model/MATSUser.java
	10. mats_view/MATSView.java
	11. mats_tests/MATSModelTest.java
	12. mats_tests/MATSProjectCollectionTest.java
	13. mats_tests/MATSComponentTest.java
	14. mats_tests/MATSTaskTest.java
	15. mats_tests/MATSTaskStateTest.java
	16. mats_tests/MATSSubprojectTest.java
	17. mats_tests/MATSUserDatabaseTest.java
	18. mats_tests/MATSUserTest.java
	19. mats_tests/MATSControllerTest.java
	20. mats_tests/MATSViewTest.java

	21. UML_Class_Diagram_Iteration2.jpg
	22. UML_Sequence_Diagram_Iteration2.jpg
	23. User Stories.doc
	24. Choice of Data Structures.doc


D) History Changes to Documentation since Iteration 1:

	
	1. Created documentation on the design choices for iteration 1
	2. Updated the design choices to reflect iteration 2
	3. Updated the user stories with moving a task/subproject
	4. Updated the user stories with the list of commands
	5. Updated the UML Class Diagram
	6. Updated the UML Sequence Diagram


E) History Changes to Design since Iteration 1:

	1. Updated the list of methods with adding: 
	2. Created code for all methods
	3. Created J-Unit tests
	4. Separated the model, controller, view, and tests into packages respectively.
	5. 




F) Known Issues:

	1. 

	How does a task know who is trying to modify it?
	Should a chat system be implemented and how?
	Should State be its own class?
	Should application be a multi-user system?
	Since the application is based on the MVC design, we feel that we still don't have a view.




H) Roadmap Ahead:

	We plan to implement a GUI(view), JUnit testing, finish implementing methods and finish putting overall concept together

	
	





	




 
	 
 