#which image should I start from?
FROM public.ecr.aws/compose-x/amazoncorretto:17
#create some volumes to store information from the application runtime
VOLUME ["/debezium-demo"]
#Expose actuator/health stats
EXPOSE 8080
#copy my jar to ...
COPY target/debezium-demo.jar /debezium-demo.jar
#what to run when the docker run image cmd is launched
ENTRYPOINT ["java","-jar","debezium-demo.jar"]