(ns headmaster.backend.core
  (:require
    [goog.functions :as gfunctions]
    [headmaster.backend.config :refer [config]]
    [headmaster.backend.db :as db]
    [headmaster.backend.db.queries :as queries]
    [headmaster.backend.express :as express]
    [taoensso.timbre :as timbre]))

(defn- after-db-connection []
  (queries/load-sql-queries!)
  (express/init-express-server!))

(def init!
  (gfunctions/once
    (fn []
      (timbre/info "Headmaster server started :)")
      ; (config/load-config!)
      (db/connect-to-db! {:environment :development
                          :on-success after-db-connection}))))
