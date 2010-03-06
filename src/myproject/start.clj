; starting the application in a jetty.
; the original application is decorated by a function that sets up the
; app engine services.

(ns vehicle.gae-proxy
  (:use vehicle.ignition)
  (:use compojure.server.jetty compojure.http compojure.control))

(defmacro with-app-engine
  "testing macro to create an environment for a thread"
  ([body]
    `(with-app-engine env-proxy ~body))
  ([proxy body]
    `(last (doall [(com.google.apphosting.api.ApiProxy/setEnvironmentForCurrentThread ~proxy)
    ~body]))))

(defn login-aware-proxy
  "returns a proxy for the google apps environment that works locally"
  [request]
  (let [email (:email (:session request))]
    (proxy [com.google.apphosting.api.ApiProxy$Environment] []
      (isLoggedIn [] (boolean email))
      (getAuthDomain [] "")
      (getRequestNamespace [] "")
      (getDefaultNamespace [] "")
      (getAttributes [] (java.util.HashMap.))
      (getEmail [] (or email ""))
      (isAdmin [] true)
      (getAppId [] "local"))))

(defn environment-decorator
  "decorates the given application with a local version of the app engine environment"
  [application]
    (fn [request]
      (with-app-engine (login-aware-proxy request)
      (application request))))

(defn init-app-engine
  "Initialize the app engine services."
  ([]
    (init-app-engine "/tmp"))
  ([dir]
    (let [proxy-factory (com.google.appengine.tools.development.ApiProxyLocalFactory.)
    environment (proxy [com.google.appengine.tools.development.LocalServerEnvironment] []
    (getAppDir [] (java.io.File dir)))
    api-proxy (.create proxy-factory environment)]
    (com.google.apphosting.api.ApiProxy/setDelegate api-proxy))))

;; make sure every thread has the environment set up

(defn start-it
  []
  (do
    (init-app-engine)
    (run-server {:port 8080} "/*" (servlet (environment-decorator ignition)))))
