(ns headmaster.backend.express
  (:require
    ["connect-session-knex" :as connect-session-knex]
    ["express-session" :as express-session]
    ["express" :as express]
    ["passport" :as passport]
    ["path" :as path]
    [goog.functions :as gfunctions]
    [headmaster.backend.config :refer [config]]
    [headmaster.backend.db :as db]
    [headmaster.backend.middleware :as middleware]
    [headmaster.backend.routes.pages :as pages]
    [taoensso.timbre :as timbre]))

(def public-path "public/")

(defn- create-session-middleware []
  (let [KnexSessionStore (connect-session-knex express-session)
        store (KnexSessionStore. (js-obj "knex" db/js-conn
                                         "tablename" "Sessions"))]
    (express-session (js-obj "secret" "Headmaster-dgxcXOMAHOwh000ElXoX"
                             "resave" true
                             "saveUninitialized" true
                             "store" store))))

(defn- add-middleware [app]
  (doto app
    (.use (.json express))
    ;; TODO: app.use(express.urlencoded({ extended: false }))
    (.use (create-session-middleware))
    (.use (.initialize passport))
    (.use (.session passport))
    (.use (.static express public-path))))

(defn- add-routes [app]
  (doto app
    (.get "/" pages/homepage)))

(def init-express-server!
  (gfunctions/once
    (fn []
      (doto
        (express)
        add-middleware
        add-routes
        (.listen (:port config)
                 #(timbre/info (str "express.js server started on port " (:port config))))))))
