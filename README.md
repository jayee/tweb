# Translation Web - tweb

Easy translation on the web.

- Fast setup with docker
- No database - only files
- Summary of the translation status
- Find keys and texts
- No transformation of files
- Keeps the format/content, including comments
- Use [Google cloud translation API](https://cloud.google.com/translate/) to aid the translation work (requires an API key)


## Setup

Currently the only supported file format for the translation files is [Java properties](https://en.wikipedia.org/wiki/.properties).

The application should be able to run on any Java EE 7 compatible server (tested only with [Payara 4.1](http://www.payara.fish/))
with filesystem access to the translation files.

Setup with [Docker](https://docker.com) is preferable.

Before starting the service you should create a tweb.conf and put it in the project root of the project you are going to translate.

The minimal tweb.conf specifies the languages you want to translate to.

```bash
$ echo 'languages = ["fr", "sv_SE"]' > your_project_to_translate_root/tweb.conf
```

The format of the file is HOCON as specified in [typesafe config](https://github.com/typesafehub/config) and the reference (default) settings are found in [reference.conf](./web/src/main/resources/reference.conf)

Other settings that may be useful to modify are

```
create_missing_files = true
default_language_name = ""
```

### Start via docker cli

By using this approach you are using nothing but the tweb.conf and docker to run the translation service.

```bash
$ cd your_project_to_translate_root
$ docker run -d --name tweb -e "LOCAL_USER_ID=$(id -u $USER)" -v $(pwd):/storage_root -p 8080:8080 jayee/tweb
```

Explanation:
* LOCAL_USER_ID environment variable is used to specify which user id the service is going to use for reading and writing the translation files. In the above example the current user is used.
* The current working directory is volume mapped to the containers /storage_root path used by the service.
* The port 8080 is connected to the port 8080 of the container.


### Start via docker-compose

This is very simple but requires some files from the source repository. First, clone (or download) the git repository

```bash
$ cd your_project_to_translate_root
$ git clone https://github.com/jayee/tweb.git
```

and then

```bash
$ cd tweb/web
$ ./start.sh
```

The service is started with the current user.

### Logs and stopping

Check the logs
```bash
$ docker logs tweb
```

Stop the service
```bash
$ docker stop tweb
```

## Build

tweb is built with [Polymer](https://www.polymer-project.org/) as front-end and [Java EE 7](https://docs.oracle.com/javaee/7/api/toc.htm) as backend.
The front-end is based on the Polymer Starter Kit, see the [app document](./app/README.md). 

To build the tweb front-end you need [Node.js](https://nodejs.org) with [npm](https://www.npmjs.com/), [Bower](https://bower.io) and polymer-cli.

To build the tweb backend you need java 8 sdk and maven.

Build everything by

```bash
$ cd tweb/app
$ bower install

$ cd tweb
$ mvn install
```

Build the docker image by

```bash
$ cd tweb/web
$ docker-compose --build
```

