# JOB SCRAPPER

### WHAT I CAN DO?
I can parse job advertisement on ```https://jobs.techstars.com/jobs``` based on requested job function.

To get results you need to send request to next endpoint ```GET http://localhost:8080/job/search?function=<job function>```, where
```<job function>``` can get next values:
````
Accounting Finance
Administration
Customer Service
Data Science
Design
IT
Legal
Marketing Communications
Operations
Other Engineering
People HR
Product
Quality Assurance
Sales Business Development
Software Engineering
````
```! It's important !```
You shouldn't use ```&``` character, instead replace it with empty string ```""```

To get all parsed results you can use next endpoint ```GET http://localhost:8080/job```

To delete all parsed results you can use delete endpoint ```DELETE http://localhost:8080/job```