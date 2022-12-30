# Object Oriented Programming Project 2020
### Bedetta Alessandro	
===========================================================================
## A brief introduction

This project consists of an application which allows the user to have a **clear vision of the revisions** made on files located in a Dropbox folder. 
A **revision** takes place whenever we upload a file which is already in the directory, but with some modifications (*A revision is essentially an update*). 
The application allows the user to get information, filtered-data and statistics on the reviews made to the files.


## The Program.

Concretely, the program consists in a Java-developed Web application, which relies on **Spring Boot** to handle the web server. 
As soon as the application is started an embedded Tomcat server is set up. Libraries have been handled using **Maven**


## How to use it…

You can’t use something you don’t have, so first of all, the user must download the application. 
The easiest way to do it is to clone this directory and then import the 'OOP_Project' as *“Maven Project”* in an IDE (*like Eclipse or NetBeans*).
Once the user has the program opened in the IDE, to start the application he just has to right-click on the project directory and run it as Spring Boot App. 
As said before, from now on the application will be listening for requests on the localhost 8080 port.

============================================================================

## Making requests

To interact with the application the user must use an **API/REST Development tool** like *PostMan*. 
There are also some alternatives like *Insomnia REST Client*, *HTTPie*, *Paw*and *soapUI*. The requests follow the HTTP protocol.  
This application only uses the GET and POST methods.  
To send a request,all the user has to do is to choose a route and specify the appropriate parameters.  

## Routes   
METHOD|ROUTE|DESCRIPTION
-|-|-
GET|**/listRev/[file_name]**| Returns all the reviews done on that file.
POST | **/dailyRev/[file_name/all]?date=[yyyy-MM-dd]** | Returns all the reviews made on one or all files on that day.
POST | **/weeklyRev/[file_name/all]?date=[yyyy-MM-dd]** | Returns all the reviews made on one, or all files on the week corresponding to that day.
POST | **/check?token=[token] &path=[path_dir]** | Allows to user to authenticate his credentials (*path indicating the folder to work with and API access token*).
GET | **/metadata/[file_name/all]** | Returns the metadata of one, or all files.
GET | **/stats/[file_name]** | Returns statistics based on time passed between reviews made on the specified file.
GET| **/help** | Returns a guide that shows all the application’s routes.

## Requests Examples

#### 1) Request to list all the reviews made on 'doc1.txt' :   
**localhost:8080/listRev/doc1.txt**  

Returned JSON represents the request:  
 &nbsp;  
**{    
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "name": "doc1.txt",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "path_lower": "/cartella1/doc1.txt",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "path_display": "/Cartella1/doc1.txt",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "id": "id:puacmojhHxcAAAAAAAAAGA",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "client_modified": "2020-09-14T07:32:58Z",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "server_modified": "2020-09-14T07:32:58Z",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "rev": "015af410b251bc300000001ea922600",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "content_hash": "1bfb9eff4e9810db8f559fbc7a1d33992bb8d24dd492c07702261876fd2b465f",  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "size": 32,  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "is_downloadable": true  
}**  
    

#### 2) Request to get the statistics on 'doc2.txt':  
**localhost:8080/stats/doc2.txt** 

Returned JSON (maximum, minimum and average time between 2 reviews and standard deviation ) :  
(In the returned JSON **MM**=months,**DD**=days,**hh**=hours,**mm**=minutes,**ss**=seconds.)  
 &nbsp;  
**{  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "max_time": "10DD22hh39mm46ss ",  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "min_time": "22mm34ss",  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "avarage": "4DD13hh44mm5ss ",  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "stdDev": "1DD0hh45mm27ss "  
}**  

#### 3)Request to list all the reviews made on 'doc3.txt' on 9 of september 2020:  
**localhost:8080/dailyRev/doc3.txt?date=2020-09-09**   

This will return the list of reviews (JSON) and a counter.  
 &nbsp;  
**{   
    &nbsp;&nbsp;&nbsp;"counter": 1,   
    &nbsp;&nbsp;&nbsp;"revs": [  
         &nbsp;&nbsp;&nbsp;&nbsp; {  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "name": "doc1.txt",  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "path_lower": "/cartella1/doc1.txt",  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "path_display": "/Cartella1/doc1.txt",  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "id": "id:puacmojhHxcAAAAAAAAAGA",  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "client_modified": "2020-09-09T17:25:46Z",  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"server_modified": "2020-09-09T17:25:46Z",  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"rev": "015aee4bdfee6ba00000001ea922600 ,  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"content_hash": "51f313877d9222d88510e9b9d4336b64c186328f3775c8595af4b68a21eed806",  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"size": 32,  
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "is_downloadable": true  
         &nbsp;&nbsp;&nbsp;&nbsp;}  
    &nbsp;&nbsp;&nbsp;]  
}**  
#### 4)Request to list all the reviews made on the week corresponding to the  9 of september 2020:  
**localhost:8080/weeklyRev/all?date=2020-09-09**  

The returned list this time contains **reviews made on different files.**  

============================================================================

# UML Diagrams
## Use-cases 
This diagram shows all the **features** that the user can access:
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/UseCase5.PNG)

## Classes
This scheme represents the **program's main structure**.  
All **packages**, **classes** and all their **associations**. For each class all the main *methods* and *attributes* are listed.

![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/ClassDiagram3.PNG)

## Time Sequences
These diagrams aim to show **how the program works** whenever the user wants to perform an operation.  
They consist in an ascending timeline where all the operations done by the application are ordered.  

**1) List Reviews request**    
In this scheme, it can be seen how the memory class uses the 'requestHandler' class to establish the connection to the DropBox API    
and to send the request for the specified file.    
 &nbsp;    
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/seq_ListRevs.PNG)    
 &nbsp;    
 **2) Statistics request**   
 In this particular diagram, we can see how the memory class invokes its own method (**listReview()**) to refresh the reviews Array. It then proceeds to calculate all the statistics and send it back to the controller.   
 &nbsp;     
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/seq_stats.PNG)    
 &nbsp;    
 **3) Daily Reviews request**   
Here we can clearly see how the memory class uses the **'dateFormatHandler' class** to examine the date given by the user. If the date is correct, it is sent back to the memory class, otherwise an error is returned.  
 &nbsp;     
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/seq_daily.PNG)    
 &nbsp;    
 **4) Weekly Reviews request**   
In this 2 diagrams, it's easy to see how the program operates depending on what the user gives as parameters.  
The first diagram shows how the program responds to the weekly-reviews request for a single file... there are not many differences between this one and the daily-reviews diagram. But looking at the second graph we can see how the program operates if the user wants to get the weekly reviews for ALL files.    
In this case the memory class uses its own method **'listFolder'** to fetch the list of file names. It then proceeds to perform the weekly-reviews process on all of them.   
 &nbsp;     
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/seq_weekly.PNG)    
 &nbsp;    
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/seq_allweekly2.PNG)    
 &nbsp; 
 **5) Metadata request**   
These are the 2 diagrams that show how the program returns **metadata** ( got from a **single file** or **all files** ). The process for getting all files metadata is similar to the one done for the weekly-reviews request.
 &nbsp;     
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/metadata2.PNG)    
 &nbsp;  
 **6) User Check request**   
This is the procedure carried out by the application to validate the user's credentials (**access token** and **directory path**)
 &nbsp;     
![](https://github.com/Alessandrob99/OOP_Project/blob/master/OOP_Project/src/main/resources/seq_check.PNG)    
 &nbsp;  

