Pernix Dashboard
===================

Required software
-------------
 1. Maven
 4. Postgres

Setup development environment
-------------

 1. Create database in postgres

 2. Clone repo this repo:  
 `git clone https://github.com/kescobar-pernix/pernix-dashboard-backend`
  

Build the project
-------------

 - Build the project using maven command:  
`mvn package`

 - Build the project using maven but without tests run:  
`mvn package -Dmaven.test.skip=true`

- In windows: 
`mvn package "-Dmaven.test.skip=true"`


Publish on heroku  
-------------
* `git checkout -b new-branch-name`  
* Change database configuration in `src/main/resources/hibernate.cfg.xml`  
* `git add .`  
* `git commit -m "New Release <Date>"`  
* `git push heroku new-branch-name:master -f`  

----------
Crafted by:
[![Pernix-Solutions.com](http://pernix.cr/static/images/pernix-logo.svg)
](http://Pernix-Solutions.com)