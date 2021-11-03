This project provides a solution to the following task at epam.grow.com:

- Create docker image via dockerfile which inherits from an openjdk image
- Add custom code to your SpringBoot app which is able to connect to a MongoDB in your docker image
- Download a MongoDB image and run it
- Run your docker image and call your SpringBoot app via HTTP. Your app should connect to the other docker container's MongoDB
- Attach a volume and read/write files from/into it from the springboot app , e.g.: log files,...
- Try out pseudo terminal
- Use docker compose too

- The project runnable via "docker-compose up" but first you have to do the followings:
1. Inside the project directory you have to build the project with "mvn install". It is because the container uses the 
built jar file.
2. Need to create an image with: "docker image build -t spring-boot-docker ."

- The project accessible at http://localhost:8080/names where it shows a few randomly generated name and age. Also logs them in the log file
which is in the project folders log directory