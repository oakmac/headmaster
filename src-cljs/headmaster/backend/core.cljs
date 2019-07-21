(ns headmaster.backend.core
  (:require
    ["express" :as express]
    ["passport" :as passport]
    ["path" :as path]
    [goog.functions :as gfunctions]
    [headmaster.backend.config :refer [config]]
    [headmaster.backend.db :as db]
    [headmaster.backend.db.queries :as queries]
    [oops.core :refer [ocall]]
    [taoensso.timbre :as timbre]))

(def public-path "public/")

(defn homepage [js-req js-res next-fn]
  (timbre/info "homepage 9999999999999999999999999")
  (.send js-res "damn son"))

(defn- foo [js-req js-res next-fn]
  (timbre/info "foo!!!!!")
  (next-fn))

(defn- add-middleware [app]
  (doto app
    (.use (.json express))
    ;; TODO: urlencoded
    ;; TODO: session()
    ;; TODO: passport.initialize()
    ;; TODO: passport.session()
    (.use (.static express public-path))))

(defn- add-routes [app]
  (doto app
    (.use "/" (array foo foo foo homepage))))

(def init-express-server!
  (gfunctions/once
    (fn []
      (doto
        (express)
        add-middleware
        add-routes
        (.listen (:port config)
                 #(timbre/info (str "express.js server started on port " (:port config))))))))

(def init!
  (gfunctions/once
    (fn []
      (timbre/info "Booting up Headmaster server now :)")
      (queries/load-sql-queries!)
      ; (load-config!)
      (db/connect-to-db!)
      (init-express-server!)

      (js/setTimeout
        (fn []
          (db/run-query :get-cohort))
        100))))
