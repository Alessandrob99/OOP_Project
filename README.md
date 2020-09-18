# Object Oriented Programming project 2020
### Bedetta Alessandro	
=============================================================================================================================
## A brief introduction

This project consists in an application which allows the user to have a **clear vision of the revisions** made on a file located in a Dropbox folder. 
A **revision** takes place whenever we upload a file which is already in the directory but with some modifications (*A revision is essentially an update*). 
The application allows the user to get information, filtered-data and statistics on the reviews made on the files.


## The Program.

Concretely, the program consists in a Java-developed Web application, which relies on **Spring Boot** to handle the web server. 
As soon as the application is started an embedded Tomcat server is set up. Libraries have been handle using **Maven**


## How to use it…

You can’t use something you don’t have, so first of all the user must download the application. 
The easiest way to do it is to clone this directory and then import the it as *“Maven Project”* in an IDE (* like Eclipse or NetBeans *).
Once the user has the program opened in the IDE, to start the application  he just has to right-click on the project directory and run it as Spring Boot App. 
As said before, from now on the application will be listening for requests on the localhost 8080 port.


## Making requests

To interact with the application the user must use an **API/REST Development tool** like *PostMan*. 
There are also some alternatives like *Insomnia REST Client*, *HTTPie*, *Paw*and *soapUI*. The request follow the HTTP protocol. 
This application only uses the GET and POST methods.

Method|route|description
GET|/listRev/[file_name]| Returns all the reviews made on that file.
-|-|-
POST | **/dailyRev/[file_name/all]?date=[yyyy-MM-dd]** | Returns all the reviews made on one or all files on that day.
POST | **/weeklyRev/[file_name/all]?date=[yyyy-MM-dd]** | Returns all the reviews made on one, or all files on the week corresponding to that day.
POST | **/check?token=[token] &path=[path_dir]** | Allows to user to authenticate his credentials (path indicating the folder to work with and API access token).
GET | **/metadata/[file_name/all]** | Returns the metadata of one, or all files.
GET | **/stats/[file_name]** | Returns statistics based on time passed between reviews made on the specified file.
GET| **/help** | Returns a guide that shows all the application’s routes.

### Examples

1)**Request to list all the reviews made on 'doc1.txt'** :   
*localhost:8080/listRev/doc1.txt*  
Returned JSON representing the request:  
{    
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
}    
    
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
2)**Request to get the statistics on 'doc2.txt'**:  
*localhost:8080/stats/doc2.txt*  
Returned JSON (maximum minimum and average time between 2 reviews and standard deviation ) :  
(In the returned JSON MM=months,DD=days,hh=hours,mm=minutes,ss=seconds.)  
{  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "max_time": "10DD22hh39mm46ss ",  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "min_time": "22mm34ss",  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "avarage": "4DD13hh44mm5ss ",  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "stdDev": "1DD0hh45mm27ss "  
}  

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

3)**Request to list all the reviews made on 'doc3.txt' on 9 of september 2020**:  
*localhost:8080/dailyRev/doc3.txt?date=2020-09-09*  
This will return the list of reviews (JSON).  
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
4)**Request to list all the reviews made on the week corresponding to the  9 of september 2020**:  
*localhost:8080/weeklyRev/all?date=2020-09-09*  
The returned list this time contains reviews made on different files.   
