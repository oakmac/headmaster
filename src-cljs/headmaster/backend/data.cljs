(ns headmaster.backend.sql-queries
  (:require
    ["fs-plus" :as fs-plus]
    ["glob" :as js-glob]
    ["path" :as js-path]
    [headmaster.backend.config :refer [config]]
    [oops.core :refer [oget]]
    [taoensso.timbre :as timbre]))

; (def sql-files (.sync js-glob "./sql/*.sql"))
;
; (def sql-queries
;   (reduce
;     (fn [queries file-with-path]
;       (let [filename (oget (.parse js-path file-with-path) "name")
;             query-name (keyword filename)
;             file-contents (.readFileSync fs-plus file-with-path "utf8")]
;         (assoc queries query-name file-contents)))
;     {}
;     sql-files))
;
;
;
; (timbre/info sql-queries)
; (js/console.log "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
;
;
;
;

(def sql-queries (atom {}))

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











(defn get-cohort-promise [success-fn error-fn])

(defn go-get-cohort
  "Returns a core.async channel"
  [slug])
