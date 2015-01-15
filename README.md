# BtProject
A program which, given details of the organisational chart and the names of two people, prints out the names of all the
people in the chain between them. A list of employees will be provided in a text file, one employee per line, in the following 
format (including the header line):

  | Employee ID | Name            | Manager ID |  
  
  | 1           | Dangermouse     |            |
  
  | 2           | Gonzo the Great | 1          |
  
  | 3           | Invisible Woman | 1          |
  
  | 6           | Black Widow     | 2          |
  
  | 12          | Hit Girl        | 3          |
  
  | 15          | Super Ted       | 3          |
  
  | 16          | Batman          | 6          |
  
  | 17          | Catwoman        | 6          |
  
  
The program will be run like this:
Organization atchm_3803.txt "Batman" "Super Ted"

The appplication was developed using the IntelliJ IDEA 14. The project can be imported by both Eclipse and IntelliJ Idea IDE.
