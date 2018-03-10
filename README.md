### Object Oriented and Concurrent Programming, SeaPort Project 4 ###

#### Overview ####

The four-part SeaPort Project Series served as the primary means of concept enforcement in CMSC 335, Object Oriented and Current Programming. At its most basic level, the program employs the fundamental principles of object oriented programming and multithreading concurrency to chart the movements of hypothetical ships in a set of sea ports, processing their jobs in real time with the application of included dock workers and moving the vessels about from queue to dock to harbor as their jobs are completed.

Over the course of five projects, including the four required and an alternate Project 4, the author expanded a hand-drawn GUI constructed with Swing that provides users with a comprehensive overview of the world and its contents, in addition to a number of secondary utility features. Users are permitted access to a search function for any and all objects in the world, as well as a sorting option that lists objects by a number of user-selected parameters such as weight, draft, etc. Furthermore, each of the jobs in question is interactive, with the user given the ability to suspend, cancel, or complete ongoing jobs as desired.

#### About Project 4 ####

Project 4 is an expansion of the third project, aimed at improving upon the nature of the jobs progression multithreading function and incorporating dock worker resource pools divided by profession. Moored ships with jobs in need of completion by persons with specific skills are required to request workers from these worker pools before proceeding with their tasks; if no available workers are present, jobs threads are required to relinquish access to the shared resource pools and wait until workers become available. If jobs can never be fulfilled, either due to a lack of any workers of a needed skill or a required number greater than those currently present, the job is cancelled. Furthermore, this project displays each port-based resource pool in the GUI, displaying the number of available and total workers, organized into skill groups.

Included are all files, documentation (notated as `Eissen_Project4.pdf`), and the project requirements rubrics (notated as `SeaPort_Project4_Rubric.pdf`).