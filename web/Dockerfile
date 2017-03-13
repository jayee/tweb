FROM openjdk:8-jdk-alpine
MAINTAINER jonte.eriksson@gmail.com

# Template from docker-payaraserver-full
# See https://github.com/payara/docker-payaraserver-full/blob/master/Dockerfile

ENV PAYARA_PKG https://s3-eu-west-1.amazonaws.com/payara.fish/Payara+Downloads/Payara+4.1.1.164/payara-4.1.1.164.zip
ENV PAYARA_VERSION 164
ENV PKG_FILE_NAME payara-full-$PAYARA_VERSION.zip
ENV PAYARA_PATH /opt/payara41
ENV ADMIN_USER admin
ENV ADMIN_PASSWORD admin
ENV AUTODEPLOY_DIR ${PAYARA_PATH}/glassfish/domains/domain1/autodeploy
ENV JAVA_OPTS -Dconfig.file=/storage_root/tweb.conf

# Default payara ports to expose
EXPOSE 4848 8009 8080 8181 9009

RUN apk update \
 && apk add ca-certificates wget \
 && update-ca-certificates \
 && apk add --no-cache bash \
 && apk add --no-cache su-exec

RUN mkdir -p $PAYARA_PATH

RUN \
wget --quiet -O /opt/$PKG_FILE_NAME $PAYARA_PKG && \
unzip -qq /opt/$PKG_FILE_NAME -d /opt && \
rm /opt/$PKG_FILE_NAME

# set credentials to admin/admin (or the specified above/cmd line)

RUN echo "AS_ADMIN_PASSWORD=admin" >> /opt/tmpfile && echo "AS_ADMIN_NEWPASSWORD=$ADMIN_PASSWORD" >> /opt/tmpfile
RUN echo "AS_ADMIN_PASSWORD=$ADMIN_PASSWORD" >> /opt/pwdfile

RUN \
 $PAYARA_PATH/bin/asadmin start-domain && \
 $PAYARA_PATH/bin/asadmin --user $ADMIN_USER --passwordfile=/opt/tmpfile change-admin-password && \
 $PAYARA_PATH/bin/asadmin --user $ADMIN_USER --passwordfile=/opt/pwdfile enable-secure-admin && \
 $PAYARA_PATH/bin/asadmin restart-domain && \
 $PAYARA_PATH/bin/asadmin --user $ADMIN_USER --passwordfile=/opt/pwdfile create-jvm-options $JAVA_OPTS && \
 rm /opt/tmpfile


WORKDIR $PAYARA_PATH

# Use the runas script to start payara
COPY runas.sh runas.sh
ENTRYPOINT ["/opt/payara41/runas.sh"]
CMD ["bin/asadmin", "start-domain", "--debug=true", "--verbose", "domain1"]

# Deploy to auto deployment directory
RUN mkdir -p ${AUTODEPLOY_DIR}
COPY ./target/tweb.war ${AUTODEPLOY_DIR}


