########################################################################################################################

FROM amazoncorretto:17-alpine

########################################################################################################################

LABEL maintainer="Jérôme ODIER <jerome@odier.io>"

LABEL description="OwnVault Server"

########################################################################################################################

RUN ["mkdir", "-p", "/ownvault/data"]

########################################################################################################################

COPY OwnVaultServer.jar /ownvault/OwnVaultServer.jar

COPY docker-entrypoint.sh /docker-entrypoint.sh

########################################################################################################################

VOLUME /ownvault/data

########################################################################################################################

ENTRYPOINT ["/docker-entrypoint.sh"]

########################################################################################################################

EXPOSE 8666

########################################################################################################################
