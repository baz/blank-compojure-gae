(ns myproject.servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use compojure.html
     [compojure.http helpers middleware request response routes servlet]))

(defroutes myproject-app
  (GET "/" "Hello World"))

(defservice myproject-app)
