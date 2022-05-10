# Please modify FROM to <non-prod ACR being used for your builds>.azurecr.io/iceleap/cicd/base/ubuntu:18.04
FROM ubuntu:18.04
ENV HOME_PATH=/usr/src/app

# The following section needs to go away and should be in the base image
RUN apt-get update -qq && apt-get install -qqy zip wget git curl unzip sudo xz-utils gnupg
# Install OpenJDK 11
RUN wget https://packages.microsoft.com/config/ubuntu/18.04/packages-microsoft-prod.deb -O packages-microsoft-prod.deb
RUN dpkg -i packages-microsoft-prod.deb
RUN apt-get install apt-transport-https
RUN apt-get update
RUN apt-get install -y msopenjdk-11

RUN mkdir -p ${HOME_PATH}
RUN mkdir -p ${HOME_PATH}/appinsights-dependencies
WORKDIR ${HOME_PATH}
COPY src/main/resources/appinsights-dependencies ${HOME_PATH}/appinsights-dependencies
COPY target/*SNAPSHOT.jar ${HOME_PATH}/eapirximmunizationdatahydrator.jar

EXPOSE 8080
CMD [ "java", "-jar", "${HOME_PATH}/eapirximmunizationdatahydrator.jar" ]
