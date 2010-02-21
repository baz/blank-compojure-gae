* Blank slate for starting a new Compojure project on Google App Engine.
* I couldn't get any of the other templates to work properly without excessive fiddling, so here is my version.
* You must edit the build.xml file to specify the location of several dependancies.
* Also be aware that currently Compojure does not work out of the box on GAE.  You must remove references to `src/compojure/http/multipart.clj` from `src/compojure/http.clj` and `src/compojure.clj` because it depends on `org.apache.commons.fileupload` which calls `java.rmi.server.UID` - restricted in the GAE sandbox environment.
* Tested with GAE 1.3.1 SDK and Compojure 0.3.2 (modified with references to `multipart` removed).
* Once `build.xml` points to the correct paths of Compojure and GAE dependancies, run `ant devserver` to compile and launch the GAE dev server to test.
