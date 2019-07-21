(ns headmaster.backend.db.queries
  "Load SQL queries from disk"
  (:require
    ["fs-plus" :as fs-plus]
    ["glob" :as js-glob]
    ["path" :as js-path]
    [oops.core :refer [oget]]
    [taoensso.timbre :as timbre]))

(def ^:private sql-queries (atom {}))

;; -----------------------------------------------------------------------------
;; Public API

(defn load-sql-queries!
  "Load SQL queries from ./sql/*.sql and put them in sql-queries atom"
  []
  (let [sql-files (.sync js-glob "./sql/*.sql")
        queries (reduce
                  (fn [queries-acc file-with-path]
                    (let [filename (oget (.parse js-path file-with-path) "name")
                          query-name (keyword filename)
                          file-contents (.readFileSync fs-plus file-with-path "utf8")]
                      (assoc queries-acc query-name file-contents)))
                  {}
                  sql-files)]
    (timbre/info (str "Loaded " (count queries) " SQL queries from ./sql/*.sql"))
    (reset! sql-queries queries)))

(defn get-query [label]
  ;; TODO: add some docs here
  ;; and some logging if the query does not exist
  (get @sql-queries label))
