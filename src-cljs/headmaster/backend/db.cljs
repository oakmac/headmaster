(ns headmaster.backend.db
  (:require
    ["knex" :as knex-lib]
    [headmaster.backend.config :refer [config]]
    [headmaster.backend.db.queries :as queries]
    [oops.core :refer [oget+]]
    [taoensso.timbre :as timbre]))

(def js-conn nil)

(defn connect-to-db!
  ([] (connect-to-db! :development))
  ([environment]
   (let [js-knexfile (js/require "./knexfile.js")
         js-db-config (oget+ js-knexfile (str "?" (name environment)))]
     (if-not js-db-config
       (timbre/error "Unable to load database config for environment:" environment)
       (let [db-config (js->clj js-db-config)]
         ;; set the connection
         (set! js-conn (knex-lib js-db-config))
         ;; execute a query to test the connection
         (doto (.select js-conn (.raw js-conn "1"))
               (.then (fn [js-result]
                        (if js-result
                          (timbre/info "Connected to database with config:" db-config)
                          (timbre/error "Unable to connect to database with config:" db-config))))
               (.catch (fn []
                         (timbre/error "Unable to connect to database with config:" db-config)))))))))

(defn run-query
  ([q-label]
   (run-query q-label {} (constantly nil)))
  ([q-label arg1]
   (cond
     (map? arg1) (run-query q-label arg1 (constantly nil))
     (fn? arg1) (run-query q-label {} arg1)
     :else (timbre/error "db/run-query called with second argument that was neither a map nor a function")))
  ([q-label q-args result-fn]
   (let [q (queries/get-query q-label)]
     (cond
       (not js-conn) (timbre/error "db/run-query error: unable to run query without database connection:" q-label)
       (not q) (timbre/error "db/run-query error: could not find query with label:" q-label)
       ;; TODO: check that the args are valid for this query
       :else
       (doto (.raw js-conn q (clj->js q-args))
         (.then
           (fn [js-result]
             (result-fn (js->clj js-result :keywordize-keys true))))
         (.catch
           (fn []
             (timbre/error "db/run-query error executing query:" q-label))))))))
