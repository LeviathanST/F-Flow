# Introduction
- F-Flow is an app designed to streamline F-Code management and engagement, built following how "F-Code" works. Drawing inspiration from its role system, guild system, crew system and more, this app simplifies coordination and enhances the F-Code experiences for everyone involved. 

# Installation
## Using docker-compose:

```
docker compose -f compose.prod.yml up -d
```
## Local
- Requirement:
  - Archlinux (btw)
  - maven >= 3.9.9 version
  - tomcat10 >= 10.1.36 version

- Clone this project
```
git clone https://github.com/LeviathanST/F-Flow.git
cd F-Flow
```

- Build and package into war file
```
mvn package

```

- Move the war file to tomcat webapps folder 
```
mv ./target/member-management-1.0.war /var/lib/tomcat10/webapps
```
