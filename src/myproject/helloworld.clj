; a basic application using the google app engine
; when this file is created it will create a class that extends
; javax.servlet.http.HttpServlet which can be mapped in the
; applications web.xml.

(ns myproject.helloworld
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use compojure.http compojure.html)
  (:require [appengine-clj.datastore :as ds])
  (:import [com.google.appengine.api.datastore Query]))

(defn index
  [request]
  (let [items (ds/find-all (Query. "item"))]
    (html
      [:h1 (str "Hello World. There are " (count items) " in the database.")]
      [:a {:href "/new"} "Create another one"])))

(defn new
  [request]
  (do
    (ds/create {:kind "item" :text "something"})
    (redirect-to "/")))

(defroutes helloworld
  (GET "/" index)
  (GET "/new" new))

(defservice helloworld)
